package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataConfirmSentEvent;
import org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataSuccessfullySentEvent;
import org.fermat.fermat_dap_api.layer.all_definition.events.NewRequestActorNotificationEvent;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.OutgoingAssetMetadataPluginRoot;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 18/04/16.
 */
public class OutgoingAssetMetadataMessageConfirmedEventHandler implements FermatEventHandler {

    //VARIABLE DECLARATION
    private OutgoingAssetMetadataRecorderService recorderService;
    private OutgoingAssetMetadataPluginRoot outgoingAssetMetadataPluginRoot;

    //CONSTRUCTORS

    public OutgoingAssetMetadataMessageConfirmedEventHandler(OutgoingAssetMetadataRecorderService recorderService, OutgoingAssetMetadataPluginRoot outgoingAssetMetadataPluginRoot) {
        this.recorderService = recorderService;
        this.outgoingAssetMetadataPluginRoot = outgoingAssetMetadataPluginRoot;
    }

    //PUBLIC METHODS
    @Override
    public void handleEvent(FermatEvent fermatEvent) throws FermatException {
        if (fermatEvent == null)
            throw new CantSaveEventException(null, "Handling an asset buyer event", "Illegal Argument, this method takes an fermatEvent and was passed an null");

        System.out.println("VAMM: OUTGOING ASSET METADATA RECEIVED A NEW EVENT!");
        System.out.println("VAMM: Type: " + fermatEvent.getEventType() + " - Source: " + fermatEvent.getSource());

        if (recorderService.getStatus() != ServiceStatus.STARTED) {
            throw new TransactionServiceNotStartedException();
        }
        recorderService.receiveNewEvent(fermatEvent);

        if (fermatEvent.getSource() == EventSource.INCOMING_METADATA_TRANSACTION) {
            outgoingAssetMetadataPluginRoot.handleEvent((DigitalAssetMetadataConfirmSentEvent) fermatEvent);
        }
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
