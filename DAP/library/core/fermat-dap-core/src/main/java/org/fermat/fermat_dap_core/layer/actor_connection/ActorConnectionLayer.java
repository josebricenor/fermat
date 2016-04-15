package org.fermat.fermat_dap_core.layer.actor_connection;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

import org.fermat.fermat_dap_core.layer.actor_connection.asset_issuer.AssetIssuerPluginSubsystem;
import org.fermat.fermat_dap_core.layer.actor_connection.asset_user.AssetUserPluginSubsystem;
import org.fermat.fermat_dap_core.layer.actor_connection.redeem_point.RedeemPointPluginSubsystem;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 12/04/16.
 */
public class ActorConnectionLayer extends AbstractLayer {
    //VARIABLE DECLARATION

    //CONSTRUCTORS
    public ActorConnectionLayer() {
        super(Layers.ACTOR_CONNECTION);
    }

    //PUBLIC METHODS
    @Override
    public void start() throws CantStartLayerException {
        try {

            registerPlugin(new AssetIssuerPluginSubsystem());
            registerPlugin(new AssetUserPluginSubsystem());
            registerPlugin(new RedeemPointPluginSubsystem());

        } catch (Exception e) {

            throw new CantStartLayerException(
                    e,
                    "Crypto Transaction Layer of DAP Platform",
                    "Problem trying to register a plugin."
            );
        }
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
