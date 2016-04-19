package org.fermat.fermat_dap_api.layer.dap_metadata_transaction.asset_outgoing.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;

import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions.CantStartMetadataTransactionException;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions.CantUpdateMetadataTransactionException;

import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public interface AssetOutgoingMetadataTransactionManager extends FermatManager {

    /**
     * @param actorFrom      the local {@link DAPActor} which is requesting to send the metadata. This actor
     *                       could be found in one of the the DAP Actor Layer plugins.
     * @param actorTo        the external {@link DAPActor} whom will receive the metadata. This actor is usually
     *                       found on the in one of the DAP Actor Connection Layer plugins.
     * @param metadataToSend the {@link DigitalAssetMetadata} that we'll attempt to send. This metadata NEEDS to be
     *                       previously stored on the {@link org.fermat.fermat_dap_api.layer.metadata.interfaces.DigitalAssetMetadataManager} plugin
     *                       since this one won't have the responsibility of store it and will just search for it on that plugin.
     * @return {@link MetadataTransactionRecord} instance that will be use to interact later with this plugin.
     * @throws CantStartMetadataTransactionException any exception that could happen while attempting to start a sending would be wrapped inside this one.
     */
    MetadataTransactionRecord sendMetadata(DAPActor actorFrom, DAPActor actorTo, DigitalAssetMetadata metadataToSend) throws CantStartMetadataTransactionException;

    /**
     * When the metadata is successfully sent. This plugin will raise a {@link org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataSuccessfullySentEvent}
     * periodically, whomever consumes this plugin and started the metadata transaction shall call this method when he receives this events to notify this plugin to
     * stop raising event.
     *
     * @param record The {@link MetadataTransactionRecord} associated with the transaction.
     * @throws CantUpdateMetadataTransactionException
     */
    void notifyReception(MetadataTransactionRecord record) throws CantUpdateMetadataTransactionException;

    /**
     * Call this method for stop attempting send the metadata. In case we cancel the sending while already sent the metadata, but not confirmed
     * the confirmation would be silently ignored and won't raise any event.
     *
     * @param record The {@link MetadataTransactionRecord} associated with the transaction.
     * @throws CantUpdateMetadataTransactionException
     */
    void cancelTransaction(MetadataTransactionRecord record) throws CantUpdateMetadataTransactionException;

    /**
     * Usually, when consuming this plugin and starting a sending we'll store the {@link UUID} of the record associated with
     * that transaction, using this method you can retrieve the whole record which is needed for the interaction with this plugin.
     *
     * @param recordId {@link UUID} the unique ID that represents this record
     * @return a {@link MetadataTransactionRecord} instance or {@code null} in case there's none or an exception happen.
     */
    MetadataTransactionRecord getRecord(UUID recordId);

    /**
     * Call this method if for any reason you need to know the last transaction associated with a specific {@link DigitalAssetMetadata}
     *
     * @param assetMetadata the {@link DigitalAssetMetadata} that is related with the record to search
     * @return a {@link MetadataTransactionRecord} instance or {@code null} in case there's none or an exception happen.
     */
    MetadataTransactionRecord getLastTransaction(DigitalAssetMetadata assetMetadata);

}
