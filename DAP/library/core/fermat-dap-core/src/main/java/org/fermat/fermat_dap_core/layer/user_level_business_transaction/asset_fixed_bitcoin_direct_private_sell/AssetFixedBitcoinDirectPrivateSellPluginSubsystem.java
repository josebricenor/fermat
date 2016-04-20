package org.fermat.fermat_dap_core.layer.user_level_business_transaction.asset_fixed_bitcoin_direct_private_sell;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

import org.fermat.fermat_dap_plugin.layer.user_level_business_transaction.asset_fixed_bitcoin_price_direct_private_sell.developer.DeveloperBitDubai;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 12/04/16.
 */
public class AssetFixedBitcoinDirectPrivateSellPluginSubsystem extends AbstractPluginSubsystem {

    //VARIABLE DECLARATION

    //CONSTRUCTORS
    public AssetFixedBitcoinDirectPrivateSellPluginSubsystem() {
        super(new PluginReference(Plugins.ASSET_FIXED_BITCOIN_DIRECT_PRIVATE_SELL));
    }

    //PUBLIC METHODS
    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
            System.out.println("LFTL: ULBT ASSET FIXED BITCOIN DIRECT PRIVATE SELL");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
