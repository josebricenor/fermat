package org.fermat.fermat_dap_api.layer.all_definition.events;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;

/**
 * This event shall be raised for the {@link org.fermat.fermat_dap_api.layer.dap_metadata_transaction.asset_outgoing.interfaces.AssetOutgoingMetadataTransactionManager} plugin
 * when the metadata is completely sent.
 * <p/>
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public final class DigitalAssetMetadataConfirmSentEvent extends AbstractDAPEvent {

    //VARIABLE DECLARATION
    private MetadataTransactionRecord record;

    //CONSTRUCTORS
    public DigitalAssetMetadataConfirmSentEvent(MetadataTransactionRecord record) {
        super(EventType.DIGITAL_ASSET_METADATA_CONFIRM_SENT);
        this.record = record;
    }

    public DigitalAssetMetadataConfirmSentEvent() {
        super(EventType.DIGITAL_ASSET_METADATA_CONFIRM_SENT);
    }
    //PUBLIC METHODS

    //PRIVATE METHODS

    //GETTER AND SETTERS
    public MetadataTransactionRecord getRecord() {
        return record;
    }

    public void setRecord(MetadataTransactionRecord record) {
        this.record = record;
    }

    //INNER CLASSES
}
