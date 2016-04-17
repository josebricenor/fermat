package org.fermat.fermat_dap_plugin.layer.statistic_collector.asset_transfer.developer.version_1.structure.database;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class AssetTransferDatabaseConstants {
    //DATABASE
    public static final String ASSET_TRANSFER_DATABASE = "asset_transfer_database";

    //ASSET TRANSFER TABLE
    public static final String ASSET_TRANSFER_TABLE = "asset_transfer";
    public static final String ASSET_TRANSFER_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String ASSET_TRANSFER_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String ASSET_TRANSFER_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_TRANSFER_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_TRANSFER_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_TRANSFER_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_TRANSFER_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_TRANSFER_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_TRANSFER_EVENTS_RECORDED_ID_COLUMN_NAME;
}
