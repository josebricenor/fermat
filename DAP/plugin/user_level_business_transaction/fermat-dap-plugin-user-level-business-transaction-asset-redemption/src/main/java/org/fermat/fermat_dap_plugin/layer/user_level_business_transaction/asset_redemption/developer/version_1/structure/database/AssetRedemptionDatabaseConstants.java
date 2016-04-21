package org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_redemption.developer.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetRedemptionDatabaseConstants {
    //DATABASE
    public static final String ASSET_REDEMPTION_DATABASE = "asset_redemption_database";

    //ASSET REDEMPTION TABLE
    public static final String ASSET_REDEMPTION_TABLE = "asset_redemption";
    public static final String ASSET_REDEMPTION_ID_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_REDEMPTION_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_REDEMPTION_EVENTS_RECORDED_ID_COLUMN_NAME;
}
