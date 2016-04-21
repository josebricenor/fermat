package org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_transfer.developer.version_1.structure.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventStatus;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantExecuteDatabaseOperationException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;

import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetTransferDAO {

    //VARIABLE DECLARATION

    //VARIABLE DECLARATION
    private final Database database;
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;

    //CONSTRUCTORS
    public AssetTransferDAO(UUID pluginId, PluginDatabaseSystem pluginDatabaseSystem) throws CantExecuteDatabaseOperationException {
        this.pluginId = pluginId;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        database = openDatabase(pluginDatabaseSystem, pluginId);
    }

    //PUBLIC METHODS
    public void saveNewEvent(FermatEvent event) throws CantSaveEventException {
        String eventType = event.getEventType().getCode();
        String eventSource = event.getSource().getCode();
        String context = "Event Type : " + eventType + " - Event Source: " + eventSource;
        try {
            DatabaseTable databaseTable = this.database.getTable(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_TABLE_NAME);
            DatabaseTableRecord eventRecord = databaseTable.getEmptyRecord();
            UUID eventRecordID = UUID.randomUUID();

            eventRecord.setUUIDValue(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_ID_COLUMN_NAME, eventRecordID);
            eventRecord.setStringValue(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_EVENT_COLUMN_NAME, eventType);
            eventRecord.setStringValue(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_SOURCE_COLUMN_NAME, eventSource);
            eventRecord.setStringValue(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_STATUS_COLUMN_NAME, EventStatus.PENDING.getCode());
            eventRecord.setLongValue(AssetTransferDatabaseConstants.ASSET_TRANSFER_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME, System.currentTimeMillis());
            databaseTable.insertRecord(eventRecord);
        } catch (CantInsertRecordException exception) {
            throw new CantSaveEventException(exception, context, "Cannot insert a record in Asset Appropriation Event Table");
        } catch (Exception exception) {
            throw new CantSaveEventException(FermatException.wrapException(exception), context, "Unexpected exception");
        }
    }
    //PRIVATE METHODS

    private Database openDatabase(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) throws CantExecuteDatabaseOperationException {
        try {
            return pluginDatabaseSystem.openDatabase(pluginId, AssetTransferDatabaseConstants.ASSET_TRANSFER_DATABASE);
        } catch (CantOpenDatabaseException | DatabaseNotFoundException exception) {
            throw new CantExecuteDatabaseOperationException(exception, "Opening the Asset Reception Transaction Database", "Error in database plugin.");
        }
    }

    private DatabaseTable getAssetTransferTable() {
        return database.getTable(AssetTransferDatabaseConstants.ASSET_TRANSFER_TABLE);
    }

    //INNER CLASSES
}
