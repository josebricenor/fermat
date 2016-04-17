package org.fermat.fermat_dap_plugin.layer.swap_transaction.bitcoin_for_asset.developer.version_1.structure.database;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class BitcoinForAssetDatabaseConstants {
    //DATABASE
    public static final String BITCOIN_FOR_ASSET_DATABASE = "bitcoin_for_asset_database";

    //BITCOIN FOR ASSET TABLE
    public static final String BITCOIN_FOR_ASSET_TABLE = "bitcoin_for_asset";
    public static final String BITCOIN_FOR_ASSET_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String BITCOIN_FOR_ASSET_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = BITCOIN_FOR_ASSET_EVENTS_RECORDED_ID_COLUMN_NAME;
}
