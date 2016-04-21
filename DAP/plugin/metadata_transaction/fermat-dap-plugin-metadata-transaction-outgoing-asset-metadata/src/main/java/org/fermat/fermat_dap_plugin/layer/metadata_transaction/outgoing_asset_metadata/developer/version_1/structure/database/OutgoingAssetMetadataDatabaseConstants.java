package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 18/04/16.
 */
public class OutgoingAssetMetadataDatabaseConstants {
    //DATABASE
    public static final String OUTGOING_ASSET_METADATA_DATABASE = "outgoing_asset_metadata_database";

    //OUTGOING ASSET METADATA TABLE
    public static final String OUTGOING_ASSET_METADATA_TABLE_NAME = "outgoing_asset_metadata";
    public static final String OUTGOING_ASSET_METADATA_ID_COLUMN_NAME = "id";
    public static final String OUTGOING_ASSET_METADATA_MESSAGE_ID_COLUMN_NAME = "message_id";
    public static final String OUTGOING_ASSET_METADATA_MESSAGE_COLUMN_NAME = "message";
    public static final String OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME = "attempts";
    public static final String OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME = "status";
    public static final String OUTGOING_ASSET_METADATA_ACTOR_FROM_COLUMN_NAME = "actor_from";
    public static final String OUTGOING_ASSET_METADATA_ACTOR_TO_COLUMN_NAME = "actor_to";
    public static final String OUTGOING_ASSET_METADATA_TIMESTAMP_COLUMN_NAME = "timestamp";

    //EVENTS RECORDED TABLE
    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = OUTGOING_ASSET_METADATA_EVENTS_RECORDED_ID_COLUMN_NAME;
}
