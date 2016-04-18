package org.fermat.fermat_dap_plugin.layer.offer_transaction.incoming_asset_offer.developer.version_1.structure.database;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class IncomingAssetOfferDatabaseConstants {
    //DATABASE INCOM
    public static final String INCOMING_ASSET_OFFER_DATABASE = "incoming_asset_offer_database";

    //BITCOIN FOR ASSET TABLE
    public static final String INCOMING_ASSET_OFFER_TABLE = "incoming_asset_offer";
    public static final String INCOMING_ASSET_OFFER_COLUMN_NAME = "id";

    //EVENTS RECORDED TABLE
    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_TABLE_NAME = "events_recorded";

    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String INCOMING_ASSET_OFFER_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = INCOMING_ASSET_OFFER_EVENTS_RECORDED_ID_COLUMN_NAME;
}
