package org.fermat.fermat_dap_core.layer.digital_asset_transaction.multi_transaction_asset_issuing;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.multi_transaction_asset_issuing.developer.DeveloperBitDubai;

public class MultiTransactionAssetIssuingPluginSubsystem extends AbstractPluginSubsystem {

    //VARIABLE DECLARATION

    //CONSTRUCTORS
    public MultiTransactionAssetIssuingPluginSubsystem() {
        super(new PluginReference(Plugins.MULTI_TRANSACTION_ASSET_ISSUING));
    }

    //PUBLIC METHODS
    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
            System.out.println("LFTL: DAT MULTI TRANSACTION ASSET ISSUING");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}