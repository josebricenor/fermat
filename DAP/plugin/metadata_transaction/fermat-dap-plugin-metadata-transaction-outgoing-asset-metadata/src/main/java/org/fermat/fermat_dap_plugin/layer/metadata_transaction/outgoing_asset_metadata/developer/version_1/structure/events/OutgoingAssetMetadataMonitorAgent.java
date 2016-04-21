package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.events;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPMessageSubject;
import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataSuccessfullySentEvent;
import org.fermat.fermat_dap_api.layer.all_definition.network_service_message.DAPMessage;
import org.fermat.fermat_dap_api.layer.all_definition.network_service_message.content_message.AssetMetadataContentMessage;
import org.fermat.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantSendMessageException;
import org.fermat.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantUpdateMessageStatusException;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;
import org.fermat.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.AssetTransmissionNetworkServiceManager;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDAO;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.internal.MetadataTransactionRecordImpl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 20/04/16.
 */
public class OutgoingAssetMetadataMonitorAgent extends FermatAgent {

    //VARIABLE DECLARATION
    private MetadataTransactionAgent metadataTransactionAgent;

    private final ErrorManager errorManager;
    private final OutgoingAssetMetadataDAO dao;
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final AssetTransmissionNetworkServiceManager assetTransmissionNetworkServiceManager;
    private final EventManager eventManager;

    //CONSTRUCTORS

    public OutgoingAssetMetadataMonitorAgent(ErrorManager errorManager, OutgoingAssetMetadataDAO dao, AssetTransmissionNetworkServiceManager assetTransmissionNetworkServiceManager, EventManager eventManager) {
        this.errorManager = errorManager;
        this.dao = dao;
        this.assetTransmissionNetworkServiceManager = assetTransmissionNetworkServiceManager;
        this.eventManager = eventManager;
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartAgentException {
        try {
            metadataTransactionAgent = new MetadataTransactionAgent(dao);
            service.submit(metadataTransactionAgent);
            super.start();
        } catch (Exception e) {
            throw new CantStartAgentException(FermatException.wrapException(e), null, null);
        }
    }

    @Override
    public void stop() throws CantStopAgentException {
        try {
            service.shutdown();
            super.stop();
        } catch (Exception e) {
            throw new CantStopAgentException(FermatException.wrapException(e), null, null);
        }
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES

    private class MetadataTransactionAgent implements Runnable {

        private boolean agentRunning, toSend, toConfirm;
        private static final int WAIT_TIME = 20; //SECONDS
        private OutgoingAssetMetadataDAO outgoingAssetMetadataDAO;

        public MetadataTransactionAgent(OutgoingAssetMetadataDAO outgoingAssetMetadataDAO) {
            this.outgoingAssetMetadataDAO = outgoingAssetMetadataDAO;
            startAgent();
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (agentRunning) {
                try {
                    doTheMainTask();
                    Thread.sleep(WAIT_TIME * 1000);
                } catch (Exception e) {
                    errorManager.reportUnexpectedPluginException(Plugins.OUTGOING_ASSET_METADATA, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }
            }
        }

        private void doTheMainTask() {
            sendMetadataTransactionRecord();
            raiseEventReceiveMetadataTransactionRecord();
            checkWorkToDo();
        }

        /**
        * Check if there are no more messages o events to be sent. If it is not, will put the agent to false.
        * */
        private void checkWorkToDo(){
            toSend = outgoingAssetMetadataDAO.isEmptyOfPending() ? false : true;
            toConfirm = outgoingAssetMetadataDAO.isEmptyOfReceive() ? false : true;
            agentRunning = toSend==false && toConfirm==false ? false : true;
        }

        /**
        * Check if there are messages to be sent and send it
        * */
        private void sendMetadataTransactionRecord() {
            boolean sent = false;
            List<MetadataTransactionRecordImpl> metadataTransactionRecords = outgoingAssetMetadataDAO.getPendingMetadataTransactionRecord();

            if(!metadataTransactionRecords.isEmpty()){
                for (MetadataTransactionRecordImpl metadataTransactionRecord : metadataTransactionRecords) {
                    try {
                        sendMessage(metadataTransactionRecord);
                        sent = true;
                        outgoingAssetMetadataDAO.updateSentMetadataTransactionRecord(metadataTransactionRecord);
                    } catch (RecordsNotFoundException e) {
                        e.printStackTrace();
                    } catch (CantUpdateMessageStatusException e) {
                        e.printStackTrace();
                    } catch (CantSetObjectException e) {
                        e.printStackTrace();
                    } catch (CantSendMessageException e) {
                        e.printStackTrace();
                    } finally {
                        if (!sent) {
                            try {
                                outgoingAssetMetadataDAO.updateAttemptMetadataTransactionRecord(metadataTransactionRecord.getRecordId(), metadataTransactionRecord.getFailureCount() + 1);
                            } catch (CantUpdateRecordException e) {
                                e.printStackTrace();
                            } catch (CantPersistFileException e) {
                                e.printStackTrace();
                            } catch (RecordsNotFoundException e) {
                                e.printStackTrace();
                            } catch (CantLoadTableToMemoryException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        /**
        * Check if there are events to be raised and raise it
        * */
        private void raiseEventReceiveMetadataTransactionRecord(){
            List<MetadataTransactionRecordImpl> metadataTransactionRecordList = outgoingAssetMetadataDAO.getReceiveMetadataTransactionRecord();

            if(!metadataTransactionRecordList.isEmpty()){
                for (MetadataTransactionRecordImpl metadataTransactionRecord : metadataTransactionRecordList) {
                    sendEvent(metadataTransactionRecord);
                }
            }
        }

        /**
        * Raise the event
        * */
        public void sendEvent(MetadataTransactionRecord metadataTransactionRecord){
            FermatEvent event = eventManager.getNewEvent(EventType.DIGITAL_ASSET_METADATA_SUCCESSFULLY_SENT);
            event.setSource(EventSource.OUTGOING_METADATA_TRANSACTION);
            ((DigitalAssetMetadataSuccessfullySentEvent) event).setRecord(metadataTransactionRecord);
            eventManager.raiseEvent(event);
        }

        public void startAgent() {
            agentRunning = true;
            toSend = true;
            toConfirm = true;
        }

        /**
        * Build the DAPMessage and call the asset transmission to send the message
        * */
        private void sendMessage(MetadataTransactionRecordImpl metadataTransactionRecord) throws RecordsNotFoundException, CantUpdateMessageStatusException, CantSetObjectException, CantSendMessageException {
            AssetMetadataContentMessage assetMetadataContentMessage = new AssetMetadataContentMessage(metadataTransactionRecord.getAssetMetadata());
            DAPMessage answer = new DAPMessage(assetMetadataContentMessage, metadataTransactionRecord.getActorFrom(), metadataTransactionRecord.getActorTo(), DAPMessageSubject.ASSET_TRANSFER);
            assetTransmissionNetworkServiceManager.sendMessage(answer);
        }

    }
}
