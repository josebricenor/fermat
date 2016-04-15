package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public final class AssetIssuerDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;

    public AssetIssuerDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }

    public Database createDatabase() throws CantCreateDatabaseException {
        Database database;
        try {
            database = this.pluginDatabaseSystem.createDatabase(pluginId, AssetIssuerDatabaseConstants.ASSET_ISSUER_ACTOR_CONNECTION_DATABASE);
        } catch (CantCreateDatabaseException cantCreateDatabaseException) {
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateDatabaseException, "", "Exception not handled by the plugin, There is a problem and i cannot create the database.");
        }

        try {
            DatabaseTableFactory assetIssuerTable;
            DatabaseFactory databaseFactory = database.getDatabaseFactory();

            assetIssuerTable = databaseFactory.newTableFactory(pluginId, AssetIssuerDatabaseConstants.ASSET_ISSUER_TABLE_NAME);

            /* Asset Issuer Table primary key.*/

            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 256, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 256, Boolean.TRUE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_NAME_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME, DatabaseDataType.STRING, 10, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_REGISTRATION_DATE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, DatabaseDataType.STRING, 512, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 50, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_DESCRIPTION_COLUMN_NAME, DatabaseDataType.STRING, 256, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LATITUDE_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.FALSE);
            assetIssuerTable.addColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LONGITUDE_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.FALSE);

            assetIssuerTable.addIndex(AssetIssuerDatabaseConstants.ASSET_ISSUER_FIRST_KEY_COLUMN);

            try {
                //Create the digitalAssetMetadataTable
                databaseFactory.createTable(pluginId, assetIssuerTable);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the registered digitalAssetMetadataTable.");
            }

        } catch (InvalidOwnerIdException invalidOwnerId) {
            /**
             * This shouldn't happen here because I was the one who gave the owner id to the database file system,
             * but anyway, if this happens, I can not continue.
             */
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, invalidOwnerId, "", "There is a problem with the ownerId of the database.");
        }
        return database;
    }


    public boolean databaseExists() {
        try {
            pluginDatabaseSystem.openDatabase(pluginId, AssetIssuerDatabaseConstants.ASSET_ISSUER_ACTOR_CONNECTION_DATABASE);
            return true;
        } catch (CantOpenDatabaseException | DatabaseNotFoundException e) {
            return false;
        }
    }
}
