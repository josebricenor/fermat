package com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.wizard_pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatCheckBox;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankAccountNumber;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.interfaces.CryptoBrokerIdentity;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.exceptions.CantCreateCryptoBrokerWalletException;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.exceptions.CryptoBrokerWalletNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWallet;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.setting.CryptoBrokerWalletAssociatedSetting;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.setting.CryptoBrokerWalletSettingSpread;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletModuleManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletPreferenceSettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.exceptions.CantListWalletsException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.InstalledWallet;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters.SingleDeletableItemAdapter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters.WalletsAdapter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.common.SimpleListDialogFragment;
import com.bitdubai.reference_wallet.crypto_broker_wallet.session.CryptoBrokerWalletSession;
import com.bitdubai.reference_wallet.crypto_broker_wallet.util.InputDialogCBP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.widget.Toast.makeText;

/**
 * Created by nelson on 22/12/15.
 */
public class WizardPageSetMerchandisesFragment extends AbstractFermatFragment
        implements SingleDeletableItemAdapter.OnDeleteButtonClickedListener<InstalledWallet>, DialogInterface.OnDismissListener {

    // Constants
    private static final String TAG = "WizardPageSetMerchand";

    private List<InstalledWallet> stockWallets;
    private Map<String, FiatCurrency> bankCurrencies;
    private Map<String, String> bankAccounts;
    private List<CryptoBrokerIdentity> identities;
    private CryptoBrokerIdentity selectedIdentity;
    private CryptoBrokerWalletPreferenceSettings walletSettings;

    // Fermat Managers
    private CryptoBrokerWalletManager walletManager;
    private ErrorManager errorManager;
    private WalletsAdapter adapter;
    private RecyclerView recyclerView;
    private FermatTextView emptyView;
    private LinearLayout container;

    private CryptoBrokerWalletManager cryptoBrokerWalletManager;


    public static WizardPageSetMerchandisesFragment newInstance() {
        return new WizardPageSetMerchandisesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        stockWallets = new ArrayList<>();
        bankCurrencies = new HashMap<>();
        bankAccounts = new HashMap<>();

        try {
            CryptoBrokerWalletModuleManager moduleManager = ((CryptoBrokerWalletSession) appSession).getModuleManager();
            walletManager = moduleManager.getCryptoBrokerWallet(appSession.getAppPublicKey());
            errorManager = appSession.getErrorManager();

            //identities = getMoreDataAsync(FermatRefreshTypes.NEW, 0);
            //Obtain walletSettings or create new wallet settings if first time opening wallet
            walletSettings = null;
            try {
                walletSettings = moduleManager.getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey());
            } catch (Exception e) {
                walletSettings = null;
            }

            if (walletSettings == null) {
                walletSettings = new CryptoBrokerWalletPreferenceSettings();
                walletSettings.setIsPresentationHelpEnabled(true);
                try {
                    moduleManager.getSettingsManager().persistSettings(appSession.getAppPublicKey(), walletSettings);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            if (walletManager.getListOfIdentities().isEmpty()) {
                PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                        .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION)
                        .setBannerRes(R.drawable.banner_crypto_broker)
                        .setIconRes(R.drawable.crypto_broker)
                        .setBody(R.string.cbw_wizard_merchandise_dialog_body)
.setSubTitle(R.string.cbw_wizard_merchandise_dialog_sub_title) // TODO para franklin (revisar esto, esta es la linea que venia en tu commit): .setSubTitle("This is a simple wallet for exchange Merchandise. " + identities)
                        .setTextFooter(R.string.cbw_wizard_merchandise_dialog_footer)
                        .build();
                presentationDialog.setOnDismissListener(this);

                boolean showDialog;
                try {
                    //CryptoBrokerWalletModuleManager moduleManager = ((CryptoBrokerWalletSession) appSession).getModuleManager();
                    showDialog = moduleManager.getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey()).isHomeTutorialDialogEnabled();
                    if (showDialog) {
                        presentationDialog.show();
                    }
                } catch (FermatException e) {
                    makeText(getActivity(), "Oops! recovering from system error", Toast.LENGTH_SHORT).show();
                }

                //Buscar la identidad y asociarla
                selectedIdentity = walletManager.getListOfIdentities().get(0);
                //Llamar a este metodo getListOfIdentities y trarse el valor de la posicion 0 y asiganrlo a selectedIdentity
                if (selectedIdentity != null) {
                    walletManager.associateIdentity(selectedIdentity.getPublicKey());
                    //changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES, appSession.getAppPublicKey());
                } else {
                    Toast.makeText(WizardPageSetMerchandisesFragment.this.getActivity(), R.string.cbw_select_identity_warning_msg, Toast.LENGTH_LONG).show();
                }
                //presentationDialog.show();
            } else {
                //Buscar la identidad y asociarla
                selectedIdentity = walletManager.getListOfIdentities().get(0);
                //Llamar a este metodo getListOfIdentities y trarse el valor de la posicion 0 y asiganrlo a selectedIdentity
                if (selectedIdentity != null) {
                    walletManager.associateIdentity(selectedIdentity.getPublicKey());
                    //changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES, appSession.getAppPublicKey());
                } else {
                    Toast.makeText(WizardPageSetMerchandisesFragment.this.getActivity(), R.string.cbw_select_identity_warning_msg, Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                .setSubTitle(R.string.cbw_crypto_broker_wallet_merchandises_subTitle)
                .setBody(R.string.cbw_crypto_broker_wallet_merchandises_body)
                .setTextFooter(R.string.cbw_crypto_broker_wallet_merchandises_footer)
                .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                .setBannerRes(R.drawable.banner_crypto_broker)
                .setIconRes(R.drawable.crypto_broker)
                .build();

        presentationDialog.show();
        final View layout = inflater.inflate(R.layout.cbw_wizard_step_set_merchandises, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.cbw_selected_stock_wallets_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new WalletsAdapter(getActivity(), stockWallets);
        adapter.setDeleteButtonListener(this);
        recyclerView.setAdapter(adapter);

        emptyView = (FermatTextView) layout.findViewById(R.id.cbw_selected_stock_wallets_empty_view);


        final View cryptoButton = layout.findViewById(R.id.cbw_select_crypto_wallets);
        cryptoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWalletsDialog(Platforms.CRYPTO_CURRENCY_PLATFORM);
            }
        });

        final View bankButton = layout.findViewById(R.id.cbw_select_bank_wallets);
        bankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWalletsDialog(Platforms.BANKING_PLATFORM);
            }
        });

        final View cashButton = layout.findViewById(R.id.cbw_select_cash_wallets);
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWalletsDialog(Platforms.CASH_PLATFORM);
            }
        });


        final View nextStepButton = layout.findViewById(R.id.cbw_next_step_button);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettingAndGoNextStep();
            }
        });

        verifyIfWalletConfigured();

        return layout;
    }

    private void showWalletsDialog(final Platforms platform) {
        try {
            List<InstalledWallet> installedWallets = walletManager.getInstallWallets();
            List<InstalledWallet> filteredList = new ArrayList<>();

            for (InstalledWallet wallet : installedWallets) {
                if (wallet.getPlatform().equals(platform))
                    filteredList.add(wallet);
            }

            final SimpleListDialogFragment<InstalledWallet> dialogFragment = new SimpleListDialogFragment<>();
            dialogFragment.configure("Select a Wallet", filteredList);
            dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<InstalledWallet>() {
                @Override
                public void onItemSelected(InstalledWallet selectedItem) {
                    if (!platform.equals(Platforms.BANKING_PLATFORM)) {

                        if (!containWallet(selectedItem)) {
                            stockWallets.add(selectedItem);
                            adapter.changeDataSet(stockWallets);
                            showOrHideNoSelectedWalletsView();
                        }

                    } else {
                        showBankAccountsDialog(selectedItem);
                    }
                }
            });

            dialogFragment.show(getFragmentManager(), "WalletsDialog");

        } catch (CantListWalletsException ex) {
            Toast.makeText(WizardPageSetMerchandisesFragment.this.getActivity(), "Oops a error occurred...", Toast.LENGTH_SHORT).show();

            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null) {
                errorManager.reportUnexpectedWalletException(
                        Wallets.CBP_CRYPTO_BROKER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                        ex);
            }
        }
    }

    private void verifyIfWalletConfigured() {

        new Handler().postDelayed(new Runnable() {
            boolean walletConfigured;

            @Override
            public void run() {
                try {
                    walletConfigured = walletManager.isWalletConfigured(appSession.getAppPublicKey());

                } catch (Exception ex) {
                    Object data = appSession.getData(CryptoBrokerWalletSession.CONFIGURED_DATA);
                    walletConfigured = (data != null);

                    if (errorManager != null)
                        errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                                UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);

                }

                if (walletConfigured) {
                    changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME, appSession.getAppPublicKey());
                }
            }
        }, 500);
    }

    private void showBankAccountsDialog(final InstalledWallet selectedWallet) {
        try {
            List<BankAccountNumber> accounts = walletManager.getAccounts(selectedWallet.getWalletPublicKey());
            if (!accounts.isEmpty()) {

                SimpleListDialogFragment<BankAccountNumber> accountsDialog = new SimpleListDialogFragment<>();
                accountsDialog.configure("Select an Account", accounts);
                accountsDialog.setListener(new SimpleListDialogFragment.ItemSelectedListener<BankAccountNumber>() {

                    @Override
                    public void onItemSelected(BankAccountNumber selectedAccount) {
                        FiatCurrency currency = selectedAccount.getCurrencyType();
                        if (currency == null) {
                            InputDialogCBP inputDialogCBP = new InputDialogCBP(getActivity(), appSession, null);
                            inputDialogCBP.DialogType(2);
                            inputDialogCBP.show();

                        }
                        String account = selectedAccount.getAccount();
                        bankCurrencies.put(selectedWallet.getWalletPublicKey(), currency);
                        bankAccounts.put(selectedWallet.getWalletPublicKey(), account);
                        if (!

                                containWallet(selectedWallet)

                                )

                        {
                            stockWallets.add(selectedWallet);
                            adapter.changeDataSet(stockWallets);
                            showOrHideNoSelectedWalletsView();

                        }
                    }

                });
                accountsDialog.show(getFragmentManager(), "accountsDialog");
            } else {

                InputDialogCBP inputDialogCBP = new InputDialogCBP(getActivity(), appSession, null);
                inputDialogCBP.DialogType(1);
                inputDialogCBP.show();
            }

        } catch (FermatException ex) {
            Toast.makeText(WizardPageSetMerchandisesFragment.this.getActivity(), "Oops a error occurred...", Toast.LENGTH_SHORT).show();

            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null) {
                errorManager.reportUnexpectedWalletException(
                        Wallets.CBP_CRYPTO_BROKER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                        ex);
            }
        }
    }

    private void saveSettingAndGoNextStep() {

        if (stockWallets.isEmpty()) {
            Toast.makeText(getActivity(), R.string.cbw_select_stock_wallets_warning_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {


            for (InstalledWallet wallet : stockWallets) {
                String walletPublicKey = wallet.getWalletPublicKey();
                Platforms platform = wallet.getPlatform();

                CryptoBrokerWalletAssociatedSetting associatedSetting = walletManager.newEmptyCryptoBrokerWalletAssociatedSetting();
                associatedSetting.setBrokerPublicKey(appSession.getAppPublicKey());
                associatedSetting.setId(UUID.randomUUID());
                associatedSetting.setWalletPublicKey(walletPublicKey);
                associatedSetting.setPlatform(platform);

                if (platform.equals(Platforms.BANKING_PLATFORM)) {
                    associatedSetting.setCurrencyType(CurrencyType.BANK_MONEY);
                    associatedSetting.setMerchandise(bankCurrencies.get(walletPublicKey));
                    associatedSetting.setBankAccount(bankAccounts.get(walletPublicKey));

                } else if (platform.equals(Platforms.CRYPTO_CURRENCY_PLATFORM)) {
                    associatedSetting.setCurrencyType(CurrencyType.CRYPTO_MONEY);
                    associatedSetting.setMerchandise(wallet.getCryptoCurrency());

                } else {
                    FiatCurrency cashCurrency = walletManager.getCashCurrency(walletPublicKey);
                    associatedSetting.setMerchandise(cashCurrency);
                    associatedSetting.setCurrencyType(CurrencyType.CASH_ON_HAND_MONEY);
                }

                walletManager.saveWalletSettingAssociated(associatedSetting, appSession.getAppPublicKey());
            }

        } catch (FermatException ex) {
            Toast.makeText(WizardPageSetMerchandisesFragment.this.getActivity(), "Oops a error occurred...", Toast.LENGTH_SHORT).show();

            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null) {
                errorManager.reportUnexpectedWalletException(
                        Wallets.CBP_CRYPTO_BROKER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                        ex);
            }
        }

        //TODO: agregar esta instruccion en el try/catch cuando funcione el saveSettingSpread
        changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS, appSession.getAppPublicKey());
    }

    @Override
    public void deleteButtonClicked(InstalledWallet data, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.cbw_delete_wallet_dialog_title).setMessage(R.string.cbw_delete_wallet_dialog_msg);
        builder.setPositiveButton(R.string.cbw_delete_caps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stockWallets.remove(position);
                adapter.changeDataSet(stockWallets);
                showOrHideNoSelectedWalletsView();
            }
        });
        builder.setNegativeButton(R.string.cbw_cancel_caps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.show();
    }

    private void showOrHideNoSelectedWalletsView() {
        if (stockWallets.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private boolean containWallet(InstalledWallet selectedWallet) {
        if (stockWallets.isEmpty())
            return false;

        for (InstalledWallet wallet : stockWallets) {
            String walletPublicKey = wallet.getWalletPublicKey();
            String selectedWalletPublicKey = selectedWallet.getWalletPublicKey();

            if (walletPublicKey.equals(selectedWalletPublicKey))
                return true;
        }

        return false;
    }

    /**
     * This method will be invoked when the dialog is dismissed.
     *
     * @param dialog The dialog that was dismissed will be passed into the
     *               method.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
