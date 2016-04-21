package org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1;

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
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.all_definition.enums.MetadataTransactionStatus;
import org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataConfirmSentEvent;
import org.fermat.fermat_dap_api.layer.all_definition.events.DigitalAssetMetadataSuccessfullySentEvent;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.MetadataTransactionRecord;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.asset_outgoing.interfaces.AssetOutgoingMetadataTransactionManager;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions.CantStartMetadataTransactionException;
import org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions.CantUpdateMetadataTransactionException;
import org.fermat.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.AssetTransmissionNetworkServiceManager;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantDeliverDatabaseException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDatabaseConstants;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.events.OutgoingAssetMetadataRecorderService;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.developer_utils.OutgoingAssetMetadataDeveloperDatabaseFactory;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.database.OutgoingAssetMetadataDAO;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.events.OutgoingAssetMetadataMonitorAgent;
import org.fermat.fermat_dap_plugin.layer.metadata_transaction.outgoing_asset_metadata.developer.version_1.structure.internal.MetadataTransactionRecordImpl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jose Briceño (josebricenor@gmail.com) on 19/04/16.
 */
public class OutgoingAssetMetadataPluginRoot extends AbstractPlugin implements
        AssetOutgoingMetadataTransactionManager,
        DatabaseManagerForDevelopers {

    //VARIABLE DECLARATION
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.NETWORK_SERVICE, plugin = Plugins.ASSET_TRANSMISSION)
    AssetTransmissionNetworkServiceManager assetTransmissionNetworkServiceManager;

    private OutgoingAssetMetadataDAO dao;
    private OutgoingAssetMetadataMonitorAgent agent;
    private OutgoingAssetMetadataRecorderService recorderService;

    //CONSTRUCTORS
    public OutgoingAssetMetadataPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartPluginException {
        try {
            createDatabase();
            dao = new OutgoingAssetMetadataDAO(pluginId, pluginDatabaseSystem);
            agent = new OutgoingAssetMetadataMonitorAgent(errorManager, dao, assetTransmissionNetworkServiceManager,eventManager);
            recorderService = new OutgoingAssetMetadataRecorderService(eventManager, pluginId, dao, this);
            super.start();
        } catch (Exception e) {
            throw new CantStartPluginException(FermatException.wrapException(e));
        }
    }

    @Override
    public MetadataTransactionRecord sendMetadata(DAPActor actorFrom, DAPActor actorTo, DigitalAssetMetadata metadataToSend) throws CantStartMetadataTransactionException {

        MetadataTransactionRecord metadataTransactionRecord = null;

        try {
             metadataTransactionRecord = dao.saveNewMetadataTransactionRecord(actorFrom, actorTo, metadataToSend);
        } catch (CantSaveEventException e) {
            e.printStackTrace();
        }

        return metadataTransactionRecord;
    }

    @Override
    public void notifyReception(MetadataTransactionRecord record) throws CantUpdateMetadataTransactionException {
        dao.confirmMetadataTransactionRecord(record);
    }

    @Override
    public void cancelTransaction(MetadataTransactionRecord record) throws CantUpdateMetadataTransactionException {
        dao.cancelMetadataTransactionRecord(record);
    }

    @Override
    public MetadataTransactionRecord getRecord(UUID recordId) {
        MetadataTransactionRecordImpl metadataTransactionRecord;
        metadataTransactionRecord = (MetadataTransactionRecordImpl) dao.getMetadataTransactionRecordById(recordId);
        return metadataTransactionRecord;
    }

    @Override
    public MetadataTransactionRecord getLastTransaction(DigitalAssetMetadata assetMetadata) {
        MetadataTransactionRecordImpl metadataTransactionRecord;
        metadataTransactionRecord = (MetadataTransactionRecordImpl) dao.getLastMetadataTransactionRecordByDigitalAssetMetadata(assetMetadata);
        return metadataTransactionRecord;
    }

    @Override
    public void stop() {
        super.stop();
    }

    /**
    * Handle the incoming event, the confirm of the message reception.
    *
    * @param digitalAssetMetadataSuccessfullySentEvent the event to be handle
    *
    * */
    public void handleEvent(DigitalAssetMetadataConfirmSentEvent digitalAssetMetadataSuccessfullySentEvent){
        MetadataTransactionRecord metadataTransactionRecord = dao.getMetadataTransactionRecordById(digitalAssetMetadataSuccessfullySentEvent.getRecord().getRecordId());
        if(metadataTransactionRecord.getStatus() != MetadataTransactionStatus.CANCELLED){
            dao.updateReceiveMetadataTransactionRecord(digitalAssetMetadataSuccessfullySentEvent.getRecord());
        }
    }

    //PRIVATE METHODS
    private void createDatabase() throws CantCreateDatabaseException {
        OutgoingAssetMetadataDatabaseFactory databaseFactory = new OutgoingAssetMetadataDatabaseFactory(pluginDatabaseSystem, pluginId);
        if (!databaseFactory.databaseExists()) {
            databaseFactory.createDatabase();
        }
    }

    //GETTER AND SETTERS
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return OutgoingAssetMetadataDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory, pluginId);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return OutgoingAssetMetadataDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, OutgoingAssetMetadataDatabaseConstants.OUTGOING_ASSET_METADATA_DATABASE);
            return OutgoingAssetMetadataDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, database, developerDatabaseTable);
        } catch (CantOpenDatabaseException cantOpenDatabaseException) {
            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
            FermatException e = new CantDeliverDatabaseException("Cannot open the database", cantOpenDatabaseException, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.OUTGOING_ASSET_METADATA, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        } catch (DatabaseNotFoundException databaseNotFoundException) {
            FermatException e = new CantDeliverDatabaseException("Database does not exists", databaseNotFoundException, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.OUTGOING_ASSET_METADATA, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        } catch (Exception exception) {
            FermatException e = new CantDeliverDatabaseException("Unexpected Exception", exception, "DeveloperDatabase: " + developerDatabase.getName(), "");
            this.errorManager.reportUnexpectedPluginException(Plugins.OUTGOING_ASSET_METADATA, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        // If we are here the database could not be opened, so we return an empty list
        return Collections.EMPTY_LIST;
    }

    //INNER CLASSES
}
