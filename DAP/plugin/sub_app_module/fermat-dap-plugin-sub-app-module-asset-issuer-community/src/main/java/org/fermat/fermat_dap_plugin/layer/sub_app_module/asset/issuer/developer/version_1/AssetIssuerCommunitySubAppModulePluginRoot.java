package org.fermat.fermat_dap_plugin.layer.sub_app_module.asset.issuer.developer.version_1;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededIndirectPluginReferences;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.fermat.fermat_dap_api.layer.actor_connection.asset_issuer.interfaces.AssetIssuerActorConnectionManager;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantAssetIssuerActorNotFoundException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantUpdateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_actor.exceptions.CantConnectToActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.RedeemPointActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.exceptions.CantConnectToActorAssetRedeemPointException;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.exceptions.CantGetAssetRedeemPointActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPointManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRequestListActorAssetIssuerRegisteredException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptActorAssetUserException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetWaitingException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantRequestAlreadySendActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantGetAssetIssuerIdentitiesException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.AssetIssuerSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO explain here the main functionality of the plug-in.
 * <p/>
 * Created by Nerio on 13/10/15.
 */
@NeededIndirectPluginReferences(indirectReferences = {
        @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION, plugin = Plugins.INCOMING_EXTRA_USER),
        @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION, plugin = Plugins.INCOMING_INTRA_USER)
})
public class AssetIssuerCommunitySubAppModulePluginRoot extends AbstractPlugin implements
        AssetIssuerCommunitySubAppModuleManager {

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    ErrorManager errorManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.ASSET_ISSUER)
    IdentityAssetIssuerManager identityAssetIssuerManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR, plugin = Plugins.ASSET_ISSUER)
    ActorAssetIssuerManager actorAssetIssuerManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR_CONNECTION, plugin = Plugins.ASSET_ISSUER)
    AssetIssuerActorConnectionManager issuerActorConnectionManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR, plugin = Plugins.REDEEM_POINT)
    ActorAssetRedeemPointManager actorAssetRedeemPointManager;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.WALLET_MODULE, plugin = Plugins.REDEEM_POINT)
    AssetRedeemPointWalletSubAppModule assetRedeemPointWalletSubAppModule;

    @NeededPluginReference(platform = Platforms.DIGITAL_ASSET_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.ASSET_ISSUER)
    AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager;

    private SettingsManager<AssetIssuerSettings> settingsManager;

    BlockchainNetworkType blockchainNetworkType;

    private String appPublicKey;

    public AssetIssuerCommunitySubAppModulePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public List<ActorAssetIssuer> getAllActorAssetIssuerRegistered() throws CantGetAssetIssuerActorsException {
        try {
            List<ActorAssetIssuer> list = assetIssuerActorNetworkServiceManager.getListActorAssetIssuerRegistered();
            if (list == null) return Collections.EMPTY_LIST;
            for (ActorAssetIssuer issuer : list) {
                issuerActorConnectionManager.saveNewActorAssetIssuer(issuer);
            }
            issuerActorConnectionManager.updateOfflineIssuers(list);
        } catch (CantRequestListActorAssetIssuerRegisteredException | CantUpdateActorAssetIssuerException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetAssetIssuerActorsException("Service not available, try in a few minutes.", null, "Problems with the Web Service.", null);
        } catch (CantCreateActorAssetIssuerException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        return issuerActorConnectionManager.getAllIssuers();
    }

    @Override
    public List<ActorAssetIssuer> getAllActorAssetIssuerConnected() throws CantGetAssetIssuerActorsException {
        return issuerActorConnectionManager.getAllIssuerConnected();
    }

    @Override
    public DAPConnectionState getActorIssuerRegisteredDAPConnectionState(String actorAssetPublicKey) throws CantGetAssetIssuerActorsException {
        return issuerActorConnectionManager.getConnectionState(actorAssetPublicKey);
    }

    @Override
    public ActorAssetIssuer getActorIssuer(String actorPublicKey) throws CantGetAssetIssuerActorsException, CantAssetIssuerActorNotFoundException {
        return issuerActorConnectionManager.getByPublicKey(actorPublicKey);
    }

    @Override
    public void connectToActorAssetIssuer(ActorAssetRedeemPoint requester, List<ActorAssetIssuer> actorAssetIssuers) throws CantConnectToActorAssetRedeemPointException {

        ActorAssetRedeemPoint actorAssetRedeemPoint;
        //TODO Actor Asset Redeem Point de BD Local
        try {
            actorAssetRedeemPoint = actorAssetRedeemPointManager.getActorAssetRedeemPoint();

            blockchainNetworkType = assetRedeemPointWalletSubAppModule.getSelectedNetwork();

            RedeemPointActorRecord redeemPointActorRecord = new RedeemPointActorRecord(
                    actorAssetRedeemPoint.getActorPublicKey(),
                    actorAssetRedeemPoint.getName(),
                    actorAssetRedeemPoint.getDapConnectionState(),
                    actorAssetRedeemPoint.getLocationLatitude(),
                    actorAssetRedeemPoint.getLocationLongitude(),
                    actorAssetRedeemPoint.getCryptoAddress(),
                    actorAssetRedeemPoint.getRegistrationDate(),
                    actorAssetRedeemPoint.getLastConnectionDate(),
                    actorAssetRedeemPoint.getType(),
                    blockchainNetworkType,
                    actorAssetRedeemPoint.getProfileImage());

            if (redeemPointActorRecord != null) {
                actorAssetRedeemPointManager.sendMessage(redeemPointActorRecord, actorAssetIssuers);
                for (ActorAssetIssuer actorAssetIssuer : actorAssetIssuers) {
                    issuerActorConnectionManager.updateConnectionState(actorAssetIssuer.getActorPublicKey(), DAPConnectionState.CONNECTING);
                }
            } else
                throw new CantConnectToActorAssetRedeemPointException(CantConnectToActorAssetRedeemPointException.DEFAULT_MESSAGE, null, "THERE WAS AN ERROR GET ACTOR ASSET REDEEM POINT.", null);

        } catch (CantGetAssetRedeemPointActorsException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantConnectToActorAssetRedeemPointException(CantConnectToActorAssetRedeemPointException.DEFAULT_MESSAGE, e, "THERE WAS AN ERROR GET ACTOR ASSET REDEEM POINT.", null);
        } catch (CantConnectToActorAssetException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantConnectToActorAssetRedeemPointException(CantConnectToActorAssetRedeemPointException.DEFAULT_MESSAGE, e, "THERE WAS AN ERROR CONNECTING TO ASSET ISSUERS.", null);
        } catch (CantUpdateActorAssetIssuerException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantConnectToActorAssetRedeemPointException(CantConnectToActorAssetRedeemPointException.DEFAULT_MESSAGE, e, "THERE WAS AN ERROR UPDATING TO ASSET ISSUERS.", null);
        }
    }

    @Override
    public IdentityAssetIssuer getActiveAssetIssuerIdentity() throws CantGetIdentityAssetIssuerException {
        try {
            return identityAssetIssuerManager.getIdentityAssetIssuer();
        } catch (CantGetAssetIssuerIdentitiesException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetIdentityAssetIssuerException(e);
        }
    }

    @Override
    public void askActorAssetIssuerForConnection(List<ActorAssetIssuer> actorAssetIssuers) throws CantAskConnectionActorAssetException, CantRequestAlreadySendActorAssetException {
        try {
            ActorAssetRedeemPoint actorAssetRedeemPoint = actorAssetRedeemPointManager.getActorAssetRedeemPoint();
            if (actorAssetRedeemPoint != null) {
                for (ActorAssetIssuer actorAssetIssuer : actorAssetIssuers) {
                    issuerActorConnectionManager.askConnection(
                            actorAssetIssuer.getActorPublicKey());

//                actorAssetIssuerManager.updateIssuerRegisteredDAPConnectionState(actorAssetIssuer.getActorPublicKey(), DAPConnectionState.CONNECTING);
                    //TODO SOLO DEBE ENVIARSE EL MENSAJE LUEGO DE RECIBIDA LA ACEPTACION POR EL ISSUER A CONECTAR
//                actorAssetRedeemPointManager.sendMessage(actorAssetRedeemPoint, actorAssetIssuers);

                    if (issuerActorConnectionManager.getConnectionState(actorAssetIssuer.getActorPublicKey()) != DAPConnectionState.REGISTERED_REMOTELY) {
                        System.out.println("The User you are trying to connect with is not connected" +
                                "so we send the message to the assetIssuerActorNetworkService");
                        this.assetIssuerActorNetworkServiceManager.askConnectionActorAsset(
                                actorAssetRedeemPoint.getActorPublicKey(),
                                actorAssetRedeemPoint.getName(),
                                Actors.DAP_ASSET_REDEEM_POINT,
                                actorAssetIssuer.getActorPublicKey(),
                                actorAssetIssuer.getName(),
                                Actors.DAP_ASSET_ISSUER,
                                actorAssetRedeemPoint.getProfileImage(),
                                blockchainNetworkType);
                    } else {
                        //TODO REVISAR FUNCIONAMIENTO - SI LLEGA A OCURRIR
                        this.assetIssuerActorNetworkServiceManager.acceptConnectionActorAsset(actorAssetRedeemPoint.getActorPublicKey(), actorAssetIssuer);
                        System.out.println("The actor asset Issuer is connected");
                    }
                }
            } else {
                throw new CantConnectToActorAssetException(CantConnectToActorAssetException.DEFAULT_MESSAGE, null, "There was an error connecting to users. No identity", null);
            }
        } catch (CantAskConnectionActorAssetException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantAskConnectionActorAssetException("", e, "", "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantAskConnectionActorAssetException("CAN'T ASK ACTOR ASSET ISSUER CONNECTION", FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public void acceptActorAssetIssuer(String actorAssetIssuerPublicKey, ActorAssetIssuer actorAssetAccepted) throws CantAcceptActorAssetUserException {
        try {
            if (actorAssetAccepted.getType().getCode().equals(Actors.DAP_ASSET_REDEEM_POINT.getCode())) {
                this.actorAssetRedeemPointManager.acceptActorAssetRedeem(actorAssetIssuerPublicKey, actorAssetAccepted.getActorPublicKey());
            } else {
                issuerActorConnectionManager.acceptConnection(actorAssetAccepted.getActorPublicKey());
            }
            this.assetIssuerActorNetworkServiceManager.acceptConnectionActorAsset(actorAssetIssuerPublicKey, actorAssetAccepted);
        } catch (CantAcceptActorAssetUserException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantAcceptActorAssetUserException("CAN'T ACCEPT ACTOR ASSET ISSUER CONNECTION - KEY " + actorAssetAccepted.getActorPublicKey(), e, "", "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantAcceptActorAssetUserException("CAN'T ACCEPT ACTOR ASSET ISSUER CONNECTION - KEY " + actorAssetAccepted.getActorPublicKey(), FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public void denyConnectionActorAssetIssuer(String actorAssetIssuerLoggedPublicKey, ActorAssetIssuer actorAssetReject) throws CantDenyConnectionActorAssetException {
        try {
            if (actorAssetReject.getType() == Actors.DAP_ASSET_REDEEM_POINT) {
                this.actorAssetRedeemPointManager.acceptActorAssetRedeem(actorAssetIssuerLoggedPublicKey, actorAssetReject.getActorPublicKey());
            } else {
                issuerActorConnectionManager.denyConnection(actorAssetReject.getActorPublicKey());
            }
            this.assetIssuerActorNetworkServiceManager.denyConnectionActorAsset(actorAssetIssuerLoggedPublicKey, actorAssetReject);
        } catch (CantDenyConnectionActorAssetException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantDenyConnectionActorAssetException("CAN'T DENY ACTOR ASSET ISSUER CONNECTION - KEY:" + actorAssetReject.getActorPublicKey(), e, "", "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantDenyConnectionActorAssetException("CAN'T DENY ACTOR ASSET ISSUER CONNECTION - KEY:" + actorAssetReject.getActorPublicKey(), FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public void cancelActorAssetIssuer(ActorAssetIssuer actorAssetToCancel) throws CantCancelConnectionActorAssetException {
        try {
            issuerActorConnectionManager.cancelConnection(actorAssetToCancel.getActorPublicKey());
        } catch (CantCancelConnectionActorAssetException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelConnectionActorAssetException("CAN'T CANCEL ACTOR ASSET ISSUER CONNECTION - KEY:" + actorAssetToCancel.getActorPublicKey(), e, "", "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelConnectionActorAssetException("CAN'T CANCEL ACTOR ASSET ISSUER CONNECTION - KEY:" + actorAssetToCancel.getActorPublicKey(), FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public List<ActorAssetIssuer> getWaitingYourConnectionActorAssetIssuer(String actorAssetUserLoggedPublicKey, int max, int offset) throws CantGetActorAssetWaitingException {
        try {
            List<DAPActor> dapActor;
            List<ActorAssetIssuer> actorAssetIssuers = new ArrayList<>();

            dapActor = this.actorAssetRedeemPointManager.getWaitingYourConnectionActorAssetRedeem(actorAssetUserLoggedPublicKey, max, offset);

            if (dapActor.size() <= 0)
                dapActor = issuerActorConnectionManager.getWaitingYourConnectionActorAssetIssuer(max, offset);

            //TODO Mejorar Implementacion para tener informacion mas completa
            for (DAPActor record : dapActor) {
                actorAssetIssuers.add((new AssetIssuerActorRecord(
                        record.getActorPublicKey(),
                        record.getName(),
                        null,
                        (double) 0,
                        (double) 0,
                        (long) 0,
                        (long) 0,
                        record.getType(),
                        null,
                        null,
                        record.getProfileImage())));
            }

            return actorAssetIssuers;
        } catch (CantGetActorAssetWaitingException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetActorAssetWaitingException("CAN'T GET ACTOR ASSET ISSUER WAITING YOUR ACCEPTANCE", e, "", "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetActorAssetWaitingException("CAN'T GET ACTOR ASSET ISSUER WAITING YOUR ACCEPTANCE", FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public List<ActorAssetIssuer> getWaitingTheirConnectionActorAssetIssuer(String actorAssetUserLoggedPublicKey, int max, int offset) throws CantGetActorAssetWaitingException {
        try {
            List<DAPActor> dapActor;
            List<ActorAssetIssuer> actorAssetIssuers = new ArrayList<>();

            dapActor = this.actorAssetRedeemPointManager.getWaitingTheirConnectionActorAssetRedeem(actorAssetUserLoggedPublicKey, max, offset);

            if (dapActor.size() <= 0)
                dapActor = this.issuerActorConnectionManager.getWaitingTheirConnectionActorAssetIssuer(max, offset);

            //TODO Mejorar Implementacion para tener informacion mas completa
            for (DAPActor record : dapActor) {
                actorAssetIssuers.add((new AssetIssuerActorRecord(
                        record.getActorPublicKey(),
                        record.getName(),
                        null,
                        (double) 0,
                        (double) 0,
                        (long) 0,
                        (long) 0,
                        record.getType(),
                        null,
                        null,
                        record.getProfileImage())));
            }

            return actorAssetIssuers;
        } catch (CantGetActorAssetWaitingException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetActorAssetWaitingException("CAN'T GET ACTOR ASSET ISSUER WAITING THEIR ACCEPTANCE", e, "", "Error on ACTOR ASSET ISSUER Manager");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetActorAssetWaitingException("CAN'T GET ACTOR ASSET ISSUER WAITING THEIR ACCEPTANCE", FermatException.wrapException(e), "", "unknown exception");
        }
    }

    @Override
    public ActorAssetIssuer getLastNotification(String issuerPk) throws CantGetActorAssetNotificationException {
        try {
            return issuerActorConnectionManager.getLastNotification(issuerPk);
        } catch (CantGetActorAssetNotificationException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetActorAssetNotificationException("CAN'T GET ACTOR ASSET ISSUER LAST NOTIFICATION", e, "", "Error on ACTOR ISSUER MANAGER");
        }
    }

    @Override
    public int getWaitingYourConnectionActorAssetIssuerCount() {
        //TODO: falta que este metodo que devuelva la cantidad de request de conexion que tenes
        try {
            int countActor;

            if (getActiveAssetIssuerIdentity() != null) {
                countActor = actorAssetRedeemPointManager.getWaitingYourConnectionActorAssetRedeem(getActiveAssetIssuerIdentity().getPublicKey(), 100, 0).size();

                if (countActor <= 0) {
                    countActor = getWaitingYourConnectionActorAssetIssuer(getActiveAssetIssuerIdentity().getPublicKey(), 100, 0).size();
                }
                return countActor;

            }

        } catch (CantGetActorAssetWaitingException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        } catch (CantGetIdentityAssetIssuerException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public SettingsManager<AssetIssuerSettings> getSettingsManager() {
        if (this.settingsManager != null)
            return this.settingsManager;

        this.settingsManager = new SettingsManager<>(
                pluginFileSystem,
                pluginId
        );

        return this.settingsManager;
    }

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return identityAssetIssuerManager.getSelectedActorIdentity();
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {
        identityAssetIssuerManager.createNewIdentityAssetIssuer(name, profile_img);
    }

    @Override
    public void setAppPublicKey(String publicKey) {
        this.appPublicKey = publicKey;
    }

    @Override
    public int[] getMenuNotifications() {
        int[] notifications = new int[5];
        try {
            if (getSelectedActorIdentity() != null) {
                notifications[2] = actorAssetRedeemPointManager.getWaitingYourConnectionActorAssetRedeem(getSelectedActorIdentity().getPublicKey(), 99, 0).size();

                if (notifications[2] <= 0) {
                    notifications[2] = issuerActorConnectionManager.getWaitingYourConnectionActorAssetIssuer(99, 0).size();
                }
            } else {
                notifications[2] = 0;
            }
        } catch (CantGetActorAssetWaitingException | CantGetSelectedActorIdentityException | ActorIdentityNotSelectedException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        }
        return notifications;
    }
}
