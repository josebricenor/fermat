package com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.communications;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.AssetRedeemPointActorNetworkServicePluginRoot;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.database.communications.IncomingMessageDao;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.database.communications.OutgoingMessageDao;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.events.NewNetworkServiceMessageSentNotificationEvent;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsVPNConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by franklin on 18/10/15.
 */
public class CommunicationNetworkServiceRemoteAgent extends Observable {

    /*
* Represent the sleep time for the read or send (2000 milliseconds)
*/
    private static final long SLEEP_TIME = 2000;

    /**
     * Represent the communicationsVPNConnection
     */
    private CommunicationsVPNConnection communicationsVPNConnection;

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealWithEvents Interface member variables.
     */
    private EventManager eventManager;

    /**
     * Represent the incomingMessageDao
     */
    private IncomingMessageDao incomingMessageDao;

    /**
     * Represent the outgoingMessageDao
     */
    private OutgoingMessageDao outgoingMessageDao;

    /**
     * Represent is the tread is running
     */
    private Boolean running;

    /**
     * Represent the read messages tread of this CommunicationNetworkServiceRemoteAgent
     */
    private Thread toReceive;

    /**
     * Represent the send messages tread of this CommunicationNetworkServiceRemoteAgent
     */
    private Thread toSend;

    /**
     * Represent the eccKeyPair
     */
    private ECCKeyPair eccKeyPair;

    /**
     * Constructor with parameters
     *
     * @param eccKeyPair from the plugin root
     * @param errorManager  instance
     * @param incomingMessageDao instance
     * @param outgoingMessageDao instance
     */
    public CommunicationNetworkServiceRemoteAgent(ECCKeyPair eccKeyPair, CommunicationsVPNConnection communicationsVPNConnection, ErrorManager errorManager, EventManager eventManager, IncomingMessageDao incomingMessageDao, OutgoingMessageDao outgoingMessageDao) {

        super();
        this.eccKeyPair                          = eccKeyPair;
        this.errorManager                        = errorManager;
        this.eventManager                        = eventManager;
        this.running                             = Boolean.FALSE;
        this.incomingMessageDao                  = incomingMessageDao;
        this.outgoingMessageDao                  = outgoingMessageDao;
        this.communicationsVPNConnection         = communicationsVPNConnection;


        //Create a thread to receive the messages
        this.toReceive = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running)
                    processMessageReceived();
            }
        });

        //Create a thread to send the messages
        this.toSend = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running)
                    processMessageToSend();
            }
        });

    }

    /**
     * Start the internal threads to make the job
     */
    public void start(){

        //Set to running
        this.running  = Boolean.TRUE;

        //Start the Threads
        toReceive.start();
        toSend.start();

        System.out.println("CommunicationNetworkServiceRemoteAgent - started ");

    }

    /**
     * Pause the internal threads
     */
    public void pause(){
        this.running  = Boolean.FALSE;
    }

    /**
     * Resume the internal threads
     */
    public void resume(){
        this.running  = Boolean.TRUE;
    }

    /**
     * Stop the internal threads
     */
    public void stop(){

        //Stop the Threads
        toReceive.interrupt();
        toSend.interrupt();

        //Disconnect from the service
        communicationsVPNConnection.close();
    }

    /**
     * This method process the message received and save on the
     * data base in the table <code>incoming_messages</code> and notify all observers
     * to the new messages received
     */
    private void processMessageReceived(){

        try {

            //System.out.println("CommunicationNetworkServiceRemoteAgent - communicationsVPNConnection.isActive() = "+communicationsVPNConnection.isActive());

            /**
             * Verified the status of the connection
             */
            if (communicationsVPNConnection.isActive()){

                //System.out.println("CommunicationNetworkServiceRemoteAgent - communicationsVPNConnection.getUnreadMessagesCount() = "+communicationsVPNConnection.getUnreadMessagesCount());

                /**
                 * process all pending messages
                 */
                for (int i = 0; i < communicationsVPNConnection.getUnreadMessagesCount(); i++) {

                    /*
                     * Read the next message in the queue
                     */
                    FermatMessage message = communicationsVPNConnection.readNextMessage();

                    /*
                     * Validate the message signature
                     */
                    AsymmetricCryptography.verifyMessageSignature(message.getSignature(), message.getContent(), communicationsVPNConnection.getRemoteParticipantNetworkService().getIdentityPublicKey());

                    /*
                     * Decrypt the message content
                     */
                    ((FermatMessageCommunication) message).setContent(AsymmetricCryptography.decryptMessagePrivateKey(message.getContent(), eccKeyPair.getPrivateKey()));

                    /*
                     * Change to the new status
                     */
                    ((FermatMessageCommunication) message).setFermatMessagesStatus(FermatMessagesStatus.NEW_RECEIVED);

                    /*
                     * Save to the data base table
                     */
                    incomingMessageDao.create(message);

                    /*
                     * Remove the message from the queue
                     */
                    communicationsVPNConnection.removeMessageRead(message);

                    /*
                     * Notify all observer of this agent that Received a new message
                     */
                    setChanged();
                    notifyObservers(message);

                }

            }

            //Sleep for a time
            if (!toReceive.isInterrupted()) {
                Thread.sleep(CommunicationNetworkServiceRemoteAgent.SLEEP_TIME);
            }

        } catch (InterruptedException e) {
            running = false;
            toReceive.interrupt();
            System.out.println("CommunicationNetworkServiceRemoteAgent - Thread Interrupted stopped ...  ");
        } catch (CantInsertRecordDataBaseException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new Exception("Can not process message received. Error reason: "+e.getMessage()));
        }

    }

    /**
     * This method read for new messages pending to send on the data base in
     * the table <code>outbox_messages</code> and encrypt the message content,
     * sing the message and send it
     */
    public void processMessageToSend(){

        try {

            try {

                Map<String, Object> filters = new HashMap<>();
                filters.put(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_STATUS_COLUMN_NAME, MessagesStatus.PENDING_TO_SEND.getCode());
                filters.put(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_RECEIVER_ID_COLUMN_NAME, communicationsVPNConnection.getRemoteParticipant().getIdentityPublicKey());

                    /*
                     * Read all pending message from database
                     */
                List<FermatMessage> messages = outgoingMessageDao.findAll(filters);
                    /*
                     * For each message
                     */
                for (FermatMessage message: messages){


                    if (communicationsVPNConnection.isActive() && (message.getFermatMessagesStatus() != FermatMessagesStatus.SENT) && communicationsVPNConnection.isConnected()) {

                            /*
                             * Encrypt the content of the message whit the remote network service public key
                             */
                        ((FermatMessageCommunication) message).setContent(AsymmetricCryptography.encryptMessagePublicKey(message.getContent(), communicationsVPNConnection.getRemoteParticipantNetworkService().getIdentityPublicKey()));

                            /*
                             * Sing the message
                             */
                        String signature = AsymmetricCryptography.createMessageSignature(message.getContent(), eccKeyPair.getPrivateKey());
                        ((FermatMessageCommunication) message).setSignature(signature);


                            /*
                             * Send the message
                             */
                        communicationsVPNConnection.sendMessage(message);

                            /*
                             * Change the message and update in the data base
                             */

                        ((FermatMessageCommunication) message).setFermatMessagesStatus(FermatMessagesStatus.SENT);
                        outgoingMessageDao.update(message);


                            /*
                             * Put the message on a event and fire new event
                             */
                        FermatEvent fermatEvent = eventManager.getNewEvent(P2pEventType.NEW_NETWORK_SERVICE_MESSAGE_SENT_NOTIFICATION);
                        fermatEvent.setSource(AssetRedeemPointActorNetworkServicePluginRoot.EVENT_SOURCE);
                        ((NewNetworkServiceMessageSentNotificationEvent) fermatEvent).setData(message);
                        eventManager.raiseEvent(fermatEvent);
                    }
                }

            } catch (CantUpdateRecordDataBaseException e) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new Exception("Can not process messages to send. Error reason: "+e.getMessage()));
            } catch (CantReadRecordDataBaseException e) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new Exception("Can not process messages to send. Error reason: " + e.getMessage()));
            }

            if(!toSend.isInterrupted()){
                //Sleep for a time
                Thread.sleep(CommunicationNetworkServiceRemoteAgent.SLEEP_TIME);
            }

        } catch (InterruptedException e) {
            running = false;
            toSend.interrupt();
            System.out.println("CommunicationNetworkServiceRemoteAgent - Thread Interrupted stopped ...  ");
        }

    }



}
