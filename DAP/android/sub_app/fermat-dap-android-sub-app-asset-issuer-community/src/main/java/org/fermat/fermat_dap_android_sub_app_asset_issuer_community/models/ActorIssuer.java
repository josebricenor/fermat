package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models;

import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;

/**
 * Actor Model
 */
public class ActorIssuer {

    public boolean selected;
    private ActorAssetIssuer record;

    public ActorIssuer() {
        super();
    }

    public ActorIssuer(ActorAssetIssuer record) {
        this.record = record;
    }

    public ActorAssetIssuer getRecord() {
        return record;
    }

    public void setRecord(AssetIssuerActorRecord record) {
        this.record = record;
    }
}
