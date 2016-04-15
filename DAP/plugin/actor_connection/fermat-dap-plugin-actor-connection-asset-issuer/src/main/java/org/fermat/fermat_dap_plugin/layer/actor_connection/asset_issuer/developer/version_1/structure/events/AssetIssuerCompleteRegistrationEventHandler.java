package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;

import org.fermat.fermat_dap_api.layer.all_definition.events.ActorAssetIssuerCompleteRegistrationNotificationEvent;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.internal.AssetIssuerConnectionManager;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 14/04/16.
 */
public final class AssetIssuerCompleteRegistrationEventHandler implements FermatEventHandler {

    //VARIABLE DECLARATION
    private AssetIssuerConnectionManager connectionManager;

    //CONSTRUCTORS
    public AssetIssuerCompleteRegistrationEventHandler(AssetIssuerConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    //PUBLIC METHODS
    @Override
    public void handleEvent(FermatEvent platformEvent) throws FermatException {
        ActorAssetIssuerCompleteRegistrationNotificationEvent completeActorAssetIssuerRegistrationNotificationEvent = (ActorAssetIssuerCompleteRegistrationNotificationEvent) platformEvent;
        connectionManager.handleCompleteRegistrationEvent(completeActorAssetIssuerRegistrationNotificationEvent.getActorAssetIssuer());
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
