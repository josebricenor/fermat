package com.bitdubai.fermat_android_api.engine;

/**
 * Created by Matias Furszyfer on 2015.12.15..
 */
public interface FermatApplicationSession<S> {

    S getFermatSystem();

    FermatApplicationCaller getApplicationManager();


}
