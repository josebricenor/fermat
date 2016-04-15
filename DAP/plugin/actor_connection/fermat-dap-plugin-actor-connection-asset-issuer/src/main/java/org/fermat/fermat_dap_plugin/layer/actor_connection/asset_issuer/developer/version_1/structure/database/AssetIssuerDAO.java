package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;

import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantCreateNewDeveloperException;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetUserDeveloperIdentitiesException;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserActorsException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantExecuteDatabaseOperationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public final class AssetIssuerDAO {

    //VARIABLE DECLARATION

    //VARIABLE DECLARATION
    private final Database database;
    private final PluginFileSystem pluginFileSystem;
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;
    private final String ASSET_ISSUER_PROFILE_IMAGE_FILE_NAME = "profileImage";


    //CONSTRUCTORS
    public AssetIssuerDAO(UUID pluginId, PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem) throws CantExecuteDatabaseOperationException {
        this.pluginId = pluginId;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        database = openDatabase(pluginDatabaseSystem, pluginId);
    }

    //PUBLIC METHODS
    public void updateActorAssetIssuer(ActorAssetIssuer issuer) throws CantUpdateRecordException, CantPersistFileException, CantCreateFileException, RecordsNotFoundException, CantLoadTableToMemoryException {
        DatabaseTable table = getAssetIssuerTable();
        table.addStringFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME, issuer.getActorPublicKey(), DatabaseFilterType.EQUAL);
        table.loadToMemory();
        if (table.getRecords().isEmpty()) throw new RecordsNotFoundException();
        DatabaseTableRecord record = table.getRecords().get(0);
        setRecordFromActor(issuer, record);
        record.setLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
        table.updateRecord(record);
    }

    public void saveNewActor(ActorAssetIssuer actorAssetIssuer) throws CantCreateNewDeveloperException, CantInsertRecordException, CantPersistFileException, CantCreateFileException, RecordsNotFoundException, CantLoadTableToMemoryException, CantUpdateRecordException {
        if (issuerExists(actorAssetIssuer.getActorPublicKey())) {
            updateActorAssetIssuer(actorAssetIssuer);
        } else {
            DatabaseTable table = getAssetIssuerTable();
            DatabaseTableRecord record = table.getEmptyRecord();
            setRecordFromActor(actorAssetIssuer, record);
            record.setLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
            table.insertRecord(record);
        }
    }

    public ActorAssetIssuer getIssuerByPublicKey(String issuerPk) {
        try {
            DatabaseTableFilter pkFilter = getPublicKeyFilter(issuerPk);
            List<ActorAssetIssuer> issuers = getIssuerByFilter(pkFilter);
            if (issuers.isEmpty()) return null;
            return issuers.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public DAPConnectionState getConnectionState(String issuerPk) throws CantLoadTableToMemoryException, InvalidParameterException {
        DatabaseTableFilter pkFilter = getPublicKeyFilter(issuerPk);
        List<DatabaseTableRecord> records = getRecordsByFilter(pkFilter);
        if (records.isEmpty()) return DAPConnectionState.ERROR_UNKNOWN;
        else
            return DAPConnectionState.getByCode(records.get(0).getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME));
    }

    public void updateConnectionState(String issuerPublicKey, DAPConnectionState state) throws CantUpdateRecordException, CantLoadTableToMemoryException {
        updateColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME, state, getPublicKeyFilter(issuerPublicKey));
    }

    public void updateExtendedPublicKey(String issuerPublicKey, String extendedPublicKey) throws CantUpdateRecordException, CantLoadTableToMemoryException {
        updateColumn(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, extendedPublicKey, getPublicKeyFilter(issuerPublicKey));
    }

    public List<ActorAssetIssuer> getIssuersByConnectionState(DAPConnectionState state) {
        DatabaseTableFilter stateFilter = getConnectionStateFilter(state);
        try {
            return getIssuerByFilter(stateFilter);
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    public boolean issuerExists(String issuerPublicKey) throws CantLoadTableToMemoryException {
        DatabaseTableFilter filter = getPublicKeyFilter(issuerPublicKey);
        return issuersExistsByFilter(filter);
    }

    public boolean issuerExists(String issuerPublicKey, DAPConnectionState connectionState) throws CantLoadTableToMemoryException {
        DatabaseTableFilter pkFilter = getPublicKeyFilter(issuerPublicKey);
        DatabaseTableFilter stateFilter = getConnectionStateFilter(connectionState);
        return issuersExistsByFilter(pkFilter, stateFilter);
    }

    public List<ActorAssetIssuer> getAllIssuers() {
        try {
            return getIssuerByFilter();
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    public List<ActorAssetIssuer> getIssuersWithExtendedPublicKey() {
        DatabaseTableFilter notEqualsEmpty = constructFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, "", DatabaseFilterType.NOT_EQUALS);
        DatabaseTableFilter notEqualsNull = constructFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, null, DatabaseFilterType.NOT_EQUALS);
        DatabaseTableFilter notEqualsLine = constructFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, "-", DatabaseFilterType.NOT_EQUALS);
        try {
            return getIssuerByFilter(notEqualsEmpty, notEqualsNull, notEqualsLine);
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }


    public ActorAssetIssuer getLastNotification(String issuerPk) throws CantGetAssetUserActorsException, FileNotFoundException, InvalidParameterException, CantLoadFileException, CantCreateFileException, CantLoadTableToMemoryException {
        final DatabaseTable table = getAssetIssuerTable();
        table.addStringFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME, issuerPk, DatabaseFilterType.EQUAL);
        table.setFilterTop(String.valueOf(1));
        table.addFilterOrder(AssetIssuerDatabaseConstants.ASSET_ISSUER_REGISTRATION_DATE_COLUMN_NAME, DatabaseFilterOrder.DESCENDING);
        table.loadToMemory();
        if(table.getRecords().isEmpty()) return null;
        DatabaseTableRecord record = table.getRecords().get(0);
        return getActorFromRecord(record);
    }

    public List<DAPActor> getAllWaitingActorAssetIssuer(final DAPConnectionState dapConnectionState,
                                                        final int max,
                                                        final int offset) throws CantLoadTableToMemoryException, FileNotFoundException, InvalidParameterException, CantLoadFileException, CantCreateFileException {
        List<DAPActor> dapActors = new ArrayList<>();
        DatabaseTable table = getAssetIssuerTable();
        table.addStringFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME, dapConnectionState.getCode(), DatabaseFilterType.EQUAL);
        table.setFilterOffSet(String.valueOf(offset));
        table.setFilterTop(String.valueOf(max));
        table.loadToMemory();
        for (DatabaseTableRecord record : table.getRecords()) {
            dapActors.add(getActorFromRecord(record));
        }
        return dapActors;
    }


    //PRIVATE METHODS
    private void updateColumn(String columnName, Object value, DatabaseTableFilter filter) throws CantUpdateRecordException, CantLoadTableToMemoryException {
        DatabaseTable table = getAssetIssuerTable();
        table.addStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
        table.loadToMemory();
        for (DatabaseTableRecord record : table.getRecords()) {
            if (value instanceof String) {
                record.setStringValue(columnName, (String) value);
            } else if (value instanceof Long) {
                record.setLongValue(columnName, (Long) value);
            } else if (value instanceof Integer) {
                record.setIntegerValue(columnName, (Integer) value);
            } else if (value instanceof FermatEnum) {
                record.setFermatEnum(columnName, (FermatEnum) value);
            } else {
                record.setStringValue(columnName, value.toString());
            }
            table.updateRecord(record);
        }
    }

    private List<DatabaseTableRecord> getRecordsByFilter(DatabaseTableFilter... filters) throws CantLoadTableToMemoryException {
        DatabaseTable table = getAssetIssuerTable();
        for (DatabaseTableFilter filter : filters) {
            table.addStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
        }
        table.loadToMemory();
        return table.getRecords();
    }

    private List<ActorAssetIssuer> getIssuerByFilter(DatabaseTableFilter... filters) throws FileNotFoundException, InvalidParameterException, CantLoadFileException, CantCreateFileException, CantLoadTableToMemoryException {
        List<DatabaseTableRecord> records = getRecordsByFilter(filters);
        List<ActorAssetIssuer> toReturn = new ArrayList<>();
        for (DatabaseTableRecord record : records) {
            toReturn.add(getActorFromRecord(record));
        }
        return toReturn;
    }

    private boolean issuersExistsByFilter(DatabaseTableFilter... filters) throws CantLoadTableToMemoryException {
        DatabaseTable table = getAssetIssuerTable();
        for (DatabaseTableFilter filter : filters) {
            table.addStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
        }
        table.loadToMemory();
        return !table.getRecords().isEmpty();
    }

    private DatabaseTableFilter getPublicKeyFilter(final String issuerPk) {
        return constructFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME, issuerPk, DatabaseFilterType.EQUAL);
    }

    private DatabaseTableFilter getConnectionStateFilter(final DAPConnectionState state) {
        return constructFilter(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME, state.getCode(), DatabaseFilterType.EQUAL);
    }

    private DatabaseTableFilter constructFilter(final String column, final String value, final DatabaseFilterType type) {
        return new DatabaseTableFilter() {
            @Override
            public void setColumn(String column) {

            }

            @Override
            public void setType(DatabaseFilterType type) {

            }

            @Override
            public void setValue(String value) {

            }

            @Override
            public String getColumn() {
                return column;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public DatabaseFilterType getType() {
                return type;
            }
        };
    }

    private void persistNewAssetIssuerProfileImage(String publicKey, byte[] profileImage) throws CantCreateFileException, CantPersistFileException {
        PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                DeviceDirectory.LOCAL_USERS.getName(),
                ASSET_ISSUER_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                FilePrivacy.PRIVATE,
                FileLifeSpan.PERMANENT
        );

        file.setContent(profileImage);

        file.persistToMedia();
    }

    private ActorAssetIssuer getActorFromRecord(DatabaseTableRecord record) throws InvalidParameterException, FileNotFoundException, CantCreateFileException, CantLoadFileException {
        return new AssetIssuerActorRecord(record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME),
                record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_NAME_COLUMN_NAME),
                DAPConnectionState.getByCode(record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME)),
                record.getDoubleValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LATITUDE_COLUMN_NAME),
                record.getDoubleValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LONGITUDE_COLUMN_NAME),
                record.getLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_REGISTRATION_DATE_COLUMN_NAME),
                record.getLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME),
                Actors.getByCode(record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_TYPE_COLUMN_NAME)),
                record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_DESCRIPTION_COLUMN_NAME),
                record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME),
                getAssetIssuerProfileImagePrivateKey(record.getStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME)));
    }

    private void setRecordFromActor(ActorAssetIssuer issuer, DatabaseTableRecord recordToSet) throws CantPersistFileException, CantCreateFileException {
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_COLUMN_NAME, issuer.getActorPublicKey());
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_NAME_COLUMN_NAME, issuer.getName());
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_CONNECTION_STATE_COLUMN_NAME, issuer.getDapConnectionState().getCode());
        recordToSet.setDoubleValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LATITUDE_COLUMN_NAME, issuer.getLocationLatitude());
        recordToSet.setDoubleValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LOCATION_LONGITUDE_COLUMN_NAME, issuer.getLocationLongitude());
        recordToSet.setLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_REGISTRATION_DATE_COLUMN_NAME, issuer.getRegistrationDate());
        recordToSet.setLongValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_LAST_CONNECTION_DATE_COLUMN_NAME, issuer.getLastConnectionDate());
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_TYPE_COLUMN_NAME, issuer.getType().getCode());
        if(issuer.getDescription() != null)
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_DESCRIPTION_COLUMN_NAME, issuer.getDescription());
        if(issuer.getExtendedPublicKey() != null)
        recordToSet.setStringValue(AssetIssuerDatabaseConstants.ASSET_ISSUER_PUBLIC_KEY_EXTENDED_COLUMN_NAME, issuer.getExtendedPublicKey());
        persistNewAssetIssuerProfileImage(issuer.getActorPublicKey(), issuer.getProfileImage());
    }

    private byte[] getAssetIssuerProfileImagePrivateKey(String publicKey) throws FileNotFoundException, CantCreateFileException, CantLoadFileException {
        byte[] profileImage;
        PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                DeviceDirectory.LOCAL_USERS.getName(),
                ASSET_ISSUER_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                FilePrivacy.PRIVATE,
                FileLifeSpan.PERMANENT
        );

        file.loadFromMedia();

        profileImage = file.getContent();

        return profileImage;
    }

    private Database openDatabase(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) throws CantExecuteDatabaseOperationException {
        try {
            return pluginDatabaseSystem.openDatabase(pluginId, AssetIssuerDatabaseConstants.ASSET_ISSUER_ACTOR_CONNECTION_DATABASE);
        } catch (CantOpenDatabaseException | DatabaseNotFoundException exception) {
            throw new CantExecuteDatabaseOperationException(exception, "Opening the Asset Reception Transaction Database", "Error in database plugin.");
        }
    }

    private DatabaseTable getAssetIssuerTable() {
        return database.getTable(AssetIssuerDatabaseConstants.ASSET_ISSUER_TABLE_NAME);
    }

    //INNER CLASSES
}
