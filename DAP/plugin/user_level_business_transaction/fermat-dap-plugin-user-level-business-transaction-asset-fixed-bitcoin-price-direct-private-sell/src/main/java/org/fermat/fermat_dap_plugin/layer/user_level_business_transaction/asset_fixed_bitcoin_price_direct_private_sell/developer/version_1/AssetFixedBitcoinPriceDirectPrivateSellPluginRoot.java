package org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
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
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantDeliverDatabaseException;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.developer_utils.AssetFixedBitcoinPriceDirectPrivateSellDeveloperDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.structure.database.AssetFixedBitcoinPriceDirectPrivateSellDAO;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.structure.database.AssetFixedBitcoinPriceDirectPrivateSellDatabaseConstants;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.structure.database.AssetFixedBitcoinPriceDirectPrivateSellDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.structure.events.AssetFixedBitcoinPriceDirectPrivateSellMonitorAgent;
import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.version_1.structure.events.AssetFixedBitcoinPriceDirectPrivateSellRecorderService;

import java.util.Collections;
import java.util.List;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetFixedBitcoinPriceDirectPrivateSellPluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers {


    //VARIABLE DECLARATION
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    private AssetFixedBitcoinPriceDirectPrivateSellDAO dao;
    private AssetFixedBitcoinPriceDirectPrivateSellMonitorAgent agent;
    private AssetFixedBitcoinPriceDirectPrivateSellRecorderService recorderService;

    //CONSTRUCTORS
    public AssetFixedBitcoinPriceDirectPrivateSellPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartPluginException {
        try {
            createDatabase();
            dao = new AssetFixedBitcoinPriceDirectPrivateSellDAO(pluginId, pluginDatabaseSystem);
            agent = new AssetFixedBitcoinPriceDirectPrivateSellMonitorAgent(errorManager, dao);
            recorderService = new AssetFixedBitcoinPriceDirectPrivateSellRecorderService(eventManager, pluginId, dao);
            super.start();
        } catch (Exception e) {
            throw new CantStartPluginException(FermatException.wrapException(e));
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    //PRIVATE METHODS
    private void createDatabase() throws CantCreateDatabaseException {
        AssetFixedBitcoinPriceDirectPrivateSellDatabaseFactory databaseFactory = new AssetFixedBitcoinPriceDirectPrivateSellDatabaseFactory(pluginDatabaseSystem, pluginId);
        if (!databaseFactory.databaseExists()) {
            databaseFactory.createDatabase();
        }
    }

    //GETTER AND SETTERS
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return AssetFixedBitcoinPriceDirectPrivateSellDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory, pluginId);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return AssetFixedBitcoinPriceDirectPrivateSellDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, AssetFixedBitcoinPriceDirectPrivateSellDatabaseConstants.ASSET_FIXED_BITCOIN_PRICE_DIRECT_PRIVATE_SELL_DATABASE);
            return AssetFixedBitcoinPriceDirectPrivateSellDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, database, developerDatabaseTable);
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
