package org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.internal;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.BroadcasterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.exceptions.CantGetExtendedPublicKeyException;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.watch_only_vault.ExtendedPublicKey;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.fermat.fermat_dap_api.layer.all_definition.DAPConstants;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.all_definition.events.NewRequestActorNotificationEvent;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantCreateNewDeveloperException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantUpdateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.exceptions.CantConnectToActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptActorAssetUserException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCreateActorAssetReceiveException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantRequestAlreadySendActorAssetException;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.interfaces.ActorNotification;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.database.AssetIssuerDAO;
import org.fermat.fermat_dap_plugin.layer.actor_connection.asset_issuer.developer.version_1.structure.events.RedeemerAddressesMonitorAgent;

import java.util.List;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 13/04/16.
 */
public final class AssetIssuerConnectionManager {

    //VARIABLE DECLARATION
    private final AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager;
    private final AssetIssuerDAO dao;
    private final Broadcaster broadcaster;
    private final EventManager eventManager;
    private final AssetVaultManager assetVaultManager;
    private final CryptoAddressBookManager cryptoAddressBookManager;

    //CONSTRUCTORS
    public AssetIssuerConnectionManager(AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager, AssetIssuerDAO dao, Broadcaster broadcaster, EventManager eventManager, AssetVaultManager assetVaultManager, CryptoAddressBookManager cryptoAddressBookManager) {
        this.assetIssuerActorNetworkServiceManager = assetIssuerActorNetworkServiceManager;
        this.dao = dao;
        this.broadcaster = broadcaster;
        this.eventManager = eventManager;
        this.assetVaultManager = assetVaultManager;
        this.cryptoAddressBookManager = cryptoAddressBookManager;
    }

    //PUBLIC METHODS
    public void receivingActorAssetIssuerRequestConnection(String issuerName,
                                                           String issuerPublicKey,
                                                           byte[] profileImage) throws CantCreateActorAssetReceiveException, CantLoadTableToMemoryException, CantUpdateRecordException, CantCreateNewDeveloperException, CantInsertRecordException, CantPersistFileException, RecordsNotFoundException, CantCreateFileException {
        if (dao.issuerExists(issuerPublicKey, DAPConnectionState.PENDING_REMOTELY)) {
            dao.updateConnectionState(issuerPublicKey, DAPConnectionState.REGISTERED_REMOTELY);
        } else if (!dao.issuerExists(issuerPublicKey, DAPConnectionState.REGISTERED_LOCALLY) || !dao.issuerExists(issuerPublicKey, DAPConnectionState.PENDING_LOCALLY)) {
            AssetIssuerActorRecord issuer = new AssetIssuerActorRecord();
            issuer.setActorPublicKey(issuerPublicKey);
            issuer.setName(issuerName);
            issuer.setProfileImage(profileImage);
            issuer.setDapConnectionState(DAPConnectionState.PENDING_LOCALLY);
            dao.saveNewActor(issuer);
        }
    }

    public void askActorAssetIssuerForConnection(String issuerPublicKey) throws CantAskConnectionActorAssetException, CantRequestAlreadySendActorAssetException, CantLoadTableToMemoryException, CantUpdateRecordException {
        if (dao.issuerExists(issuerPublicKey, DAPConnectionState.CONNECTING)) {
            throw new CantRequestAlreadySendActorAssetException("CAN'T INSERT ACTOR ASSET USER", null, "", "The request already sent to actor.");
        } else if (dao.issuerExists(issuerPublicKey, DAPConnectionState.PENDING_LOCALLY)) {
            dao.updateConnectionState(issuerPublicKey, DAPConnectionState.REGISTERED_ONLINE);
        } else {
            dao.updateConnectionState(issuerPublicKey, DAPConnectionState.CONNECTING);
        }
    }

    public void updateOfflineIssuer(List<ActorAssetIssuer> issuers) throws CantLoadFileException, FileNotFoundException, InvalidParameterException, CantLoadTableToMemoryException, CantCreateFileException {
        List<ActorAssetIssuer> allIssuers = dao.getAllIssuers();
        for (ActorAssetIssuer issuer : allIssuers) {
            try {
                if (issuers.contains(issuer)) {
                    switch (issuer.getDapConnectionState()) {
                        case CONNECTED_OFFLINE:
                            dao.updateConnectionState(issuer.getActorPublicKey(), DAPConnectionState.CONNECTED_ONLINE);
                            break;
                        case REGISTERED_OFFLINE:
                            dao.updateConnectionState(issuer.getActorPublicKey(), DAPConnectionState.REGISTERED_ONLINE);
                            break;
                    }
                } else {
                    switch (issuer.getDapConnectionState()) {
                        case CONNECTED_ONLINE:
                            dao.updateConnectionState(issuer.getActorPublicKey(), DAPConnectionState.CONNECTED_OFFLINE);
                            break;
                        case REGISTERED_ONLINE:
                            dao.updateConnectionState(issuer.getActorPublicKey(), DAPConnectionState.REGISTERED_OFFLINE);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue; //Just a visual reference.
            }
        }
    }

    public void processNotifications() throws CantGetActorAssetNotificationException {
        try {
            List<ActorNotification> actorNotifications = assetIssuerActorNetworkServiceManager.getPendingNotifications();

            for (ActorNotification notification : actorNotifications) {
                String intraUserSendingPublicKey = notification.getActorSenderPublicKey();

                switch (notification.getAssetNotificationDescriptor()) {
                    case ASKFORCONNECTION:
                        if (notification.getActorSenderType() == Actors.DAP_ASSET_REDEEM_POINT) {
                            sendingRequestToActor(notification);
                        } else {
                            receivingActorAssetIssuerRequestConnection(
                                    notification.getActorSenderAlias(),
                                    intraUserSendingPublicKey,
                                    notification.getActorSenderProfileImage());
                        }
                        break;
                    case CANCEL:
                        if (notification.getActorSenderType() == Actors.DAP_ASSET_REDEEM_POINT) {
                            sendingRequestToActor(notification);
                        } else {
                            cancelActorAssetIssuer(intraUserSendingPublicKey);
                        }
                        break;
                    case ACCEPTED:
                        if (notification.getActorSenderType() == Actors.DAP_ASSET_REDEEM_POINT) {
                            sendingRequestToActor(notification);
                        } else {
                            acceptActorAssetIssuer(intraUserSendingPublicKey);
                        }
                        sendMessage(notification);
                        break;
                    case DENIED:
                        if (notification.getActorSenderType() == Actors.DAP_ASSET_REDEEM_POINT) {
                            sendingRequestToActor(notification);
                        } else {
                            denyConnectionActorAssetIssuer(intraUserSendingPublicKey);
                            broadcaster.publish(BroadcasterType.UPDATE_VIEW, DAPConstants.DAP_UPDATE_VIEW_ANDROID);
                        }
                        break;
                    case ACTOR_ASSET_NOT_FOUND:
                        dao.updateConnectionState(intraUserSendingPublicKey, DAPConnectionState.ERROR_UNKNOWN);
                        break;
                    case EXTENDED_KEY:
                        if (notification.getMessageXML() == null)
                            this.newRequestExtendedPublicKey(notification);
                        else
                            this.receiveExtendedPublicKey(notification);
                        break;
                    default:
                        break;
                }

                assetIssuerActorNetworkServiceManager.confirmActorAssetNotification(notification.getId());
            }
        } catch (CantAcceptActorAssetUserException e) {
            throw new CantGetActorAssetNotificationException("CAN'T PROCESS NETWORK SERVICE NOTIFICATIONS", e, "", "Error Update Contact State to Accepted");
        } catch (Exception e) {
            throw new CantGetActorAssetNotificationException("CAN'T PROCESS NETWORK SERVICE NOTIFICATIONS", FermatException.wrapException(e), "", "");
        }
    }


    private void newRequestExtendedPublicKey(ActorNotification actorNotification) {
        System.out.println("*****Actor Asset Redeem Point Solicita*****");
        System.out.println("Actor Asset Redeem Point Key: " + actorNotification.getActorSenderPublicKey());
        System.out.println("Actor Asset Redeem Point name: " + actorNotification.getActorSenderAlias());

        /**
         * I will request a new ExtendedPublicKey from the Asset Vault.
         * The asset vault will create a new extendedPublic Key for this redeem point.
         */
        ExtendedPublicKey extendedPublicKey = null;
        try {
            extendedPublicKey = assetVaultManager.getRedeemPointExtendedPublicKey(actorNotification.getActorSenderPublicKey());

        } catch (CantGetExtendedPublicKeyException e) {
            /**
             * if there was an error and we coulnd't get the ExtendedPublicKey, then we will send a null public Key
             * and this will be handle by the Redeem Point.
             * We might need to find a better way to handle this.
             */
            e.printStackTrace();
        }

        /**
         * I will create a new Message with the extended public Key.
         */
        if (extendedPublicKey != null) {
            RedeemerAddressesMonitorAgent monitorAgent = new RedeemerAddressesMonitorAgent(cryptoAddressBookManager, assetVaultManager, actorNotification.getActorDestinationPublicKey());
            try {
                monitorAgent.start();
            } catch (CantStartAgentException e) {
                /**
                 * If there was a problem, I will continue.
                 */
                e.printStackTrace();
            }

            System.out.println("*****Actor Asset Issuer ****: extended Public key generated");
            /**
             * and send it using the redeem point network service.
             */
            try {
                System.out.println("*****Actor Asset Issuer ****: enviando Event a Redeem Point");
                assetIssuerActorNetworkServiceManager.responseExtended(actorNotification, extendedPublicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveExtendedPublicKey(ActorNotification actorNotification) {
        FermatEvent event = eventManager.getNewEvent(EventType.NEW_RECEIVE_EXTENDED_KEY_ACTOR);
        event.setSource(EventSource.ACTOR_ASSET_ISSUER);
        ((NewRequestActorNotificationEvent) event).setActorNotification(actorNotification);
        eventManager.raiseEvent(event);
    }

    public void handleRequestNotificationEvent(ActorNotification actorNotification) {
        try {
            switch (actorNotification.getAssetNotificationDescriptor()) {
                case ASKFORCONNECTION:
                    receivingActorAssetIssuerRequestConnection(
                            actorNotification.getActorSenderAlias(),
                            actorNotification.getActorSenderPublicKey(),
                            actorNotification.getActorSenderProfileImage());
                    break;
                case CANCEL:
                    this.cancelActorAssetIssuer(actorNotification.getActorSenderPublicKey());
                    break;
                case ACCEPTED:
                    acceptActorAssetIssuer(actorNotification.getActorSenderPublicKey());
                    break;
                case DENIED:
                    denyConnectionActorAssetIssuer(actorNotification.getActorSenderPublicKey());
                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, DAPConstants.DAP_UPDATE_VIEW_ANDROID);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleCompleteRegistrationEvent(ActorAssetIssuer issuer) {
        try {
            dao.updateConnectionState(issuer.getActorPublicKey(), DAPConnectionState.REGISTERED_ONLINE);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException e) {
            e.printStackTrace();
        }
    }

    public void cancelActorAssetIssuer(String actorAssetUserToCancelPublicKey) throws CantUpdateRecordException, CantLoadTableToMemoryException {
        dao.updateConnectionState(actorAssetUserToCancelPublicKey, DAPConnectionState.CANCELLED_LOCALLY);
    }

    public void denyConnectionActorAssetIssuer(String actorAssetIssuerToRejectPublicKey) throws CantUpdateRecordException, CantLoadTableToMemoryException {
        dao.updateConnectionState(actorAssetIssuerToRejectPublicKey, DAPConnectionState.DENIED_LOCALLY);
    }

    public void acceptActorAssetIssuer(String actorAssetIssuerToAddPublicKey) throws CantAcceptActorAssetUserException, CantUpdateRecordException, CantLoadTableToMemoryException {
        dao.updateConnectionState(actorAssetIssuerToAddPublicKey, DAPConnectionState.REGISTERED_ONLINE);
    }

    //PRIVATE METHODS
    private void sendingRequestToActor(ActorNotification actorNotification) {
        FermatEvent event = eventManager.getNewEvent(EventType.ACTOR_ASSET_REQUEST_CONNECTIONS);
        event.setSource(EventSource.ACTOR_ASSET_REDEEM_POINT);
        ((NewRequestActorNotificationEvent) event).setActorNotification(actorNotification);
        eventManager.raiseEvent(event);
    }

    private void sendMessage(ActorNotification actorNotification) throws CantConnectToActorAssetException {
        assetIssuerActorNetworkServiceManager.buildSendMessage(actorNotification);
    }
    //GETTER AND SETTERS

    //INNER CLASSES
}
