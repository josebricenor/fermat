package org.fermat.fermat_dap_api.layer.all_definition.events;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;

/**
 * This event shall be raised for the {@link org.fermat.fermat_dap_api.layer.dap_metadata_transaction.asset_outgoing.interfaces.AssetOutgoingMetadataTransactionManager} plugin
 * when the metadata is completely sent.
 * <p/>
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public final class DigitalAssetMetadataSuccessfullySentEvent extends AbstractDAPEvent {

    //VARIABLE DECLARATION
    private MetadataTransactionRecord record;

    //CONSTRUCTORS
    public DigitalAssetMetadataSuccessfullySentEvent(MetadataTransactionRecord record) {
        super(EventType.DIGITAL_ASSET_METADATA_SUCCESSFULLY_SENT);
        this.record = record;
    }

    public DigitalAssetMetadataSuccessfullySentEvent() {
        super(EventType.DIGITAL_ASSET_METADATA_SUCCESSFULLY_SENT);
    }
    //PUBLIC METHODS

    //PRIVATE METHODS

    //GETTER AND SETTERS
    public MetadataTransactionRecord getRecord() {
        return record;
    }

    //INNER CLASSES
}
