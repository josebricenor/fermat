package com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.holders;

import android.view.View;
import android.widget.CheckBox;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.SquareImageView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;

/**
 * Created by francisco on 14/10/15.
 */
public class IssuerViewHolder extends FermatViewHolder {

    public CheckBox connect;
    public SquareImageView thumbnail;
    public FermatTextView name;
    public FermatTextView status;
    public FermatTextView extendedPublicKey;

    /**
     * Constructor
     *
     * @param itemView
     */
    public IssuerViewHolder(View itemView) {
        super(itemView);
        thumbnail = (SquareImageView) itemView.findViewById(R.id.profile_image);
        name = (FermatTextView) itemView.findViewById(R.id.community_name);
        connect = (CheckBox) itemView.findViewById(R.id.connect);

        status = (FermatTextView) itemView.findViewById(R.id.status);
        extendedPublicKey = (FermatTextView) itemView.findViewById(R.id.extendedPublicKey);

    }
}
