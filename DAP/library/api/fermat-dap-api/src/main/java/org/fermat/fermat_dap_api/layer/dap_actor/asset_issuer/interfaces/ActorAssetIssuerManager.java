package org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;

/**
 * Created by Nerio on 07/09/15.
 */
public interface ActorAssetIssuerManager extends FermatManager {

    /**
     * The method <code>getActorRegisteredByPublicKey</code> shows the information associated with the actorPublicKey
     *
     * @param actorPublicKey                     The public key of the Asset Actor Issuer
     * @return                                   THe information associated with the actorPublicKey.
     * @throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException
     * @throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantAssetIssuerActorNotFoundException
     */
    ActorAssetIssuer getActorByPublicKey(String actorPublicKey) throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException, org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantAssetIssuerActorNotFoundException;

    /**
     * The method <code>createActorAssetIssuerFactory</code> create Actor by a Identity
     *
     * @param assetIssuerActorPublicKey                 Referred to the Identity publicKey
     * @param assetIssuerActorName                      Referred to the Identity Alias
     * @param assetIssuerImage              Referred to the Identity profileImage
     * @throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException
     */
    void createActorAssetIssuerFactory(String assetIssuerActorPublicKey, String assetIssuerActorName, byte[] assetIssuerImage) throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;

    /**
     * The method <code>registerActorInActorNetworkService</code> Register Actor in Actor Network Service
     */
    void registerActorInActorNetworkService() throws org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRegisterActorAssetIssuerException;

    ActorAssetIssuer getActorAssetIssuer() throws org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;

}
