package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.util.Validate;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantStartServiceException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.interfaces.AssetTransactionService;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database.AssetIssuerDAO;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.internal.AssetIssuerConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public final class AssetIssuerRecorderService implements AssetTransactionService {

    //VARIABLE DECLARATION
    private ServiceStatus serviceStatus;

    {
        serviceStatus = ServiceStatus.CREATED;
    }

    private final EventManager eventManager;
    private final UUID pluginId;
    private final AssetIssuerDAO dao;
    private final AssetIssuerConnectionManager connectionManager;
    private List<FermatEventListener> listenersAdded;

    {
        listenersAdded = new ArrayList<>();
    }

    //CONSTRUCTORS
    public AssetIssuerRecorderService(EventManager eventManager, UUID pluginId, AssetIssuerDAO dao, AssetIssuerConnectionManager connectionManager) {
        this.eventManager = eventManager;
        this.pluginId = pluginId;
        this.dao = dao;
        this.connectionManager = connectionManager;
    }


    //PUBLIC METHODS

    @Override
    public void start() throws CantStartServiceException {
        String context = "Plugin ID: " + pluginId + " Event Manager: " + eventManager;
        try {
            FermatEventListener fermatEventListener;
            fermatEventListener = eventManager.getNewListener(EventType.COMPLETE_ASSET_ISSUER_REGISTRATION_NOTIFICATION);
            fermatEventListener.setEventHandler(new AssetIssuerCompleteRegistrationEventHandler(connectionManager));
            eventManager.addListener(fermatEventListener);
            listenersAdded.add(fermatEventListener);

            fermatEventListener = eventManager.getNewListener(EventType.ACTOR_ASSET_NETWORK_SERVICE_NEW_NOTIFICATIONS);
            fermatEventListener.setEventHandler(new AssetIssuerNewNotificationsEventHandler(connectionManager));
            eventManager.addListener(fermatEventListener);
            listenersAdded.add(fermatEventListener);

            fermatEventListener = eventManager.getNewListener(EventType.ACTOR_ASSET_REQUEST_CONNECTIONS);
            fermatEventListener.setEventHandler(new AssetIssuerRequestConnectionEventHandler(connectionManager));
            eventManager.addListener(fermatEventListener);
            listenersAdded.add(fermatEventListener);
        } catch (Exception e) {
            throw new CantStartServiceException(e, context, "An unexpected exception happened while trying to start the AssetAppropriationRecordeService.");
        }
        serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        removeRegisteredListeners();
        serviceStatus = ServiceStatus.STOPPED;
    }


    //PRIVATE METHODS

    private void addListener(FermatEventListener listener) {
        eventManager.addListener(listener);
        listenersAdded.add(listener);
    }


    private void removeRegisteredListeners() {
        for (FermatEventListener fermatEventListener : listenersAdded) {
            eventManager.removeListener(fermatEventListener);
        }
        listenersAdded.clear();
    }
    //GETTER AND SETTERS

    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }

    public List<FermatEventListener> getListenersAdded() {
        return Validate.verifyGetter(listenersAdded);
    }
    //INNER CLASSES
}
