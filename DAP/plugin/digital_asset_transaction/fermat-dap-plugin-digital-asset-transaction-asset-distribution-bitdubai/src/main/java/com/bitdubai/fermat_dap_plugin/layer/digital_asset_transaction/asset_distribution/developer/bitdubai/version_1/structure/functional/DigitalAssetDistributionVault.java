package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version_1.structure.functional;

import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkManager;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantGetDigitalAssetFromLocalStorageException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.interfaces.AbstractDigitalAssetVault;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWallet;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 04/10/15.
 */
public class DigitalAssetDistributionVault extends AbstractDigitalAssetVault {
    ErrorManager errorManager;

    public DigitalAssetDistributionVault(UUID pluginId,
                                         PluginFileSystem pluginFileSystem,
                                         ErrorManager errorManager,
                                         AssetIssuerWalletManager assetIssuerWalletManager,
                                         ActorAssetIssuerManager actorAssetIssuerManager,
                                         BitcoinNetworkManager bitcoinNetworkManager) throws CantSetObjectException {
        setPluginFileSystem(pluginFileSystem);
        setPluginId(pluginId);
        setErrorManager(errorManager);
        LOCAL_STORAGE_PATH = "digital-asset-transmission/";
        setAssetIssuerWalletManager(assetIssuerWalletManager);
        setActorAssetIssuerManager(actorAssetIssuerManager);
        setBitcoinCryptoNetworkManager(bitcoinNetworkManager);
    }

    public void setErrorManager(ErrorManager errorManager) throws CantSetObjectException {
        if (errorManager == null) {
            throw new CantSetObjectException("ErrorManager is null");
        }
        this.errorManager = errorManager;
    }

    public AssetIssuerWallet getIssuerWallet() throws CantLoadWalletException {
        return this.assetIssuerWalletManager.loadAssetIssuerWallet(this.walletPublicKey);
    }

    /**
     * This method get the XML file and cast the DigitalAssetMetadata object
     *
     * @param internalId AssetIssuing: Asset Issuing: This id is an UUID provided by DigitalAssetTransactionFactory, this will be used to identify the file in Local Storage
     * @return
     * @throws CantGetDigitalAssetFromLocalStorageException
     */
    @Override
    public DigitalAssetMetadata getDigitalAssetMetadataFromLocalStorage(String internalId) throws CantGetDigitalAssetFromLocalStorageException {
        try {
            return getIssuerWallet().getDigitalAssetMetadata(internalId);
        } catch (CantLoadWalletException e) {
            throw new CantGetDigitalAssetFromLocalStorageException();
        }
    }
}
