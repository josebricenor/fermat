package org.fermat.fermat_dap_plugin.layer.statistic_aggregator.asset_issuer.developer.version_1.structure.database;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class AssetIssuerDatabaseConstants {
    //DATABASE issuer
    public static final String ASSET_ISSUER_DATABASE = "asset_issuer_database";

    //ASSET ISSUER TABLE
    public static final String ASSET_ISSUER_TABLE = "asset_issuer";
    public static final String ASSET_ISSUER_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String ASSET_ISSUER_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String ASSET_ISSUER_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_ISSUER_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_ISSUER_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_ISSUER_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_ISSUER_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_ISSUER_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_ISSUER_EVENTS_RECORDED_ID_COLUMN_NAME;
}
