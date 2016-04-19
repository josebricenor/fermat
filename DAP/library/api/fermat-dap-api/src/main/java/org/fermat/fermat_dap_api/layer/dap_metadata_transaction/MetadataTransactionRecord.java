package org.fermat.fermat_dap_api.layer.dap_metadata_transaction;

import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.all_definition.enums.MetadataTransactionStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public interface MetadataTransactionRecord extends Serializable {
    /**
     * The unique {@link UUID} that represents this single record, used to identify it.
     *
     * @return
     */
    UUID getRecordId();

    /**
     * The {@link DigitalAssetMetadata} associated with this record.
     * This metadata is stored only on {@link org.fermat.fermat_dap_api.layer.metadata.interfaces.DigitalAssetMetadataManager} plugin.
     *
     * @return
     */
    DigitalAssetMetadata getAssetMetadata();

    /**
     * The transaction status for a metadata transaction where this record is located at. This
     * information could be relevant for other upper-level transactional plugins that are willing to
     * consume this one.
     *
     * @return
     */
    MetadataTransactionStatus getStatus();

    /**
     * The exact time where the outgoing request started.
     *
     * @return
     */
    Date getStartTime();

    /**
     * The failure count for sending the metadata.
     *
     * @return
     */
    int getFailureCount();
}
