package org.fermat.fermat_dap_plugin.layer.business_transaction.asset_unhold.developer.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetUnholdDatabaseConstants {
    //DATABASE
    public static final String ASSET_UNHOLD_DATABASE = "asset_unhold_database";

    //ASSET UNHOLD TABLE
    public static final String ASSET_UNHOLD_TABLE = "asset_unhold";
    public static final String ASSET_UNHOLD_ID_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String ASSET_UNHOLD_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String ASSET_UNHOLD_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_UNHOLD_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_UNHOLD_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_UNHOLD_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_UNHOLD_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_UNHOLD_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_UNHOLD_EVENTS_RECORDED_ID_COLUMN_NAME;
}
