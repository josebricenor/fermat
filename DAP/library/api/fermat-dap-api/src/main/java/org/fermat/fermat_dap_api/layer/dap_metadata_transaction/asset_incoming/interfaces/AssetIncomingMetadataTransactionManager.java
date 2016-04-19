package org.fermat.fermat_dap_api.layer.dap_metadata_transaction.asset_incoming.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;

import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions.CantUpdateMetadataTransactionException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public interface AssetIncomingMetadataTransactionManager extends FermatManager {
    /**
     * Whenever this plugin receives a new {@link org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata}
     * and after store the metadata in the {@link org.fermat.fermat_dap_api.layer.metadata.interfaces.DigitalAssetMetadataManager} plugin
     * will raise periodically a specific event for this transaction, whomever is expecting this metadata shall notify its reception
     * of the event.
     *
     * @param record the {@link MetadataTransactionRecord} associated with the transaction.
     * @throws CantUpdateMetadataTransactionException
     */
    void notifyReception(MetadataTransactionRecord record) throws CantUpdateMetadataTransactionException;
}
