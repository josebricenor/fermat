package org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1;

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
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.watch_only_vault.interfaces.WatchOnlyVaultManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantAssetIssuerActorNotFoundException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRegisterActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces.AssetRedeemPointActorNetworkServiceManager;
import org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.exceptions.CantAddPendingAssetIssuerException;
import org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.structure.AssetIssuerActorDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Nerio on 09/09/15.
 */
public class AssetIssuerActorPluginRoot extends AbstractPlugin implements
        ActorAssetIssuerManager,
        DatabaseManagerForDevelopers {

    public AssetIssuerActorPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

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

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.ASSET_ISSUER)
    private AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.REDEEM_POINT)
    private AssetRedeemPointActorNetworkServiceManager assetRedeemPointActorNetworkServiceManager;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS, layer = Layers.CRYPTO_VAULT, plugin = Plugins.BITCOIN_ASSET_VAULT)
    private AssetVaultManager assetVaultManager;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS, layer = Layers.CRYPTO_VAULT, plugin = Plugins.BITCOIN_WATCH_ONLY_VAULT)
    private WatchOnlyVaultManager watchOnlyVaultManager;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS, layer = Layers.CRYPTO_MODULE, plugin = Plugins.CRYPTO_ADDRESS_BOOK)
    private CryptoAddressBookManager cryptoAddressBookManager;

    private AssetIssuerActorDao assetIssuerActorDao;

    private ActorAssetIssuer actorAssetIssuer;

    List<FermatEventListener> listenersAdded = new ArrayList<>();

    @Override
    public void start() throws CantStartPluginException {
        try {
            /**
             * Created instance of AssetUserActorDao and initialize Database
             */
            assetIssuerActorDao = new AssetIssuerActorDao(this.pluginDatabaseSystem, this.pluginFileSystem, this.pluginId);

            /**
             * Will load (if any) the local asset issuer
             */
            actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();


            /**
             * Agent for Search Actor Asset User REGISTERED in Actor Network Service User
             */
//            startMonitorAgent();

            this.serviceStatus = ServiceStatus.STARTED;

            /**
             * Test to comment when not needed.
             */
            //testGenerateAndInitializeWatchOnlyVault();

        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
            throw new CantStartPluginException(e, Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR);
        }
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ActorAssetIssuer getActorByPublicKey(String actorPublicKey) throws CantGetAssetIssuerActorsException,
            CantAssetIssuerActorNotFoundException {
        try {
            ActorAssetIssuer currentIssuer = getActorAssetIssuer();
            if (currentIssuer != null && currentIssuer.getActorPublicKey().equals(actorPublicKey)) {
                return currentIssuer;
            } else {
                return assetIssuerActorDao.getActorByPublicKey(actorPublicKey);
            }
        } catch (CantGetAssetIssuerActorsException e) {
            throw new CantGetAssetIssuerActorsException("", FermatException.wrapException(e), "Cant Get Actor Asset Issuer from Data Base", null);
        }
    }

    @Override
    public void createActorAssetIssuerFactory(String assetIssuerActorPublicKey, String assetIssuerActorName, byte[] assetIssuerActorProfileImage) throws CantCreateActorAssetIssuerException {
        try {
            ActorAssetIssuer actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();

            if (actorAssetIssuer == null) {

                Double locationLatitude = new Random().nextDouble();
                Double locationLongitude = new Random().nextDouble();
                String description = "Asset Issuer Skynet Test";

                AssetIssuerActorRecord record = new AssetIssuerActorRecord(
                        assetIssuerActorPublicKey,
                        assetIssuerActorName,
                        DAPConnectionState.REGISTERED_LOCALLY,
                        locationLatitude,
                        locationLongitude,
                        System.currentTimeMillis(),
                        System.currentTimeMillis(),
                        Actors.DAP_ASSET_ISSUER,
                        description,
                        "-",
                        assetIssuerActorProfileImage);

                assetIssuerActorDao.createNewAssetIssuer(record);

                actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();

                assetIssuerActorNetworkServiceManager.registerActorAssetIssuer(actorAssetIssuer);
            } else {

                actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();

                Double locationLatitude = new Random().nextDouble();
                Double locationLongitude = new Random().nextDouble();
                String description = "Asset Issuer Skynet Test";

                AssetIssuerActorRecord record = new AssetIssuerActorRecord(
                        actorAssetIssuer.getActorPublicKey(),
                        assetIssuerActorName,
                        actorAssetIssuer.getDapConnectionState(),
                        locationLatitude,
                        locationLongitude,
                        actorAssetIssuer.getRegistrationDate(),
                        System.currentTimeMillis(),
                        actorAssetIssuer.getType(),
                        description,
                        actorAssetIssuer.getExtendedPublicKey(),
                        assetIssuerActorProfileImage);

                assetIssuerActorDao.updateAssetIssuer(record);

                actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();

                assetIssuerActorNetworkServiceManager.updateActorAssetIssuer(actorAssetIssuer);
            }

            if (actorAssetIssuer != null) {
                System.out.println("*********************Actor Asset Issuer************************");
                System.out.println("Actor Asset PublicKey: " + actorAssetIssuer.getActorPublicKey());
                System.out.println("Actor Asset Name: " + actorAssetIssuer.getName());
                System.out.println("Actor Asset Description: " + actorAssetIssuer.getDescription());
                System.out.println("***************************************************************");
            }
        } catch (CantAddPendingAssetIssuerException e) {
            throw new CantCreateActorAssetIssuerException("CAN'T ADD NEW ACTOR ASSET ISSUER", e, "", "");
        } catch (CantGetAssetIssuerActorsException e) {
            throw new CantCreateActorAssetIssuerException("CAN'T GET ACTOR ASSET ISSUER", e, "", "");
        } catch (Exception e) {
            throw new CantCreateActorAssetIssuerException("CAN'T ADD NEW ACTOR ASSET ISSUER unknow Cause", FermatException.wrapException(e), "", "");
        }
    }

    @Override
    public ActorAssetIssuer getActorAssetIssuer() throws CantGetAssetIssuerActorsException {

        ActorAssetIssuer actorAssetIssuer;
        try {
            actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();
        } catch (Exception e) {
            throw new CantGetAssetIssuerActorsException("", FermatException.wrapException(e), "There is a problem I can't identify.", null);
        }

        return actorAssetIssuer;
    }

    public void registerActorInActorNetworkService() throws CantRegisterActorAssetIssuerException {
        try {
            /*
             * Send the Actor Asset Issuer Local for Register in Actor Network Service
             */
            ActorAssetIssuer actorAssetIssuer = this.assetIssuerActorDao.getActorAssetIssuer();

            if (actorAssetIssuer != null)
                assetIssuerActorNetworkServiceManager.registerActorAssetIssuer(actorAssetIssuer);

        } catch (CantRegisterActorAssetIssuerException e) {
            throw new CantRegisterActorAssetIssuerException("CAN'T Register Actor Asset Issuer in Actor Network Service", e, "", "");
        } catch (CantGetAssetIssuerActorsException e) {
            throw new CantRegisterActorAssetIssuerException("CAN'T GET ACTOR ASSET ISSUER", e, "", "");
        }
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory dbFactory = new org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory(this.pluginDatabaseSystem, this.pluginId);
        return dbFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory dbFactory = new org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory(this.pluginDatabaseSystem, this.pluginId);
        return dbFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        try {
            org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory dbFactory = new org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.developerUtils.AssetIssuerActorDeveloperDatabaseFactory(this.pluginDatabaseSystem, this.pluginId);
            dbFactory.initializeDatabase();
            return dbFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
        } catch (org.fermat.fermat_dap_plugin.layer.actor.asset.issuer.developer.version_1.exceptions.CantInitializeAssetIssuerActorDatabaseException e) {
            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_USER_ACTOR, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        } catch (Exception e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_USER_ACTOR, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        // If we are here the database could not be opened, so we return an empry list
        return Collections.EMPTY_LIST;
    }
}
