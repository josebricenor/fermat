package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;

import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.all_definition.enums.EventStatus;
import org.fermat.fermat_dap_api.layer.all_definition.enums.MetadataTransactionStatus;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetUserDeveloperIdentitiesException;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantExecuteDatabaseOperationException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.internal.MetadataTransactionRecordImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 18/04/16.
 */
public class OutgoingAssetMetadataDAO {

    //VARIABLE DECLARATION
    private final Database database;
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;

    //CONSTRUCTORS
    public OutgoingAssetMetadataDAO(UUID pluginId, PluginDatabaseSystem pluginDatabaseSystem) throws CantExecuteDatabaseOperationException {
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
            DatabaseTable databaseTable = this.database.getTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TABLE_NAME);
            DatabaseTableRecord eventRecord = databaseTable.getEmptyRecord();
            UUID eventRecordID = UUID.randomUUID();

            eventRecord.setUUIDValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_ID_COLUMN_NAME, eventRecordID);
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_EVENT_COLUMN_NAME, eventType);
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_SOURCE_COLUMN_NAME, eventSource);
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_STATUS_COLUMN_NAME, EventStatus.PENDING.getCode());
            eventRecord.setLongValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME, System.currentTimeMillis());
            databaseTable.insertRecord(eventRecord);
        } catch (CantInsertRecordException exception) {
            throw new CantSaveEventException(exception, context, "Cannot insert a record in Asset Appropriation Event Table");
        } catch (Exception exception) {
            throw new CantSaveEventException(FermatException.wrapException(exception), context, "Unexpected exception");
        }
    }

    /**
    * Change the status of a record to CANCELLED
    * @param metadataTransactionRecord the one to be changed
    *
    * */
    public void cancelMetadataTransactionRecord(MetadataTransactionRecord metadataTransactionRecord){
        try {
            updateStatusMetadataTransactionRecord(metadataTransactionRecord.getRecordId(), MetadataTransactionStatus.CANCELLED);
        } catch (CantUpdateRecordException e) {
            e.printStackTrace();
        } catch (CantPersistFileException e) {
            e.printStackTrace();
        } catch (RecordsNotFoundException e) {
            e.printStackTrace();
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
    }

    /**
    * Change the status of a record to SENT
    * @param metadataTransactionRecord the one to be changed
    *
    * */
    public void updateSentMetadataTransactionRecord(MetadataTransactionRecord metadataTransactionRecord){
        try {
            updateStatusMetadataTransactionRecord(metadataTransactionRecord.getRecordId(), MetadataTransactionStatus.SENT);
        } catch (CantUpdateRecordException e) {
            e.printStackTrace();
        } catch (CantPersistFileException e) {
            e.printStackTrace();
        } catch (RecordsNotFoundException e) {
            e.printStackTrace();
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
    }

    /**
    * Change the status of a record to RECEIVE
    * @param metadataTransactionRecord the one to be changed
    *
    * */
    public void updateReceiveMetadataTransactionRecord(MetadataTransactionRecord metadataTransactionRecord){
        try {
            updateStatusMetadataTransactionRecord(metadataTransactionRecord.getRecordId(), MetadataTransactionStatus.RECEIVE);
        } catch (CantUpdateRecordException e) {
            e.printStackTrace();
        } catch (CantPersistFileException e) {
            e.printStackTrace();
        } catch (RecordsNotFoundException e) {
            e.printStackTrace();
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
    }

    /**
    * Change the status of a record to CONFIRMED
    * @param metadataTransactionRecord the one to be changed
    *
    * */
    public void confirmMetadataTransactionRecord(MetadataTransactionRecord metadataTransactionRecord){
        try {
            updateStatusMetadataTransactionRecord(metadataTransactionRecord.getRecordId(), MetadataTransactionStatus.CONFIRMED);
        } catch (CantUpdateRecordException e) {
            e.printStackTrace();
        } catch (CantPersistFileException e) {
            e.printStackTrace();
        } catch (RecordsNotFoundException e) {
            e.printStackTrace();
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
    }

    /**
    * Save a new MetadataTransactionRecord
    *
    * */
    public MetadataTransactionRecord saveNewMetadataTransactionRecord(DAPActor actorFrom, DAPActor actorTo, DigitalAssetMetadata metadataToSend) throws CantSaveEventException {

        String context = "Event Type : " + " - Event Source: ";
        MetadataTransactionRecordImpl metadataTransactionRecord;
        long currentTime;

        try {
            DatabaseTable databaseTable = getOutgoingAssetMetadataTable();
            DatabaseTableRecord eventRecord = databaseTable.getEmptyRecord();
            UUID eventRecordID = UUID.randomUUID();

            eventRecord.setUUIDValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME, eventRecordID);
            eventRecord.setUUIDValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_MESSAGE_ID_COLUMN_NAME, metadataToSend.getMetadataId());
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_MESSAGE_COLUMN_NAME, XMLParser.parseObject(metadataToSend));
            eventRecord.setIntegerValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME, 0);
            eventRecord.setFermatEnum(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME, MetadataTransactionStatus.PENDING);
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_FROM_COLUMN_NAME, XMLParser.parseObject(actorFrom));
            eventRecord.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_TO_COLUMN_NAME, XMLParser.parseObject(actorTo));

            currentTime = System.currentTimeMillis();
            eventRecord.setLongValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TIMESTAMP_COLUMN_NAME, currentTime);

            databaseTable.insertRecord(eventRecord);
            metadataTransactionRecord = new MetadataTransactionRecordImpl(eventRecordID, metadataToSend, MetadataTransactionStatus.PENDING, new Date(currentTime), 0, actorTo, actorFrom);

        } catch (CantInsertRecordException exception) {
            throw new CantSaveEventException(exception, context, "Cannot insert a record in OUTGOING ASSET METADATA Table");
        } catch (Exception exception) {
            throw new CantSaveEventException(FermatException.wrapException(exception), context, "Unexpected exception");
        }

        return metadataTransactionRecord;
    }

    /**
    * Update the status of the metadata
    *
    * */
    public void updateStatusMetadataTransactionRecord(UUID idMetadataTransaction, MetadataTransactionStatus metadataTransactionStatus) throws CantUpdateRecordException, CantPersistFileException, RecordsNotFoundException, CantLoadTableToMemoryException {
        DatabaseTable table = getOutgoingAssetMetadataTable();
        table.addUUIDFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME, idMetadataTransaction, DatabaseFilterType.EQUAL);
        table.loadToMemory();
        if (table.getRecords().isEmpty()) throw new RecordsNotFoundException();
        DatabaseTableRecord record = table.getRecords().get(0);
        record.setStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME, metadataTransactionStatus.toString());
        table.updateRecord(record);
    }

    /**
    * Update the attempts count
    *
    * */
    public void updateAttemptMetadataTransactionRecord(UUID idMetadataTransaction, int attempt) throws CantUpdateRecordException, CantPersistFileException, RecordsNotFoundException, CantLoadTableToMemoryException {
        DatabaseTable table = getOutgoingAssetMetadataTable();
        table.addUUIDFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME, idMetadataTransaction, DatabaseFilterType.EQUAL);
        table.loadToMemory();
        if (table.getRecords().isEmpty()) throw new RecordsNotFoundException();
        DatabaseTableRecord record = table.getRecords().get(0);
        record.setIntegerValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME, attempt);
        table.updateRecord(record);
    }

    /**
    * Return true if the table is empty of PENDING status records
    *
    * */
    public boolean isEmptyOfPending(){
        try {
            return isEmptyByStatus(MetadataTransactionStatus.PENDING);
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
    * Return true if the table is empty of RECEIVE status records
    *
    * */
    public boolean isEmptyOfReceive(){
        try {
            return isEmptyByStatus(MetadataTransactionStatus.RECEIVE);
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
    * Return true if the table is empty of a determinate status
    *
    * */
    public boolean isEmptyByStatus(MetadataTransactionStatus metadataTransactionStatus) throws CantLoadTableToMemoryException {
        DatabaseTable table = getOutgoingAssetMetadataTable();
        table.addFermatEnumFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME, metadataTransactionStatus, DatabaseFilterType.EQUAL);
        table.loadToMemory();
        if (table.getRecords().isEmpty()) return true;
        return false;
    }

    public MetadataTransactionRecord getMetadataTransactionRecordById(UUID idRecord) {
        MetadataTransactionRecord metadataTransactionRecord = null;
        DatabaseTable table;

        // Get Asset Users identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Metadata transaction, table not found.", "Plugin Identity", "Cant get MetadataTransactionRecord, table not found.");
            }
            table.addUUIDFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME, idRecord, DatabaseFilterType.EQUAL);
            table.loadToMemory();
            // 3) Get Metadata Transaction Record.
            metadataTransactionRecord = getMetadataTransactionFromRecord(table.getRecords());
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the values.
        return metadataTransactionRecord;
    }

    /**
    * Bring the last record of a related DigitalAssetMetadata
    *
    * */
    public MetadataTransactionRecord getLastMetadataTransactionRecordByDigitalAssetMetadata(DigitalAssetMetadata digitalAssetMetadata) {
        MetadataTransactionRecord metadataTransactionRecord = null;
        DatabaseTable table;

        // Get Asset Users identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Metadata transaction, table not found.", "Plugin Identity", "Cant get MetadataTransactionRecord, table not found.");
            }
            table.addUUIDFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_MESSAGE_ID_COLUMN_NAME, digitalAssetMetadata.getMetadataId(), DatabaseFilterType.EQUAL);
            table.loadToMemory();
            // 3) Get Metadata Transaction Record.
            metadataTransactionRecord = getMetadataTransactionFromRecord(table.getRecords());
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the values.
        return metadataTransactionRecord;
    }

    /**
    * Bring all the records with a PENDING status
    *
    * */
    public List<MetadataTransactionRecordImpl> getPendingMetadataTransactionRecord(){
        return getMetadataTransactionRecordByStatus(MetadataTransactionStatus.PENDING);
    }

    /**
    * Bring all the records with a RECEIVE status
    *
    * */
    public List<MetadataTransactionRecordImpl> getReceiveMetadataTransactionRecord(){
        return getMetadataTransactionRecordByStatus(MetadataTransactionStatus.RECEIVE);
    }

    /**
    * Bring all the records filter by status
    *
    * */
    public List<MetadataTransactionRecordImpl> getMetadataTransactionRecordByStatus(MetadataTransactionStatus metadataTransactionStatus) {
        List<MetadataTransactionRecordImpl> metadataTransactionRecord = null;
        DatabaseTable table;

        // Get Asset Users identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Metadata transaction, table not found.", "Plugin Identity", "Cant get MetadataTransactionRecord, table not found.");
            }
            table.addFermatEnumFilter(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME, metadataTransactionStatus, DatabaseFilterType.EQUAL);
            table.loadToMemory();
            // 3) Get Metadata Transaction Record.
            metadataTransactionRecord = getMetadataTransactionListFromRecord(table.getRecords());
        } catch (CantLoadTableToMemoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the values.
        return metadataTransactionRecord;
    }

    //PRIVATE METHODS

    private Database openDatabase(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) throws CantExecuteDatabaseOperationException {
        try {
            return pluginDatabaseSystem.openDatabase(pluginId, OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME);
        } catch (CantOpenDatabaseException | DatabaseNotFoundException exception) {
            throw new CantExecuteDatabaseOperationException(exception, "Opening the OUTGOING ASSET METADATA Database", "Error in database plugin.");
        }
    }

    private DatabaseTable getOutgoingAssetMetadataTable() {
        return database.getTable(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TABLE_NAME);
    }

    private MetadataTransactionRecordImpl getMetadataTransactionFromRecord(List<DatabaseTableRecord> records) throws InvalidParameterException {
        MetadataTransactionRecordImpl metadataTransactionRecord = null;
        for (DatabaseTableRecord record : records) {

            metadataTransactionRecord = new MetadataTransactionRecordImpl(
                    record.getUUIDValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME),
                    (DigitalAssetMetadata) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME), DigitalAssetMetadata.class),
                    MetadataTransactionStatus.getByCode(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME)),
                    new Date(record.getLongValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TIMESTAMP_COLUMN_NAME)),
                    record.getIntegerValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME),
                    (DAPActor) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_TO_COLUMN_NAME), DAPActor.class),
                    (DAPActor) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_FROM_COLUMN_NAME), DAPActor.class)
                    );

        }
        return metadataTransactionRecord;
    }

    private List<MetadataTransactionRecordImpl> getMetadataTransactionListFromRecord(List<DatabaseTableRecord> records) throws InvalidParameterException {
        List<MetadataTransactionRecordImpl> metadataTransactionRecordList = null;
        MetadataTransactionRecordImpl metadataTransactionRecord;
        for (DatabaseTableRecord record : records) {

            metadataTransactionRecord = new MetadataTransactionRecordImpl(
                    record.getUUIDValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME),
                    (DigitalAssetMetadata) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ID_COLUMN_NAME), DigitalAssetMetadata.class),
                    MetadataTransactionStatus.getByCode(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_STATUS_COLUMN_NAME)),
                    new Date(record.getLongValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_TIMESTAMP_COLUMN_NAME)),
                    record.getIntegerValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ATTEMPTS_COLUMN_NAME),
                    (DAPActor) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_TO_COLUMN_NAME), DAPActor.class),
                    (DAPActor) XMLParser.parseXML(record.getStringValue(OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_ACTOR_FROM_COLUMN_NAME), DAPActor.class)
                    );

            metadataTransactionRecordList.add(metadataTransactionRecord);
        }
        return metadataTransactionRecordList;
    }

    //INNER CLASSES
}
