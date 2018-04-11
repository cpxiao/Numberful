package com.cpxiao.numberful.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.ThreadUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.gamelib.views.CountDownTextView;
import com.cpxiao.numberful.mode.extra.GridSize;
import com.cpxiao.numberful.mode.extra.Time;
import com.cpxiao.numberful.view.GameView;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/08/23.
 */

public class GameFragment extends BaseZAdsFragment {

    private CountDownTextView mCountDownTextView;

    private GameView mGameView;

    private long mTimeMillis = Time.TIME60;
    private int mGridSizeX = GridSize.DEFAULT[0];
    private int mGridSizeY = GridSize.DEFAULT[1];

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_GAME);

        mCountDownTextView = view.findViewById(R.id.countDownTextView);
        mCountDownTextView.setTimeMillis(mTimeMillis);
        mCountDownTextView.start();
        mCountDownTextView.setOnTimeUpListener(new CountDownTextView.OnTimeUpListener() {
            @Override
            public void timeUp() {
                showGameOverDialog();
            }
        });

        Bundle bundle;
        bundle = getArguments();
        if (bundle != null) {
            mGridSizeX = bundle.getInt(GridSize.GRID_SIZE_X, GridSize.DEFAULT[0]);
            mGridSizeY = bundle.getInt(GridSize.GRID_SIZE_Y, GridSize.DEFAULT[1]);
        }
        final LinearLayout layout = view.findViewById(R.id.layout_game);
        mGameView = new GameView(view.getContext(), mGridSizeX, mGridSizeY);

        layout.addView(mGameView);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownTextView != null) {
            mCountDownTextView.destroy();
        }
    }

    private void showGameOverDialog() {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new AlertDialog.Builder(getHoldingActivity())
                        .setTitle(R.string.time_up)
                        .setMessage(R.string.click_to_restart)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (mCountDownTextView != null) {
                                    mCountDownTextView.resetTime(mTimeMillis);
                                    mCountDownTextView.start();
                                }
                                if (mGameView != null) {
                                    mGameView.init();
                                }
                            }
                        })
                        .create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }


}
