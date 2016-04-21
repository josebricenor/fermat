package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.developer_utils;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_api.layer.all_definition.util.Validate;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;

import org.fermat.fermat_dap_api.layer.dap_transaction.asset_redemption.exceptions.CantInitializeAssetRedeemPointRedemptionTransactionDatabaseException;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDatabaseConstants;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDatabaseFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class OutgoingAssetMetadataDeveloperDatabaseFactory {

    //VARIABLE DECLARATION
    private PluginDatabaseSystem pluginDatabaseSystem;
    private UUID pluginId;
    //CONSTRUCTORS

    public OutgoingAssetMetadataDeveloperDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) throws CantSetObjectException {
        this.pluginDatabaseSystem = Validate.verifySetter(pluginDatabaseSystem, "pluginDatabaseSystem is null");
        this.pluginId = Validate.verifySetter(pluginId, "pluginId is null");
    }

    public OutgoingAssetMetadataDeveloperDatabaseFactory() {
    }

    //PUBLIC METHODS


    public void initializeDatabase() throws CantInitializeAssetRedeemPointRedemptionTransactionDatabaseException {
        try {

             /*
              * Open new database connection
              */
            pluginDatabaseSystem.openDatabase(pluginId, OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_DATABASE);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new CantInitializeAssetRedeemPointRedemptionTransactionDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            OutgoingAssetMetadataDatabaseFactory assetAppropriationDatabaseFactory = new OutgoingAssetMetadataDatabaseFactory(pluginDatabaseSystem, pluginId);

            try {
                  /*
                   * We create the new database
                   */
                assetAppropriationDatabaseFactory.createDatabase();
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeAssetRedeemPointRedemptionTransactionDatabaseException(cantCreateDatabaseException.getMessage());
            }
        }
    }

    public static List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory) {
        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        List<String> outgoingAssetMetadataColumns = new ArrayList<>();
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_MESSAGE_ID_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_MESSAGE_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_FROM_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_TO_COLUMN_NAME);
        outgoingAssetMetadataColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TIMESTAMP_COLUMN_NAME);

        DeveloperDatabaseTable outgoingAssetMetadataTable = developerObjectFactory.getNewDeveloperDatabaseTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME, outgoingAssetMetadataColumns);
        tables.add(outgoingAssetMetadataTable);

        List<String> eventsRecordedColumns = new ArrayList<>();
        eventsRecordedColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_ID_COLUMN_NAME);
        eventsRecordedColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_EVENT_COLUMN_NAME);
        eventsRecordedColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_SOURCE_COLUMN_NAME);
        eventsRecordedColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_STATUS_COLUMN_NAME);
        eventsRecordedColumns.add(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME);

        DeveloperDatabaseTable eventsRecordedTables = developerObjectFactory.getNewDeveloperDatabaseTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TABLE_NAME, eventsRecordedColumns);
        tables.add(eventsRecordedTables);
        return tables;
    }

    public static List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory, UUID pluginId) {
        /**
         * I only have one database on my plugin. I will return its name.
         */
        List<DeveloperDatabase> databases = new ArrayList<>();
        databases.add(developerObjectFactory.getNewDeveloperDatabase(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_DATABASE, pluginId.toString()));
        return databases;
    }

    public static List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, Database database, DeveloperDatabaseTable developerDatabaseTable) {
        /**
         * Will get the records for the given table
         */
        List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<DeveloperDatabaseTableRecord>();
        /**
         * I load the passed table name from the SQLite database.
         */
        DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());
        try {
            selectedTable.loadToMemory();
            List<DatabaseTableRecord> records = selectedTable.getRecords();
            for (DatabaseTableRecord row : records) {
                List<String> developerRow = new ArrayList<String>();
                /**
                 * for each row in the table list
                 */
                for (DatabaseRecord field : row.getValues()) {
                    /**
                     * I get each row and save them into a List<String>
                     */
                    developerRow.add(field.getValue());
                }
                /**
                 * I create the Developer Database record
                 */
                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            /**
             * return the list of DeveloperRecords for the passed table.
             */
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
        return returnedRecords;
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
