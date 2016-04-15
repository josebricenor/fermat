package org.fermat.fermat_dap_api.layer.actor_connection.asset_issuer.interfaces;


import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantReceiveRequestException;

import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantUpdateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetWaitingException;

import java.util.List;

/**
 * Created by rodrigo on 4/10/16.
 */
public interface AssetIssuerActorConnectionManager extends FermatManager {

    /**
     * This method will store a external {@link ActorAssetIssuer} or update a previous one in case
     * its already registered.
     *
     * @param issuer the issuer to store.
     * @throws CantCreateActorAssetIssuerException
     */
    void saveNewActorAssetIssuer(ActorAssetIssuer issuer) throws CantCreateActorAssetIssuerException;

    /**
     * Will search on database the last connection state for the requested issuer public key, or throw
     * an exception in case its not stored.
     *
     * @param issuerPk {@link String} that represents the public key for the issuer to search
     * @return {@link DAPConnectionState} with the actual connection state for this issuer.
     * @throws CantGetAssetIssuerActorsException
     */
    DAPConnectionState getConnectionState(String issuerPk) throws CantGetAssetIssuerActorsException;

    ActorAssetIssuer getByPublicKey(String issuerPk);

    /**
     * @return {@link List} instance filled with all the {@link ActorAssetIssuer} with Connected State ({@link DAPConnectionState#CONNECTED},
     * {@link DAPConnectionState#CONNECTED_ONLINE}, {@link DAPConnectionState#CONNECTED_OFFLINE}
     */
    List<ActorAssetIssuer> getAllIssuers();

    /**
     * @return {@link List} instance filled with all the {@link ActorAssetIssuer} with Connected State ({@link DAPConnectionState#CONNECTED},
     * {@link DAPConnectionState#CONNECTED_ONLINE}, {@link DAPConnectionState#CONNECTED_OFFLINE}
     */
    List<ActorAssetIssuer> getAllIssuerConnected();

    /**
     * @return {@link List} instance filled with all the {@link ActorAssetIssuer} with Connected State ({@link DAPConnectionState#CONNECTED},
     * {@link DAPConnectionState#CONNECTED_ONLINE}, {@link DAPConnectionState#CONNECTED_OFFLINE} AND a redeem point extended public key associated.
     */
    List<ActorAssetIssuer> getAllIssuerConnectedWithExtendedPk();

    void updateOfflineIssuers(List<ActorAssetIssuer> issuersInNs) throws CantUpdateActorAssetIssuerException;

    void receivingRequestConnection(String issuerName, String issuerPk, byte[] profileImage) throws CantReceiveRequestException;

    void updateExtendedPk(String extendedPk, String issuerPk) throws CantUpdateActorAssetIssuerException;

    void updateConnectionState(String issuerPk, DAPConnectionState state) throws CantUpdateActorAssetIssuerException;

    void askConnection(String issuerPk) throws CantAskConnectionActorAssetException;

    void acceptConnection(String issuerPk) throws CantAcceptConnectionActorAssetException;

    void denyConnection(String issuerPk) throws CantDenyConnectionActorAssetException;

    void cancelConnection(String issuerPk) throws CantCancelConnectionActorAssetException;

    /**
     * The method {@code getWaitingYourConnectionActorAssetIssuer} shows the list of all intra users
     * that sent a connection request and are waiting for the acceptance of the logged in one.
     *
     * @return
     * @throws org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetWaitingException
     */
    List<DAPActor> getWaitingYourConnectionActorAssetIssuer(int max, int offset) throws CantGetActorAssetWaitingException;

    /**
     * The method {@code getWaitingTheirConnectionActorAssetIssuer} shows the list of all actor asset users
     * that the logged in one has sent connections request to and have not been answered yet..
     *
     * @return
     * @throws org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetWaitingException
     */
    List<DAPActor> getWaitingTheirConnectionActorAssetIssuer(int max, int offset) throws CantGetActorAssetWaitingException;

    ActorAssetIssuer getLastNotification(String issuerPk) throws CantGetActorAssetNotificationException;

}
