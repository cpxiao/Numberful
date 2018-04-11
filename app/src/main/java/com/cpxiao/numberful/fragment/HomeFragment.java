package com.cpxiao.numberful.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.RateAppUtils;
import com.cpxiao.androidutils.library.utils.ShareAppUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.numberful.mode.extra.GridSize;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/8/23.
 * @version cpxiao on 2017/9/25. 改为继承BaseZAdsFragment
 */

public class HomeFragment extends BaseZAdsFragment implements View.OnClickListener {

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_HOME);

        Button buttonEasy = view.findViewById(R.id.play);
        ImageView buttonRateApp = view.findViewById(R.id.rate_app);
        ImageButton buttonShare = view.findViewById(R.id.share);
        ImageButton buttonBestScore = view.findViewById(R.id.best_score);
        ImageButton buttonSettings = view.findViewById(R.id.settings);

        buttonEasy.setOnClickListener(this);
        buttonRateApp.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonBestScore.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Context context = getHoldingActivity();
        int gridSizeX = PreferencesUtils.getInt(context, GridSize.GRID_SIZE_X, GridSize.DEFAULT[0]);
        int gridSizeY = PreferencesUtils.getInt(context, GridSize.GRID_SIZE_Y, GridSize.DEFAULT[1]);

        Bundle bundle = new Bundle();
        if (id == R.id.play) {
            bundle.putInt(GridSize.GRID_SIZE_X, gridSizeX);
            bundle.putInt(GridSize.GRID_SIZE_Y, gridSizeY);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.rate_app) {
            Toast.makeText(context, "Thanks for rating us.", Toast.LENGTH_SHORT).show();
            RateAppUtils.rate(context);
        } else if (id == R.id.share) {
            String msg = getString(R.string.share_msg) + "\n" +
                    getString(R.string.app_name) + "\n" +
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            ShareAppUtils.share(context, getString(R.string.share), msg);
        } else if (id == R.id.best_score) {
            Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
            //            showBestScoreDialog(context);
        } else if (id == R.id.settings) {
            Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
        }
    }


}
