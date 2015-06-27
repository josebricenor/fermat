package com.bitdubai.fermat_osa_addon.layer.android.logger.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogLevel;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_osa_addon.layer.android.logger.developer.bitdubai.version_1.structure.LoggerManager;

import java.util.UUID;

/**
 * Created by rodrigo on 2015.06.25..
 */
public class LoggerAddonRoot implements Addon, Service{

    /**
     * Default logging level is minimal
     */
    LogLevel logLevel = LogLevel.MINIMAL_LOGGING;
    LoggerManager loggerManager;
    UUID pluginId;
    ServiceStatus serviceStatus = ServiceStatus.STOPPED;



    @Override
    public void start() throws CantStartPluginException {
        /**
         * I initialize the logger Manager
         */
        loggerManager = new LoggerManager(logLevel);
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }
}
