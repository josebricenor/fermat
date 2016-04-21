package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.internal;

import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.all_definition.enums.MetadataTransactionStatus;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 19/04/16.
 */
public class MetadataTransactionRecordImpl implements MetadataTransactionRecord {

    //VARIABLE DECLARATION

    private final UUID id;
    private final DigitalAssetMetadata digitalAssetMetadata;
    private final MetadataTransactionStatus metadataTransactionStatus;
    private final Date date;
    private final int failureCount;
    private final DAPActor actorTo;
    private final DAPActor actorFrom;

    // CONSTRUCTORS

    public MetadataTransactionRecordImpl(UUID id, DigitalAssetMetadata digitalAssetMetadata, MetadataTransactionStatus metadataTransactionStatus, Date date, int failureCount, DAPActor actorTo, DAPActor actorFrom) {
        this.id = id;
        this.digitalAssetMetadata = digitalAssetMetadata;
        this.metadataTransactionStatus = metadataTransactionStatus;
        this.date = date;
        this.failureCount = failureCount;
        this.actorTo = actorTo;
        this.actorFrom = actorFrom;
    }

    // PUBLIC METHODS

    @Override
    public UUID getRecordId() {
        return id;
    }

    @Override
    public DigitalAssetMetadata getAssetMetadata() {
        return digitalAssetMetadata;
    }

    @Override
    public MetadataTransactionStatus getStatus() {
        return metadataTransactionStatus;
    }

    @Override
    public Date getStartTime() {
        return date;
    }

    @Override
    public int getFailureCount() {
        return failureCount;
    }

    // PRIVATE METHODS
    // GETTER AND SETTERS

    public DAPActor getActorTo() {
        return actorTo;
    }

    public DAPActor getActorFrom() {
        return actorFrom;
    }


    // INNER CLASSES

}
