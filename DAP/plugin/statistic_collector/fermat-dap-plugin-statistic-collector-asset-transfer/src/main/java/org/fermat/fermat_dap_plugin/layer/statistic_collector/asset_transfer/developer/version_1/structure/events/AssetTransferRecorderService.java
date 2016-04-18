package org.fermat.fermat_dap_plugin.layer.statistic_collector.asset_transfer.developer.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.util.Validate;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantStartServiceException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.interfaces.AssetTransactionService;
import org.fermat.fermat_dap_plugin.layer.statistic_collector.asset_transfer.developer.version_1.structure.database.AssetTransferDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class AssetTransferRecorderService implements AssetTransactionService {

    //VARIABLE DECLARATION
    private ServiceStatus serviceStatus;

    {
        serviceStatus = ServiceStatus.CREATED;
    }

    private final EventManager eventManager;
    private final UUID pluginId;
    private final AssetTransferDAO dao;
    private List<FermatEventListener> listenersAdded;

    {
        listenersAdded = new ArrayList<>();
    }

    //CONSTRUCTORS

    public AssetTransferRecorderService(EventManager eventManager, UUID pluginId, AssetTransferDAO dao) {
        this.eventManager = eventManager;
        this.pluginId = pluginId;
        this.dao = dao;
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartServiceException {
        String context = "Plugin ID: " + pluginId + " Event Manager: " + eventManager;
        try {
            FermatEventListener fermatEventListener;
            FermatEventHandler fermatEventHandler = new AssetTransferEventHandler(this);
        } catch (Exception e) {
            throw new CantStartServiceException(e, context, "An unexpected exception happened while trying to start the AssetAppropriationRecordeService.");
        }
        serviceStatus = ServiceStatus.STARTED;
    }


    void receiveNewEvent(FermatEvent event) throws CantSaveEventException {
        String context = "pluginId: " + pluginId + " - event: " + event;
        try {
            dao.saveNewEvent(event);
        } catch (Exception e) {
            throw new CantSaveEventException(FermatException.wrapException(e), context, CantSaveEventException.DEFAULT_MESSAGE);
        }
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
