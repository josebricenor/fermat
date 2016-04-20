package org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.multi_transaction_asset_issuing.developer.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class MultiTransactionAssetIssuingDatabaseConstants {
    //DATABASE
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_DATABASE = "multi_transaction_asset_issuing_database";

    //MULTI TRANSACTION ASSET ISSUING TABLE
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_TABLE = "multi_transaction_asset_issuing";
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_ID_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = MULTI_TRANSACTION_ASSET_ISSUING_EVENTS_RECORDED_ID_COLUMN_NAME;
}
