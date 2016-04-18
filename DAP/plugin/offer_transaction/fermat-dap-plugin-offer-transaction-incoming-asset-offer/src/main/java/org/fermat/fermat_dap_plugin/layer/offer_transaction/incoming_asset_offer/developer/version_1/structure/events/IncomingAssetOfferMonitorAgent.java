package org.fermat.fermat_dap_plugin.layer.offer_transaction.incoming_asset_offer.developer.version_1.structure.events;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.fermat.fermat_dap_plugin.layer.offer_transaction.incoming_asset_offer.developer.version_1.structure.database.IncomingAssetOfferDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class IncomingAssetOfferMonitorAgent extends FermatAgent {

    //VARIABLE DECLARATION
    private BuyerAgent buyerAgent;

    private final ErrorManager errorManager;
    private final IncomingAssetOfferDAO dao;
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    //CONSTRUCTORS

    public IncomingAssetOfferMonitorAgent(ErrorManager errorManager, IncomingAssetOfferDAO dao) {
        this.errorManager = errorManager;
        this.dao = dao;
    }

    //PUBLIC METHODS

    @Override
    public void start() throws CantStartAgentException {
        try {
            buyerAgent = new BuyerAgent();
            service.submit(buyerAgent);
            super.start();
        } catch (Exception e) {
            throw new CantStartAgentException(FermatException.wrapException(e), null, null);
        }
    }

    @Override
    public void stop() throws CantStopAgentException {
        try {
            service.shutdown();
            super.stop();
        } catch (Exception e) {
            throw new CantStopAgentException(FermatException.wrapException(e), null, null);
        }
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES

    private class BuyerAgent implements Runnable {

        public BuyerAgent() {
        }


        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                doTheMainTask();
            } catch (Exception e) {
                errorManager.reportUnexpectedPluginException(Plugins.INCOMING_ASSET_OFFER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            }
        }

        private void doTheMainTask() {
        }

    }
}
