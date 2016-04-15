package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantReceiveRequestException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.actor_connection.asset_issuer.interfaces.AssetIssuerActorConnectionManager;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantCreateNewDeveloperException;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantUpdateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptActorAssetUserException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCreateActorAssetReceiveException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetWaitingException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantRequestAlreadySendActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantDeliverDatabaseException;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.developer_utils.AssetIssuerDeveloperDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database.AssetIssuerDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.events.AssetIssuerRecorderService;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database.AssetIssuerDAO;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database.AssetIssuerDatabaseConstants;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.events.AssetIssuerMonitorAgent;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.internal.AssetIssuerConnectionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public final class AssetIssuerActorConnectionPluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers,
        AssetIssuerActorConnectionManager {


    //VARIABLE DECLARATION
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_BROADCASTER_SYSTEM)
    private Broadcaster broadcaster;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS, layer = Layers.CRYPTO_VAULT, plugin = Plugins.BITCOIN_ASSET_VAULT)
    private AssetVaultManager assetVaultManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.ASSET_ISSUER)
    private AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS, layer = Layers.CRYPTO_MODULE, plugin = Plugins.CRYPTO_ADDRESS_BOOK)
    private CryptoAddressBookManager cryptoAddressBookManager;

    private AssetIssuerDAO dao;
    private AssetIssuerMonitorAgent agent;
    private AssetIssuerRecorderService recorderService;
    private AssetIssuerConnectionManager connectionManager;

    //CONSTRUCTORS
    public AssetIssuerActorConnectionPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartPluginException {
        try {
            createDatabase();
            dao = new AssetIssuerDAO(pluginId, pluginDatabaseSystem, pluginFileSystem);
            agent = new AssetIssuerMonitorAgent(errorManager, dao);
            agent.start();
            connectionManager = new AssetIssuerConnectionManager(assetIssuerActorNetworkServiceManager, dao, broadcaster, eventManager, assetVaultManager, cryptoAddressBookManager);
            recorderService = new AssetIssuerRecorderService(eventManager, pluginId, dao, connectionManager);
            recorderService.start();
            super.start();
        } catch (Exception e) {
            throw new CantStartPluginException(FermatException.wrapException(e));
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    /**
     * This method will store a external {@link ActorAssetIssuer} or update a previous one in case
     * its already registered.
     *
     * @param issuer the issuer to store.
     * @throws CantCreateActorAssetIssuerException
     */
    @Override
    public void saveNewActorAssetIssuer(ActorAssetIssuer issuer) throws CantCreateActorAssetIssuerException {
        String context = "Issuer: " + issuer.toString();
        try {
            dao.saveNewActor(issuer);
        } catch (CantCreateNewDeveloperException | CantInsertRecordException | CantCreateFileException | CantPersistFileException | CantUpdateRecordException | RecordsNotFoundException | CantLoadTableToMemoryException e) {
            throw new CantCreateActorAssetIssuerException(null, e, context, null);
        }
    }

    /**
     * Will search on database the last connection state for the requested issuer public key, or throw
     * an exception in case its not stored.
     *
     * @param issuerPk {@link String} that represents the public key for the issuer to search
     * @return {@link DAPConnectionState} with the actual connection state for this issuer.
     * @throws CantGetAssetIssuerActorsException
     */
    @Override
    public DAPConnectionState getConnectionState(String issuerPk) throws CantGetAssetIssuerActorsException {
        String context = "Issuer PK: " + issuerPk;
        try {
            return dao.getConnectionState(issuerPk);
        } catch (CantLoadTableToMemoryException | InvalidParameterException e) {
            throw new CantGetAssetIssuerActorsException(null, e, issuerPk, null);
        }
    }

    @Override
    public ActorAssetIssuer getByPublicKey(String issuerPk) {
        return dao.getIssuerByPublicKey(issuerPk);
    }

    @Override
    public List<ActorAssetIssuer> getAllIssuers() {
        return dao.getAllIssuers();
    }

    /**
     * @return {@link List} instance filled with all the {@link ActorAssetIssuer} with Connected State ({@link DAPConnectionState#CONNECTED},
     * {@link DAPConnectionState#CONNECTED_ONLINE}, {@link DAPConnectionState#CONNECTED_OFFLINE}
     */
    @Override
    public List<ActorAssetIssuer> getAllIssuerConnected() {
        List<ActorAssetIssuer> toReturn = new ArrayList<>();
        toReturn.addAll(dao.getIssuersByConnectionState(DAPConnectionState.CONNECTED));
        toReturn.addAll(dao.getIssuersByConnectionState(DAPConnectionState.CONNECTED_OFFLINE));
        return toReturn;
    }

    /**
     * @return {@link List} instance filled with all the {@link ActorAssetIssuer} with Connected State ({@link DAPConnectionState#CONNECTED},
     * {@link DAPConnectionState#CONNECTED_ONLINE}, {@link DAPConnectionState#CONNECTED_OFFLINE} AND a redeem point extended public key associated.
     */
    @Override
    public List<ActorAssetIssuer> getAllIssuerConnectedWithExtendedPk() {
        return dao.getIssuersWithExtendedPublicKey();
    }

    @Override
    public void updateOfflineIssuers(List<ActorAssetIssuer> issuersInNs) throws CantUpdateActorAssetIssuerException {
        try {
            connectionManager.updateOfflineIssuer(issuersInNs);
        } catch (CantLoadFileException | CantCreateFileException | CantLoadTableToMemoryException | InvalidParameterException | FileNotFoundException e) {
            throw new CantUpdateActorAssetIssuerException(null, e, null, null);
        }
    }

    @Override
    public void receivingRequestConnection(String issuerName, String issuerPk, byte[] profileImage) throws CantReceiveRequestException {
        String context = "Name: " + issuerName + " - Pk: " + issuerName;
        try {
            connectionManager.receivingActorAssetIssuerRequestConnection(issuerName, issuerPk, profileImage);
        } catch (Exception e) {
            throw new CantReceiveRequestException(null, e, context, null);
        }
    }

    @Override
    public void updateExtendedPk(String extendedPk, String issuerPk) throws CantUpdateActorAssetIssuerException {
        String context = "Extended PK: " + extendedPk + " - Issuer PK: " + issuerPk;
        try {
            dao.updateExtendedPublicKey(issuerPk, extendedPk);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException e) {
            throw new CantUpdateActorAssetIssuerException(null, e, context, null);
        }
    }

    @Override
    public void updateConnectionState(String issuerPk, DAPConnectionState state) throws CantUpdateActorAssetIssuerException {
        String context = "Connection State: " + state.getCode() + " - Issuer PK: " + issuerPk;
        try {
            dao.updateConnectionState(issuerPk, state);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException e) {
            throw new CantUpdateActorAssetIssuerException(null, e, context, null);
        }
    }

    @Override
    public void askConnection(String issuerPk) throws CantAskConnectionActorAssetException {
        try {
            connectionManager.askActorAssetIssuerForConnection(issuerPk);
        } catch (CantRequestAlreadySendActorAssetException | CantLoadTableToMemoryException | CantUpdateRecordException e) {
            throw new CantAskConnectionActorAssetException(null, e, issuerPk, null);
        }
    }

    @Override
    public void acceptConnection(String issuerPk) throws CantAcceptConnectionActorAssetException {
        try {
            connectionManager.acceptActorAssetIssuer(issuerPk);
        } catch (CantAcceptActorAssetUserException | CantUpdateRecordException | CantLoadTableToMemoryException e) {
            throw new CantAcceptConnectionActorAssetException(null, e, issuerPk, null);
        }
    }

    @Override
    public void denyConnection(String issuerPk) throws CantDenyConnectionActorAssetException {
        try {
            connectionManager.denyConnectionActorAssetIssuer(issuerPk);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException e) {
            throw new CantDenyConnectionActorAssetException(null, e, issuerPk, null);
        }
    }

    @Override
    public void cancelConnection(String issuerPk) throws CantCancelConnectionActorAssetException {
        try {
            connectionManager.cancelActorAssetIssuer(issuerPk);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException e) {
            throw new CantCancelConnectionActorAssetException(null, e, issuerPk, null);
        }
    }


    @Override
    public List<DAPActor> getWaitingYourConnectionActorAssetIssuer(int max, int offset) throws CantGetActorAssetWaitingException {
        try {
            return dao.getAllWaitingActorAssetIssuer(DAPConnectionState.PENDING_LOCALLY, max, offset);
        } catch (CantLoadTableToMemoryException | FileNotFoundException | CantLoadFileException | InvalidParameterException | CantCreateFileException e) {
            throw new CantGetActorAssetWaitingException(null, e, null, null);
        }
    }

    @Override
    public List<DAPActor> getWaitingTheirConnectionActorAssetIssuer(int max, int offset) throws CantGetActorAssetWaitingException {
        try {
            return dao.getAllWaitingActorAssetIssuer(DAPConnectionState.PENDING_REMOTELY, max, offset);
        } catch (CantLoadTableToMemoryException | FileNotFoundException | CantLoadFileException | InvalidParameterException | CantCreateFileException e) {
            throw new CantGetActorAssetWaitingException(null, e, null, null);
        }
    }

    @Override
    public ActorAssetIssuer getLastNotification(String issuerPk) throws CantGetActorAssetNotificationException {
        try {
            return dao.getLastNotification(issuerPk);
        } catch (Exception e) {
            throw new CantGetActorAssetNotificationException("CAN'T GET ACTOR ASSET ISSUER LAST NOTIFICATION", e, issuerPk, "Error on ACTOR ISSUER MANAGER");
        }
    }

    //PRIVATE METHODS
    private void createDatabase() throws CantCreateDatabaseException {
        AssetIssuerDatabaseFactory databaseFactory = new AssetIssuerDatabaseFactory(pluginDatabaseSystem, pluginId);
        if (!databaseFactory.databaseExists()) {
            databaseFactory.createDatabase();
        }
    }

    //GETTER AND SETTERS
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return AssetIssuerDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory, pluginId);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return AssetIssuerDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, AssetIssuerDatabaseConstants.ASSET_ISSUER_ACTOR_CONNECTION_DATABASE);
            return AssetIssuerDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, database, developerDatabaseTable);
        } catch (CantOpenDatabaseException cantOpenDatabaseException) {
            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
            FermatException e = new CantDeliverDatabaseException("Cannot open the database", cantOpenDatabaseException, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.ASSET_BUYER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        } catch (DatabaseNotFoundException databaseNotFoundException) {
            FermatException e = new CantDeliverDatabaseException("Database does not exists", databaseNotFoundException, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.ASSET_BUYER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        } catch (Exception exception) {
            FermatException e = new CantDeliverDatabaseException("Unexpected Exception", exception, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.ASSET_BUYER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        // If we are here the database could not be opened, so we return an empty list
        return Collections.EMPTY_LIST;
    }


    //INNER CLASSES
}
