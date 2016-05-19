package com.bitdubai.android_core.app.common.version_1.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bitdubai.android_core.app.FermatActivity;
import com.bitdubai.fermat_android_api.constants.ApplicationConstants;
import com.bitdubai.android_core.app.common.version_1.adapters.TabsPagerAdapter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.FermatBundle;

import java.lang.ref.WeakReference;

/**
 * Created by mati on 2016.05.03..
 */
public class UpdateViewReceiver extends BroadcastReceiver {

    public static final String INTENT_NAME = "update_view_receiver";

    private final WeakReference<FermatActivity> weakReference;

    public UpdateViewReceiver(FermatActivity fermatActivity) {
        this.weakReference = new WeakReference<FermatActivity>(fermatActivity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i(INTENT_NAME,"Notification received!");
        String appPublicKey = intent.getStringExtra(ApplicationConstants.INTENT_DESKTOP_APP_PUBLIC_KEY);
        String code = intent.getStringExtra(ApplicationConstants.INTENT_EXTRA_DATA);
//        Log.i(INTENT_NAME,"AppPublicKey: "+appPublicKey);
//        Log.i(INTENT_NAME,"Code received:"+code);
        updateView(appPublicKey,code);

    }

    private void updateView(FermatBundle bundle) {
        if(weakReference.get() instanceof FermatActivity) {
            TabsPagerAdapter adapter = ((FermatActivity)weakReference.get()).getAdapter();
            if (adapter != null) {
                for (AbstractFermatFragment fragment : adapter.getLstCurrentFragments()) {
                    fragment.onUpdateView(bundle);
                    fragment.onUpdateViewUIThred(bundle);
                }
            }
        }
    }

    private void updateView(String code){
        if(weakReference.get() instanceof FermatActivity) {
            TabsPagerAdapter adapter = ((FermatActivity) weakReference.get()).getAdapter();
            if (adapter != null) {
                for (AbstractFermatFragment fragment : adapter.getLstCurrentFragments()) {
                    fragment.onUpdateView(code);
                    fragment.onUpdateViewUIThred(code);
                }
            }
        }
    }

    //TODO: esto va a ser del codigo de la app, el paquete del intent
    private void updateView(String appCode,String code){
        if(weakReference.get() instanceof FermatActivity) {
            TabsPagerAdapter adapter = ((FermatActivity) weakReference.get()).getAdapter();
            if (adapter != null) {
                for (AbstractFermatFragment fragment : adapter.getLstCurrentFragments()) {
                    fragment.onUpdateViewHandler(appCode, code);
                    fragment.onUpdateView(code);
                    fragment.onUpdateViewUIThred(code);
                }
            }
        }
    }

    public void clear() {
        weakReference.clear();
    }
}
