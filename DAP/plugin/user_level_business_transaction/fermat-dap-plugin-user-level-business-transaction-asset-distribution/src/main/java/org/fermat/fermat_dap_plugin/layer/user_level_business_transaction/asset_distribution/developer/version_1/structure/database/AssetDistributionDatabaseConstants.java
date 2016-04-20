package org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_distribution.developer.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetDistributionDatabaseConstants {
    //DATABASE
    public static final String ASSET_DISTRIBUTION_DATABASE = "asset_distribution_database";

    //ASSET DISTRIBUTION TABLE
    public static final String ASSET_DISTRIBUTION_TABLE = "asset_distribution";
    public static final String ASSET_DISTRIBUTION_ID_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_DISTRIBUTION_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_DISTRIBUTION_EVENTS_RECORDED_ID_COLUMN_NAME;
}
