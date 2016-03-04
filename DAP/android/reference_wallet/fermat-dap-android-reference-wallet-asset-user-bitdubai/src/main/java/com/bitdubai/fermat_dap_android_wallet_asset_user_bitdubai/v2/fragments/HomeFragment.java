package com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.expandableRecicler.ExpandableRecyclerAdapter;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletExpandableListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.R;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.sessions.AssetUserSession;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.util.CommonLogger;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.common.adapters.HomeIssuerItemExpandableAdapter;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.common.data.DataManager;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.Asset;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.GrouperItem;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.Issuer;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 2/24/16.
 */
public class HomeFragment extends FermatWalletExpandableListFragment<GrouperItem, AssetUserSession, ResourceProviderManager> implements FermatListItemListeners<Asset> {

    // Fermat Managers
    private ErrorManager errorManager;
//    SettingsManager<AssetUserSettings> settingsManager;

    // Data
    private DataManager dataManager;
    private List<GrouperItem> data;

    //UI
    private View noIssuersView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            dataManager = new DataManager(appSession.getModuleManager());
            errorManager = appSession.getErrorManager();
//            settingsManager = appSession.getModuleManager().getSettingsManager();

            data = (List) getMoreDataAsync(FermatRefreshTypes.NEW, 0);
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.DAP_ASSET_USER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        configureToolbar();
        noIssuersView = layout.findViewById(R.id.dap_v2_wallet_asset_user_home_no_assets);
        showOrHideNoAssetsView(data.isEmpty());
    }

    private void showOrHideNoAssetsView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            noIssuersView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noIssuersView.setVisibility(View.GONE);
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.dap_user_wallet__home_issuer_principal));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBottom(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.dap_user_wallet__home_issuer_principal));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_v2_wallet_asset_user_home;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.dap_v2_wallet_asset_user_home_swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_v2_wallet_asset_user_home_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                data = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(data);

                showOrHideNoAssetsView(data.isEmpty());
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public ExpandableRecyclerAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HomeIssuerItemExpandableAdapter(getActivity(), data, getResources());
            adapter.setChildItemFermatEventListeners(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    public List<GrouperItem> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<GrouperItem> data = new ArrayList<>();
        try {
            List<Issuer> issuers = dataManager.getIssuers();
            for (Issuer issuer : issuers) {
                GrouperItem<Asset, Issuer> item = new GrouperItem<>(issuer.getAssets(), false, issuer);
                data.add(item);
            }
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(
                        Wallets.DAP_ASSET_USER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                        ex);
        }
        return data;
    }

    @Override
    public void onItemClickListener(Asset data, int position) {
        appSession.setData("asset", data);
        changeActivity(Activities.DAP_WALLET_ASSET_USER_V2_DETAIL, appSession.getAppPublicKey());
    }

    @Override
    public void onLongItemClickListener(Asset data, int position) {

    }
}
