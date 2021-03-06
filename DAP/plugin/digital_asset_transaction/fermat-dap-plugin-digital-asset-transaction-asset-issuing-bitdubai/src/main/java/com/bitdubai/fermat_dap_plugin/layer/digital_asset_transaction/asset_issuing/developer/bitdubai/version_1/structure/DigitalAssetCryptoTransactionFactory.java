package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrencyVault;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.exceptions.CantRegisterCryptoAddressBookRecordException;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.bitcoin_vault.CryptoVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.exceptions.GetNewCryptoAddressException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletBalance;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_intra_actor.exceptions.CantGetOutgoingIntraActorTransactionManagerException;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_intra_actor.exceptions.OutgoingIntraActorCantSendFundsExceptions;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_intra_actor.exceptions.OutgoingIntraActorInsufficientFundsException;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_intra_actor.interfaces.OutgoingIntraActorManager;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.exceptions.CantListIntraWalletUsersException;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraWalletUserIdentity;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraWalletUserIdentityManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantGetBalanceException;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAsset;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.IssuingStatus;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.State;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.TransactionStatus;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.ObjectNotSetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantDeliverDigitalAssetToAssetWalletException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantGetGenesisAddressException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantIssueDigitalAssetsException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantSendGenesisAmountException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CryptoWalletBalanceInsufficientException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantCreateDigitalAssetFileException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantGetDigitalAssetFromLocalStorageException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantPersistDigitalAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantPersistsTransactionUUIDException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantSaveStatisticException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletManager;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantCheckAssetIssuingProgressException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantCreateDigitalAssetTransactionException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantGetDigitalAssetMetadataFromGenesisAddressException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantIssueDigitalAssetException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantPersistsGenesisAddressException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantPersistsGenesisTransactionException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantReadDigitalAssetFileException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.FormingGenesisException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.GenesisObtainedOrSettledException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.SendingCryptoException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure.database.AssetIssuingTransactionDao;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 04/09/15.
 */
public class DigitalAssetCryptoTransactionFactory implements DealsWithErrors {

    private int assetsAmount;
    private volatile AssetIssuingTransactionDao assetIssuingTransactionDao;
    private ActorAssetIssuerManager actorAssetIssuerManager;
    private AssetVaultManager assetVaultManager;
    private BitcoinWalletBalance bitcoinWalletBalance;
    private BlockchainNetworkType blockchainNetworkType;
    private CryptoVaultManager cryptoVaultManager;
    private BitcoinWalletManager bitcoinWalletManager;
    private CryptoAddressBookManager cryptoAddressBookManager;
    private IntraWalletUserIdentityManager intraWalletUserIdentityManager;
    private String digitalAssetFileName;
    private String digitalAssetFileStoragePath;
    private String digitalAssetLocalFilePath;
    private DigitalAssetIssuingVault digitalAssetIssuingVault;
    private volatile DigitalAsset digitalAsset;
    private ErrorManager errorManager;
    private OutgoingIntraActorManager outgoingIntraActorManager;
    private final String LOCAL_STORAGE_PATH = "digital-asset-issuing/";
    private PluginFileSystem pluginFileSystem;
    private UUID pluginId;
    private String walletPublicKey;
    private String actorAssetIssuerPublicKey;
    private String intraActorPublicKey;
    private final AssetIssuerWalletManager assetIssuerWalletManager;
    //This flag must be used to select the way to send bitcoins from this plugin

    public DigitalAssetCryptoTransactionFactory(UUID pluginId,
                                                CryptoVaultManager cryptoVaultManager,
                                                BitcoinWalletManager bitcoinWalletManager,
                                                PluginFileSystem pluginFileSystem,
                                                AssetVaultManager assetVaultManager,
                                                CryptoAddressBookManager cryptoAddressBookManager,
                                                OutgoingIntraActorManager outgoingIntraActorManager,
                                                AssetIssuerWalletManager assetIssuerWalletManager,
                                                ErrorManager errorManager,
                                                IntraWalletUserIdentityManager intraWalletUserIdentityManager,
                                                ActorAssetIssuerManager actorAssetIssuerManager) throws CantSetObjectException, CantExecuteDatabaseOperationException {

        this.cryptoVaultManager = cryptoVaultManager;
        this.bitcoinWalletManager = bitcoinWalletManager;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.assetVaultManager = assetVaultManager;
        this.cryptoAddressBookManager = cryptoAddressBookManager;
        this.outgoingIntraActorManager = outgoingIntraActorManager;
        this.assetIssuerWalletManager = assetIssuerWalletManager;
        this.errorManager = errorManager;
        this.intraWalletUserIdentityManager = intraWalletUserIdentityManager;
        this.actorAssetIssuerManager = actorAssetIssuerManager;
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    private void setBlockchainNetworkType(BlockchainNetworkType blockchainNetworkType) throws ObjectNotSetException {
        if (blockchainNetworkType == null) {
            throw new ObjectNotSetException("The BlockchainNetworkType is null");
        }
        this.blockchainNetworkType = blockchainNetworkType;
    }

    public void setDigitalAssetIssuingVault(DigitalAssetIssuingVault digitalAssetIssuingVault) throws CantSetObjectException {
        if (digitalAssetIssuingVault == null) {
            throw new CantSetObjectException("digitalAssetIssuingVault is null");
        }
        this.digitalAssetIssuingVault = digitalAssetIssuingVault;
    }

    public void setAssetIssuingTransactionDao(AssetIssuingTransactionDao assetIssuingTransactionDao) throws CantSetObjectException {
        this.assetIssuingTransactionDao = assetIssuingTransactionDao;
    }

    public int getNumberOfIssuedAssets(String digitalAssetPublicKey) throws CantCheckAssetIssuingProgressException {
        return this.assetIssuingTransactionDao.getNumberOfIssuedAssets(digitalAssetPublicKey);
    }

    public void setWalletPublicKey(String walletPublicKey) throws ObjectNotSetException {
        if (walletPublicKey == null) {
            throw new ObjectNotSetException("walletPublicKey is null");
        }
        this.walletPublicKey = walletPublicKey;
    }

    public void setActorAssetIssuerManager(ActorAssetIssuerManager actorAssetIssuerManager) throws CantSetObjectException {

        if (actorAssetIssuerManager == null) {
            throw new CantSetObjectException("actorAssetIssuerManager is null");
        }
        this.actorAssetIssuerManager = actorAssetIssuerManager;
    }

    private void getActorAssetIssuerPublicKey() throws ObjectNotSetException, CantGetAssetIssuerActorsException {
        if (actorAssetIssuerManager.getActorAssetIssuer() == null) {
            throw new ObjectNotSetException("ActorAssetIssuer is null");
        }
        try {
            this.actorAssetIssuerPublicKey = actorAssetIssuerManager.getActorAssetIssuer().getActorPublicKey();
            System.out.println("ASSET ISSUING Actor Asset Issuer public key " + actorAssetIssuerPublicKey);
        } catch (CantGetAssetIssuerActorsException exception) {
            throw new ObjectNotSetException(exception, "Setting the actor asset issuer manager", "Cannot get the actor asset issuer manager");
        }
    }

    private void getIntraWalletActorUserPublicKey() throws ObjectNotSetException, CantGetAssetIssuerActorsException, CantGetIntraWalletUsersException {
        try {
            if (!intraWalletUserIdentityManager.hasIntraUserIdentity()) {
                throw new ObjectNotSetException("intraWalletUserIdentityManager does not have Intra User Identity");
            }
            List<IntraWalletUserIdentity> intraWalletUserIdentityList = intraWalletUserIdentityManager.getAllIntraWalletUsersFromCurrentDeviceUser();
            if (intraWalletUserIdentityList.isEmpty()) {
                throw new ObjectNotSetException("intraWalletUserIdentityManager does not have Intra User Identity");
            }
            this.intraActorPublicKey = intraWalletUserIdentityList.get(0).getPublicKey();
        } catch (CantListIntraWalletUsersException exception) {
            throw new ObjectNotSetException(exception, "Getting IntraActor Public Key", "intraWalletUserIdentityManager does not have Intra User Identity");
        }
    }

    /**
     * This method checks that every object in Digital asset is set.
     *
     * @throws ObjectNotSetException
     * @throws CantIssueDigitalAssetException
     */
    private void areObjectsSettled() throws ObjectNotSetException, CantIssueDigitalAssetException {
        if (digitalAsset.getContract() == null) {
            throw new ObjectNotSetException("Digital Asset Contract is not set");
        }
        if (digitalAsset.getResources() == null) {
            throw new ObjectNotSetException("Digital Asset Resources is not set");
        }
        if (digitalAsset.getDescription() == null) {
            throw new ObjectNotSetException("Digital Asset Description is not set");
        }
        if (digitalAsset.getName() == null) {
            throw new ObjectNotSetException("Digital Asset Name is not set");
        }
        if (digitalAsset.getPublicKey() == null) {
            throw new ObjectNotSetException("Digital Asset PublicKey is not set");
        }
        if (digitalAsset.getState() == null) {
            digitalAsset.setState(State.DRAFT);
        }
        if (digitalAsset.getIdentityAssetIssuer() == null) {
            throw new ObjectNotSetException("Digital Asset Identity is not set");
        }
    }

    /**
     * This method checks that the Digital Asset publicKey is registered in Database
     *
     * @param publicKey
     * @throws CantIssueDigitalAssetException
     */
    private boolean isPublicKeyInDatabase(String publicKey) {
        try {
            return this.assetIssuingTransactionDao.isPublicKeyUsed(publicKey);
        } catch (CantCheckAssetIssuingProgressException exception) {
            return true;
        }
    }

    /**
     * This method persists the digital asset in database
     *
     * @throws CantPersistDigitalAssetException
     */
    private void persistFormingGenesisDigitalAsset() throws CantPersistDigitalAssetException {
        this.assetIssuingTransactionDao.persistDigitalAsset(
                digitalAsset.getPublicKey(),
                this.digitalAssetFileStoragePath,
                this.assetsAmount,
                this.blockchainNetworkType,
                this.walletPublicKey);
    }

    /**
     * This method gets the genesis address from asset vault
     *
     * @return
     * @throws CantGetGenesisAddressException
     */
    private CryptoAddress requestGenesisAddress() throws CantGetGenesisAddressException {
        try {
            return this.assetVaultManager.getNewAssetVaultCryptoAddress(this.blockchainNetworkType);
        } catch (GetNewCryptoAddressException exception) {
            throw new CantGetGenesisAddressException(exception, "Requesting a genesis address", "Cannot get a new crypto address from asset vault");
        }
    }

    /**
     * This method checks if is available balance in the selected crypto wallet by wallet Public key
     *
     * @throws CryptoWalletBalanceInsufficientException
     * @throws CantGetBalanceException
     */
    private void checkCryptoWalletBalance() throws CantLoadWalletException, CantCalculateBalanceException, CryptoWalletBalanceInsufficientException {

        if (this.bitcoinWalletBalance == null) {
            BitcoinWalletWallet bitcoinWalletWallet = this.bitcoinWalletManager.loadWallet(this.walletPublicKey);
            this.bitcoinWalletBalance = bitcoinWalletWallet.getBalance(BalanceType.AVAILABLE);
        }
        long bitcoinWalletAvailableBalance = bitcoinWalletBalance.getBalance();
        long digitalAssetGenesisAmount = this.digitalAsset.getGenesisAmount();
        if (digitalAssetGenesisAmount > bitcoinWalletAvailableBalance) {
            throw new CryptoWalletBalanceInsufficientException("The current balance in Wallet " + this.walletPublicKey + " is " + bitcoinWalletAvailableBalance + " the amount needed is " + digitalAssetGenesisAmount);
        }

    }

    private void setDigitalAssetLocalFilePath() {
        this.digitalAssetFileStoragePath = this.LOCAL_STORAGE_PATH + this.digitalAsset.getPublicKey();
        setDigitalAssetLocalStoragePath();
    }

    private void setDigitalAssetLocalFilePath(String publicKey) {
        this.digitalAssetFileStoragePath = this.LOCAL_STORAGE_PATH + publicKey;
        setDigitalAssetLocalStoragePath();
    }

    private void setDigitalAssetLocalStoragePath() {
        this.digitalAssetFileName = this.digitalAsset.getName() + ".xml";
        this.digitalAssetLocalFilePath = digitalAssetFileStoragePath + "/" + digitalAssetFileName;
    }

    /**
     * This method gets the selected digital asset from local storage
     *
     * @throws CantGetDigitalAssetFromLocalStorageException
     */
    private void getDigitalAssetFileFromLocalStorage() throws CantGetDigitalAssetFromLocalStorageException {
        try {
            PluginTextFile digitalAssetFile = this.pluginFileSystem.getTextFile(this.pluginId, this.digitalAssetFileStoragePath, this.digitalAssetFileName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            String digitalAssetXMLString = digitalAssetFile.getContent();
            this.digitalAsset = (DigitalAsset) XMLParser.parseXML(digitalAssetXMLString, this.digitalAsset);
        } catch (FileNotFoundException exception) {
            throw new CantGetDigitalAssetFromLocalStorageException(exception, "Getting Digital Asset file from local storage", "Cannot find " + this.digitalAssetLocalFilePath + this.digitalAssetFileName + "' file");
        } catch (CantCreateFileException exception) {
            throw new CantGetDigitalAssetFromLocalStorageException(exception, "Getting Digital Asset file from local storage", "Cannot create " + this.digitalAssetLocalFilePath + this.digitalAssetFileName + "' file");
        }
    }

    /**
     * This method persists the digital asset in local storage
     *
     * @throws CantCreateDigitalAssetFileException
     */
    private void persistInLocalStorage() throws CantCreateDigitalAssetFileException {
        try {
            setDigitalAssetLocalFilePath();
            String digitalAssetInnerXML = digitalAsset.toString();
            PluginTextFile digitalAssetFile = this.pluginFileSystem.createTextFile(this.pluginId, this.digitalAssetFileStoragePath, this.digitalAssetFileName, FilePrivacy.PUBLIC, FileLifeSpan.PERMANENT);
            digitalAssetFile.setContent(digitalAssetInnerXML);
            digitalAssetFile.persistToMedia();
        } catch (CantCreateFileException exception) {
            throw new CantCreateDigitalAssetFileException(exception, "Persisting Digital Asset in local storage", "Can't create '" + this.digitalAssetLocalFilePath + this.digitalAssetFileName + "' file");
        } catch (CantPersistFileException exception) {
            throw new CantCreateDigitalAssetFileException(exception, "Persisting Digital Asset in local storage", "Can't persist '" + this.digitalAssetLocalFilePath + this.digitalAssetFileName + "' file");
        }
    }

    /**
     * This method generate a random UUID to identify the asset issuing process in database. The value cannot be repeated
     *
     * @return
     * @throws CantExecuteQueryException
     * @throws UnexpectedResultReturnedFromDatabaseException
     * @throws CantPersistDigitalAssetException
     */
    private UUID generateTransactionUUID() throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException, CantPersistDigitalAssetException {
        UUID transactionUUID = UUID.randomUUID();
        try {
            if (this.assetIssuingTransactionDao.isTransactionIdUsed(transactionUUID)) {
                generateTransactionUUID();
            }
        } catch (CantCheckAssetIssuingProgressException | UnexpectedResultReturnedFromDatabaseException exception) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
        }
        this.assetIssuingTransactionDao.persistDigitalAssetTransactionId(this.digitalAsset.getPublicKey(), transactionUUID.toString());
        return transactionUUID;
    }

    /**
     * This method issue multiple digital assets
     *
     * @param digitalAsset
     * @param assetsAmount
     * @param walletPublicKey
     * @param blockchainNetworkType
     * @throws CantIssueDigitalAssetsException
     * @throws CantDeliverDigitalAssetToAssetWalletException
     */
    public void issueDigitalAssets(DigitalAsset digitalAsset, int assetsAmount, String walletPublicKey, BlockchainNetworkType blockchainNetworkType) throws CantIssueDigitalAssetsException, CantDeliverDigitalAssetToAssetWalletException, CantCheckAssetIssuingProgressException {

        this.digitalAsset = digitalAsset;
        this.assetsAmount = assetsAmount;
        try {

            getActorAssetIssuerPublicKey();
            getIntraWalletActorUserPublicKey();
            setBlockchainNetworkType(blockchainNetworkType);
            setWalletPublicKey(walletPublicKey);
            this.digitalAssetIssuingVault.setWalletPublicKey(walletPublicKey);
            int MINIMAL_DIGITAL_ASSET_TO_GENERATE_AMOUNT = 1;
            if (assetsAmount < MINIMAL_DIGITAL_ASSET_TO_GENERATE_AMOUNT) {
                this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.INVALID_NUMBER_TO_ISSUE);
                throw new ObjectNotSetException("The assetsAmount " + assetsAmount + " is lower that " + MINIMAL_DIGITAL_ASSET_TO_GENERATE_AMOUNT);
            }
            areObjectsSettled();
            if (isPublicKeyInDatabase(digitalAsset.getPublicKey())) {
                this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.ISSUING);
                for (String genesisTransaction : assetIssuingTransactionDao.getGenesisTransactionByAssetKey(digitalAsset.getPublicKey())) {
                    assetIssuingTransactionDao.updateTransactionProtocolStatus(genesisTransaction, ProtocolStatus.TO_BE_NOTIFIED);
                    this.assetIssuingTransactionDao.updateDigitalAssetTransactionStatus(assetIssuingTransactionDao.getTransactionIdByGenesisTransaction(genesisTransaction), TransactionStatus.ISSUING);
                }
                return;
            }
            //Persist the digital asset in local storage
            persistInLocalStorage();
            //Persist the digital asset in database
            persistFormingGenesisDigitalAsset();
            for (int i = 0; i < assetsAmount; i++) {
                createDigitalAssetCryptoTransaction();
            }
        } catch (ObjectNotSetException exception) {
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "Digital Asset object is not complete");
        } catch (CantCreateDigitalAssetFileException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.FILESYSTEM_EXCEPTION);
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "A local storage procedure exception");
        } catch (CantPersistDigitalAssetException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.DATABASE_EXCEPTION);
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "A database procedure exception");
        } catch (CantIssueDigitalAssetException exception) {
            //ALREADY UPDATED STATUS IN THE ROOT OF THIS EXCEPTION.
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "The public key is already used");
        } catch (CantSetObjectException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.WALLET_EXCEPTION);
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "The wallet public key is probably null");
        } catch (CantCheckAssetIssuingProgressException exception) {
            //ALREADY UPDATED STATUS IN THE ROOT OF THIS EXCEPTION.
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "Cannot check the asset issuing progress");
        } catch (CantGetAssetIssuerActorsException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.ACTOR_ISSUER_NULL);
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "The Actor Issuer is null");
        } catch (CantGetIntraWalletUsersException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.INTRA_ACTOR_NULL);
            throw new CantIssueDigitalAssetsException(exception, "Issuing " + assetsAmount + " Digital Assets", "The Actor Issuer is null");
        } catch (Exception e) {
            throw new CantIssueDigitalAssetsException(e, "Issuing " + assetsAmount + " Digital Assets", "Creating the Digital Asset transaction");
        }
    }


    /**
     * This method issue unfinished or interrupted digital assets
     *
     * @param publicKey
     */
    public void issuePendingDigitalAssets(String publicKey) {

        try {
            List<String> pendingDigitalAssetsTransactionIdList = this.assetIssuingTransactionDao.getPendingDigitalAssetsTransactionIdByPublicKey(publicKey);
            for (String pendingDigitalAssetsTransactionId : pendingDigitalAssetsTransactionIdList) {
                issueUnfinishedDigitalAsset(pendingDigitalAssetsTransactionId);
            }
        } catch (CantCheckAssetIssuingProgressException exception) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
        }
    }

    /**
     * This method checks if the digital Asset metadata is complete and ready to issue
     *
     * @param digitalAsset
     * @param digitalAssetMetadata
     * @return
     * @throws CantCheckAssetIssuingProgressException
     */
    private boolean isDigitalAssetComplete(DigitalAsset digitalAsset, DigitalAssetMetadata digitalAssetMetadata) throws CantCheckAssetIssuingProgressException {
        try {
            areObjectsSettled();
            CryptoAddress genesisAddress = digitalAsset.getGenesisAddress();
            if (genesisAddress == null) {
                return false;
            }
            String digitalAssetHash = digitalAssetMetadata.getDigitalAssetHash();
            if (digitalAssetHash == null || digitalAssetHash.equals("")) {
                return false;
            }
            /*String genesisTransaction=digitalAssetMetadata.getGenesisTransaction();
            if(genesisTransaction==null||genesisTransaction.equals("")){
                return false;
            }*/
            return true;
        } catch (ObjectNotSetException e) {
            return false;
        } catch (CantIssueDigitalAssetException exception) {
            throw new CantCheckAssetIssuingProgressException(exception, "Checking if digital asset is complete", "Unexpected results in database");
        }
    }

    private void readDigitalAssetFile(String transactionId) throws CantReadDigitalAssetFileException {
        try {
            //Es necesario leer el archivo para recobrar dicha transacción
            String digitalAssetPublicKey = this.assetIssuingTransactionDao.getDigitalAssetPublicKeyById(transactionId);
            setDigitalAssetLocalFilePath(digitalAssetPublicKey);
            //Leemos el archivo que contiene el digitalAsset
            getDigitalAssetFileFromLocalStorage();
        } catch (CantCheckAssetIssuingProgressException e) {
            throw new CantReadDigitalAssetFileException(e, "Read digital asset file", "Error getting value from table");
        } catch (UnexpectedResultReturnedFromDatabaseException e) {
            throw new CantReadDigitalAssetFileException(e, "Read digital asset file", "Unexpected result returned from database");
        } catch (CantGetDigitalAssetFromLocalStorageException e) {
            throw new CantReadDigitalAssetFileException(e, "Read digital asset file", "Can't read or create file");
        }
    }

    private void formingGenesis(String transactionId) throws FormingGenesisException {
        try {
            //FORMING_GENESIS: genesisAddress solicitada pero no presistida en base de datos, Asset persistido en archivo
            readDigitalAssetFile(transactionId);
            //Obtenemos la digital address y la persisitimos en base de datos
            getDigitalAssetGenesisAddressByUUID(transactionId);
        } catch (CantReadDigitalAssetFileException e) {
            throw new FormingGenesisException(e, "Forming Genesis", "Can't read the digital asset genesis address");
        } catch (CantPersistsGenesisAddressException e) {
            throw new FormingGenesisException(e, "Forming Genesis", "Can't persist the digital asset genesis address");
        } catch (CantGetGenesisAddressException e) {
            throw new FormingGenesisException(e, "Forming Genesis", "Error in request of digital asset genesis address");
        }

        issueUnfinishedDigitalAsset(transactionId);
    }

    private DigitalAssetMetadata getDigitalAssetMetadataFromGenesisAddress(String transactionId, CryptoAddress cryptoAssetGenesisAddress) throws CantGetDigitalAssetMetadataFromGenesisAddressException {
        try {
            String digitalAssetGenesisAddress = this.assetIssuingTransactionDao.getDigitalAssetGenesisAddressById(transactionId);
            cryptoAssetGenesisAddress.setAddress(digitalAssetGenesisAddress);
            setDigitalAssetGenesisAddress(transactionId, cryptoAssetGenesisAddress);
            return new DigitalAssetMetadata(this.digitalAsset);
        } catch (CantCheckAssetIssuingProgressException | CantExecuteQueryException e) {
            throw new CantGetDigitalAssetMetadataFromGenesisAddressException(e, "Getting digital asset metadata from genesis address", "Error in database transaction");
        } catch (UnexpectedResultReturnedFromDatabaseException e) {
            throw new CantGetDigitalAssetMetadataFromGenesisAddressException(e, "Getting digital asset metadata from genesis address", "Error: unexpected returned value from database");
        }
    }

    private void genesisObtainedOrSettled(String transactionId) throws GenesisObtainedOrSettledException {
        try {
            readDigitalAssetFile(transactionId);
            CryptoAddress cryptoAssetGenesisAddress = new CryptoAddress();
            DigitalAssetMetadata digitalAssetMetadata = getDigitalAssetMetadataFromGenesisAddress(transactionId, cryptoAssetGenesisAddress);

            String digitalAssetHash = getDigitalAssetHash(digitalAssetMetadata, transactionId);
            sendBitcoins(cryptoAssetGenesisAddress, digitalAssetHash, transactionId);

            issueUnfinishedDigitalAsset(transactionId);
        } catch (CantReadDigitalAssetFileException e) {
            throw new GenesisObtainedOrSettledException(e, "Genesis obtained or settled", "Can't read digital asset file");
        } catch (CantGetDigitalAssetMetadataFromGenesisAddressException e) {
            throw new GenesisObtainedOrSettledException(e, "Genesis obtained or settled", "Can't get digital asset metadata from genesis address");
        } catch (CantPersistsGenesisTransactionException e) {
            throw new GenesisObtainedOrSettledException(e, "Genesis obtained or settled", "Error getting digital asset hash. Can't persist genesis transaction");
        } catch (UnexpectedResultReturnedFromDatabaseException e) {
            throw new GenesisObtainedOrSettledException(e, "Genesis obtained or settled", "Error getting digital asset hash. Unexpected returned value");
        } catch (CantSendGenesisAmountException e) {
            throw new GenesisObtainedOrSettledException(e, "Genesis obtained or settled", "Error sending bitcoins. Can't send genesis amount");
        }
    }

    private void sendingCrypto(String transactionId) throws SendingCryptoException {
        try {
            readDigitalAssetFile(transactionId);
            CryptoAddress cryptoAssetGenesisAddress = new CryptoAddress();
            DigitalAssetMetadata digitalAssetMetadata = getDigitalAssetMetadataFromGenesisAddress(transactionId, cryptoAssetGenesisAddress);

            String digitalAssetHashFromDatabase = this.assetIssuingTransactionDao.getDigitalAssetHashById(transactionId);
            String digitalAssetHash = digitalAssetMetadata.getDigitalAssetHash();
            if (!digitalAssetHash.equals(digitalAssetHashFromDatabase)) {
                this.assetIssuingTransactionDao.updateDigitalAssetTransactionStatus(transactionId, TransactionStatus.ISSUED_FAILED);
                throw new CantIssueDigitalAssetException("The hash recorded in database for this DigitalAsset " + transactionId + " is not equal to the generated:\n" +
                        "Hash from database: " + digitalAssetHashFromDatabase + "\n" +
                        "hash from DigitalAssetMetadada: " + digitalAssetHash);
            }
            String genesisTransaction = this.assetIssuingTransactionDao.getDigitalAssetGenesisTransactionById(transactionId);
            digitalAssetMetadata = setDigitalAssetGenesisTransaction(transactionId, genesisTransaction, digitalAssetMetadata);
            //We kept the DigitalAssetMetadata in DAMVault
            saveDigitalAssetMetadataInVault(digitalAssetMetadata, transactionId);
        } catch (CantReadDigitalAssetFileException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Error reading digital asset");
        } catch (CantGetDigitalAssetMetadataFromGenesisAddressException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Error getting digital asset metadata from genesis address");
        } catch (CantCheckAssetIssuingProgressException | CantExecuteQueryException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Error in database transaction");
        } catch (UnexpectedResultReturnedFromDatabaseException | CantGetAssetIssuerActorsException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Unexpected returned value");
        } catch (CantIssueDigitalAssetException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Can't issue digital asset. The digital asset obtained is different than generated");
        } catch (CantPersistsGenesisTransactionException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Can't persist genesis transaction");
        } catch (CantDeliverDigitalAssetToAssetWalletException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Digital asset is not complete");
        } catch (CantCreateDigitalAssetFileException e) {
            throw new SendingCryptoException(e, "Sending crypto", "Can't persist digital asset metadata file in local storage");
        }
    }

    /**
     * This is a test method, this process is changing.
     *
     * @param transactionId
     */
    private void issueUnfinishedDigitalAsset(String transactionId) {
        /***
         *Este método debe verificar el estatus de cada Asset y proceder de acuerdo a cada uno de ellos.
         * El objetivo es finalizar los digital assets, ya persistidos en base de datos, pero sin emitir.
         */
        try {
            TransactionStatus digitalAssetTransactionStatus = this.assetIssuingTransactionDao.getDigitalAssetTransactionStatus(transactionId);
            //Caso Forming_genesis
            if (digitalAssetTransactionStatus == TransactionStatus.FORMING_GENESIS) {
                formingGenesis(transactionId);
            } else if (digitalAssetTransactionStatus == TransactionStatus.GENESIS_OBTAINED || digitalAssetTransactionStatus == TransactionStatus.GENESIS_SETTLED || digitalAssetTransactionStatus == TransactionStatus.HASH_SETTLED) { //Caso Genesis_obtained o Genesis_settled
                //Se obtuvo la genesisAddress, se persisitió en bases de datos, se recrea el digital asset desde el archivo y se le setea la genesisAddress desde la base de datos
                genesisObtainedOrSettled(transactionId);
            } else if (digitalAssetTransactionStatus == TransactionStatus.SENDING_CRYPTO) {
                sendingCrypto(transactionId);
            }
        } catch (Exception e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    /**
     * This method persists the genesis address in database
     *
     * @param transactionId
     * @param genesisAddress
     * @throws CantPersistsGenesisAddressException
     */
    private synchronized void persistsGenesisAddress(String transactionId, String genesisAddress) throws CantPersistsGenesisAddressException {
        this.assetIssuingTransactionDao.persistGenesisAddress(transactionId, genesisAddress);
    }

    /**
     * This method sets the genesis address in database
     *
     * @param transactionID
     * @param genesisAddress
     * @throws CantExecuteQueryException
     * @throws UnexpectedResultReturnedFromDatabaseException
     */
    private synchronized void setDigitalAssetGenesisAddress(String transactionID, CryptoAddress genesisAddress) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        this.digitalAsset.setGenesisAddress(genesisAddress);
        this.assetIssuingTransactionDao.updateDigitalAssetTransactionStatus(transactionID, TransactionStatus.GENESIS_SETTLED);
    }

    /**
     * This method sets the genesis transaction in a digital asset metadata
     *
     * @param transactionID
     * @param genesisTransaction
     * @param digitalAssetMetadata
     * @return
     * @throws CantPersistsGenesisTransactionException
     * @throws UnexpectedResultReturnedFromDatabaseException
     */
    private DigitalAssetMetadata setDigitalAssetGenesisTransaction(String transactionID, String genesisTransaction, DigitalAssetMetadata digitalAssetMetadata) throws CantPersistsGenesisTransactionException, UnexpectedResultReturnedFromDatabaseException, CantGetAssetIssuerActorsException {
        digitalAssetMetadata.addNewTransaction(genesisTransaction, null);
        digitalAssetMetadata.setLastOwner(actorAssetIssuerManager.getActorAssetIssuer());
        this.assetIssuingTransactionDao.persistGenesisTransaction(transactionID, genesisTransaction);
        return digitalAssetMetadata;
    }

    private void getDigitalAssetGenesisAddressByUUID(String transactionId) throws CantPersistsGenesisAddressException, CantGetGenesisAddressException {
        CryptoAddress genesisAddress = requestGenesisAddress();
        persistsGenesisAddress(transactionId, genesisAddress.getAddress());
    }

    /**
     * This method persists the digital asset metadata in plugin vault
     *
     * @param digitalAssetMetadata
     * @param transactionId
     * @throws CantCheckAssetIssuingProgressException
     * @throws CantDeliverDigitalAssetToAssetWalletException
     * @throws CantExecuteQueryException
     * @throws UnexpectedResultReturnedFromDatabaseException
     * @throws CantCreateDigitalAssetFileException
     */
    //TODO improve exceptions handling
    private void saveDigitalAssetMetadataInVault(DigitalAssetMetadata digitalAssetMetadata, String transactionId) throws CantCheckAssetIssuingProgressException, CantDeliverDigitalAssetToAssetWalletException, CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException, CantCreateDigitalAssetFileException {
        if (!isDigitalAssetComplete(digitalAsset, digitalAssetMetadata)) {
            throw new CantDeliverDigitalAssetToAssetWalletException("Cannot deliver the digital asset - is not complete:" + digitalAssetMetadata);
        }
        this.assetIssuingTransactionDao.updateDigitalAssetTransactionStatus(transactionId, TransactionStatus.ISSUING);
        this.digitalAssetIssuingVault.persistDigitalAssetMetadataInLocalStorage(digitalAssetMetadata, transactionId);
    }

    /**
     * This method register the genesis address in crypto address book.
     *
     * @param genesisAddress
     * @throws CantRegisterCryptoAddressBookRecordException
     */
    private synchronized void registerGenesisAddressInCryptoAddressBook(CryptoAddress genesisAddress) throws CantRegisterCryptoAddressBookRecordException {
        //TODO: solicitar los publickeys de los actors, la publicKey de la wallet
        //I'm gonna harcode the actors publicKey
        this.cryptoAddressBookManager.registerCryptoAddress(genesisAddress,
                this.intraActorPublicKey,
                Actors.INTRA_USER,
                this.actorAssetIssuerPublicKey,
                Actors.DAP_ASSET_ISSUER,
                Platforms.DIGITAL_ASSET_PLATFORM,
                VaultType.CRYPTO_ASSET_VAULT,
                CryptoCurrencyVault.BITCOIN_VAULT.getCode(),
                this.walletPublicKey,
                ReferenceWallet.BASIC_WALLET_BITCOIN_WALLET);
    }

    private String getDigitalAssetHash(DigitalAssetMetadata digitalAssetMetadata, String transactionId) throws CantPersistsGenesisTransactionException, UnexpectedResultReturnedFromDatabaseException {
        String digitalAssetHash = digitalAssetMetadata.getDigitalAssetHash();
        this.assetIssuingTransactionDao.persistDigitalAssetHash(transactionId, digitalAssetHash);
        return digitalAssetHash;
    }

    /**
     * This method send the genesis amount from the vault to the asset vault. This process is done by Outgoing Intra actor plugin
     *
     * @param genesisAddress
     * @param digitalAssetHash
     * @param transactionId
     * @return
     * @throws CantSendGenesisAmountException
     */
    private synchronized void sendBitcoins(CryptoAddress genesisAddress, String digitalAssetHash, String transactionId) throws CantSendGenesisAmountException {
        try {
            this.assetIssuingTransactionDao.updateDigitalAssetTransactionStatus(transactionId, TransactionStatus.SENDING_CRYPTO);
            this.assetIssuingTransactionDao.updateDigitalAssetCryptoStatusByTransactionHash(digitalAssetHash, CryptoStatus.PENDING_SUBMIT);
            UUID outgoingId = this.outgoingIntraActorManager.getTransactionManager().sendCrypto(this.walletPublicKey,
                    genesisAddress,
                    digitalAsset.getGenesisAmount(),
                    digitalAssetHash,
                    digitalAsset.getDescription(),
                    intraActorPublicKey,
                    actorAssetIssuerPublicKey,
                    Actors.INTRA_USER,
                    Actors.DAP_ASSET_ISSUER,
                    ReferenceWallet.BASIC_WALLET_BITCOIN_WALLET,
                    true,
                    BlockchainNetworkType.REG_TEST);
            this.assetIssuingTransactionDao.persistOutgoingIntraActorUUID(transactionId, outgoingId);
            this.assetIssuingTransactionDao.updateTransactionProtocolStatusByTransactionId(transactionId, ProtocolStatus.TO_BE_NOTIFIED);
        } catch (CantExecuteQueryException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "Cannot update the database: " + transactionId);
        } catch (UnexpectedResultReturnedFromDatabaseException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "Unexpected result reading database: " + transactionId);
        } catch (OutgoingIntraActorInsufficientFundsException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "The balance is insufficient to deliver the genesis amount: " + this.digitalAsset.getGenesisAmount());
        } catch (CantGetOutgoingIntraActorTransactionManagerException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "Asset Issuing plugin cannot get the OutgoingIntraUserTransactionManager");
        } catch (OutgoingIntraActorCantSendFundsExceptions exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "The sendToAddress is invalid: " + genesisAddress);
        } catch (CantCheckAssetIssuingProgressException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "Cannot update the transaction CryptoStatus");
        } catch (CantPersistsTransactionUUIDException exception) {
            throw new CantSendGenesisAmountException(exception, "Sending the genesis amount to Asset Wallet", "Cannot persists Transaction UUID");
        }
    }

    /**
     * This method create a Digital asset transaction. This process includes the Digital asset metadata creation to the crypto currency sending process.
     *
     * @throws CantCreateDigitalAssetTransactionException
     * @throws CantIssueDigitalAssetException
     * @throws CantDeliverDigitalAssetToAssetWalletException
     */
    private void createDigitalAssetCryptoTransaction() throws CantCreateDigitalAssetTransactionException, CantIssueDigitalAssetException, CantDeliverDigitalAssetToAssetWalletException, CantCheckAssetIssuingProgressException {

        DigitalAssetMetadata digitalAssetMetadata = null;
        try {
            //Assign internal UUID
            UUID transactionUUID = generateTransactionUUID();
            String transactionId = transactionUUID.toString();
            //Check the available balance
            checkCryptoWalletBalance();
            //Request the genesisAddress
            CryptoAddress genesisAddress = requestGenesisAddress();
            //persist the genesisAddress in database
            persistsGenesisAddress(transactionId, genesisAddress.getAddress());
            //Register genesisAddress in AddressBook
            registerGenesisAddressInCryptoAddressBook(genesisAddress);
            //Set the genesisAddres genesisAddress
            setDigitalAssetGenesisAddress(transactionId, genesisAddress);
            //create the digitalAssetMetadata
            digitalAssetMetadata = new DigitalAssetMetadata(this.digitalAsset);
            //Get the digital asset metadata hash
            String digitalAssetHash = getDigitalAssetHash(digitalAssetMetadata, transactionId);
            //LOG.info("MAP_DIGITAL ASSET FULL: "+this.digitalAsset);
            //BTC Sending
            sendBitcoins(genesisAddress, digitalAssetHash, transactionId);
            saveDigitalAssetMetadataInVault(digitalAssetMetadata, transactionId);
        } catch (CantPersistsGenesisAddressException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.DATABASE_EXCEPTION);
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot persists the Digital Asset genesis Address in database");
        } catch (CantExecuteQueryException | UnexpectedResultReturnedFromDatabaseException exception) {
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot update the Digital Asset Transaction Status in database");
        } catch (CantPersistsGenesisTransactionException exception) {
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot persists the Digital Asset genesis transaction in database");
        } catch (CantRegisterCryptoAddressBookRecordException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.WALLET_EXCEPTION);
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot register the Digital Asset genesis transaction in address book");
        } catch (CantGetGenesisAddressException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.WALLET_EXCEPTION);
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot get the Digital Asset genesis address from asset vault");
        } catch (CantPersistDigitalAssetException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.DATABASE_EXCEPTION);
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot persists the Digital Asset genesis transaction id in database");
        } catch (CantSendGenesisAmountException exception) {
            //GOT IT.
            throw new CantCreateDigitalAssetTransactionException(exception, "Issuing a new Digital Asset", "Cannot send the Digital Asset genesis amount");
        } catch (CantCreateDigitalAssetFileException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.FILESYSTEM_EXCEPTION);
            throw new CantDeliverDigitalAssetToAssetWalletException(exception, "Issuing a new Digital Asset", "Cannot kept the DigitalAssetMetadata in DigitalAssetIssuingVault");
        } catch (CantCheckAssetIssuingProgressException exception) {
            throw new CantDeliverDigitalAssetToAssetWalletException(exception, "Cannot deliver the digital asset:" + digitalAssetMetadata, "Unexpected result in database");
        } catch (CantCalculateBalanceException | CantLoadWalletException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.WALLET_EXCEPTION);
            throw new CantIssueDigitalAssetException(exception, "Issuing a new Digital Asset", "Cannot get the wallet available balance");
        } catch (CryptoWalletBalanceInsufficientException exception) {
            this.assetIssuingTransactionDao.updateDigitalAssetIssuingStatus(digitalAsset.getPublicKey(), IssuingStatus.INSUFFICIENT_FONDS);
            throw new CantIssueDigitalAssetException(exception, "Issuing a new Digital Asset", "The crypto balance is insufficient");
        }

    }

}