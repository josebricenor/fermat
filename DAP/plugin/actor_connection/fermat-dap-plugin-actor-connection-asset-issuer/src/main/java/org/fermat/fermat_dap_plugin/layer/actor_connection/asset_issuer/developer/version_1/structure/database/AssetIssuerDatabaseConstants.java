package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public final class AssetIssuerDatabaseConstants {
    //DATABASE
    public static final String ASSET_ISSUER_ACTOR_CONNECTION_DATABASE = "asset_issuer";

    //ASSET INCOMING TABLE
    public static final String ASSET_ISSUER_TABLE_NAME = "asset_issuer_actor";

    public static final String ASSET_ISSUER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME = "linked_identity_publicKey";
    public static final String ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME = "publicKey";
    public static final String ASSET_ISSUER_NAME_COLUMN_NAME = "name";
    public static final String ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME = "connection_state";
    public static final String ASSET_ISSUER_REGISTRATION_DATE_COLUMN_NAME = "registration_date";
    public static final String ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME = "last_connection_date";
    public static final String ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME = "publicKey_extended";
    public static final String ASSET_ISSUER_TYPE_COLUMN_NAME = "issuer_registered_type";
    public static final String ASSET_ISSUER_DESCRIPTION_COLUMN_NAME = "description";
    public static final String ASSET_ISSUER_LOCATION_LATITUDE_COLUMN_NAME = "location_latitude";
    public static final String ASSET_ISSUER_LOCATION_LONGITUDE_COLUMN_NAME = "location_longitude";

    public static final String ASSET_ISSUER_FIRST_KEY_COLUMN = ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME;
}
