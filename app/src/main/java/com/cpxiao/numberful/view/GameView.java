package com.cpxiao.numberful.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.gamelib.mode.common.SpriteControl;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
import com.cpxiao.numberful.mode.Dot;
import com.cpxiao.numberful.mode.extra.GridSize;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author cpxiao on 2017/08/22.
 */

public class GameView extends BaseSurfaceViewFPS {

    /*参数*/
    private int mGridSizeX = GridSize.DEFAULT[0];
    private int mGridSizeY = GridSize.DEFAULT[1];

    private int mLevel = 1;
    private long mSum = 0;
    private long mScore = 0;

    private Dot[][] mDotGrid;

    private CopyOnWriteArrayList<Dot> mSelectedDotList = new CopyOnWriteArrayList<>();

    private Dot mLastDot = null;

    public GameView(Context context, int gridSizeX, int gridSizeY) {
        super(context);
        mGridSizeX = gridSizeX;
        mGridSizeY = gridSizeY;
    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void timingLogic() {

    }

    private boolean isDotGridIllegal() {
        return mDotGrid == null || mDotGrid.length != mGridSizeY || mDotGrid[0].length != mGridSizeX;
    }

    @Override
    protected void initWidget() {
        init();
        //        setBgColor(getResources().getColor(R.color.colorBg));
        //        setShader(new LinearGradient(0, 0, 0, getHeight(), getResources().getColor(R.color.colorBgTop), getResources().getColor(R.color.colorBgBottom), Shader.TileMode.CLAMP));
        //        setShader(new LinearGradient(0, 0, getWidth(), getHeight(), Color.BLUE, Color.CYAN, Shader.TileMode.CLAMP));
        setBgBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.activity_bg));
    }

    public void init() {
        initDotGrid();
        setupDotGridNumber(mLevel);
        mScore = 0;
        mLevel = 1;
    }

    private void initDotGrid() {
        float marginLR = 0.04F * mViewWidth;
        float marginT = Resources.getSystem().getDisplayMetrics().density * 80;
        float marginB = Resources.getSystem().getDisplayMetrics().density * 120;
        float dotWH = Math.min((mViewWidth - marginLR * 2) / mGridSizeX, (mViewHeight - marginT - marginB) / mGridSizeY);

        float paddingLR = 0.5F * (mViewWidth - marginLR * 2 - dotWH * mGridSizeX);
        float paddingTB = 0.5F * (mViewHeight - marginT - marginB - dotWH * mGridSizeY);

        mDotGrid = new Dot[mGridSizeY][mGridSizeX];
        for (int y = 0; y < mGridSizeY; y++) {
            for (int x = 0; x < mGridSizeX; x++) {
                float cX = marginLR + paddingLR + (x + 0.5F) * dotWH;
                float cY = marginT + paddingTB + (y + 0.5F) * dotWH;
                Dot dot = (Dot) new Dot.Build()
                        .setIndexX(x)
                        .setIndexY(y)
                        .setW(0.6F * dotWH)
                        .setH(0.6F * dotWH)
                        .centerTo(cX, cY)
                        .build();
                mDotGrid[y][x] = dot;
            }
        }
    }

    private void setupDotGridNumber(long sum) {
        if (isDotGridIllegal()) {
            return;
        }

        for (int y = 0; y < mGridSizeY; y++) {
            for (int x = 0; x < mGridSizeX; x++) {
                Dot dot = mDotGrid[y][x];
                dot.setFrame(0);
                long number = (long) (1 + Math.random() * (sum + 5));
                number = Math.random() < 0.3 ? -number : number;
                dot.setNumber(number);

            }
        }

        int indexX = (int) (Math.random() * (mGridSizeX - 1));
        int indexY = (int) (Math.random() * (mGridSizeY - 1));

        int indexX1 = (int) (indexX + Math.random() * (2));
        int indexY1 = (int) (indexY + Math.random() * (2));
        Dot dot = mDotGrid[indexY1][indexX1];
        long number = sum - mDotGrid[indexY][indexX].getNumber();
        if (number == 0) {
            mDotGrid[indexY + 1][indexX].setNumber(-mDotGrid[indexY][indexX + 1].getNumber());
        } else {
            dot.setNumber(number);
        }

    }

    @Override
    public void drawCache() {

        drawLinkLine(mCanvasCache, mPaint);

        drawDotGrid(mCanvasCache, mPaint);

//        drawScore(mCanvasCache, mPaint);

        drawLevel(mCanvasCache, mPaint);

        drawSum(mCanvasCache, mPaint);

    }

    private void drawLinkLine(Canvas canvas, Paint paint) {
        if (mSelectedDotList.size() < 2) {
            return;
        }
        paint.setStrokeWidth(0.02F * mViewWidth);
        paint.setColor(getResources().getColor(R.color.colorLine));

        for (int i = 0; i < mSelectedDotList.size() - 1; i++) {
            Dot dot0 = mSelectedDotList.get(i);
            Dot dot1 = mSelectedDotList.get(i + 1);
            canvas.drawLine(dot0.getCenterX(), dot0.getCenterY(), dot1.getCenterX(), dot1.getCenterY(), paint);
        }
    }

    private void drawDotGrid(Canvas canvas, Paint paint) {
        if (isDotGridIllegal()) {
            return;
        }
        for (int y = 0; y < mGridSizeY; y++) {
            for (int x = 0; x < mGridSizeX; x++) {
                Dot dot = mDotGrid[y][x];
                if (dot.getNumber() > 0) {
                    dot.setBgColor(getResources().getColor(R.color.colorDotBg));
                    dot.setTextColor(getResources().getColor(R.color.colorDotText));
                } else {
                    dot.setBgColor(getResources().getColor(R.color.colorDotBg_));
                    dot.setTextColor(getResources().getColor(R.color.colorDotText_));
                }
                if (dot.isSelected()) {
                    dot.setBgColor(getResources().getColor(R.color.colorSelectedDotBg));
                    dot.setTextColor(getResources().getColor(R.color.colorSelectedDotText));
                }
                dot.draw(canvas, paint);
            }
        }

    }


    private void drawScore(Canvas canvas, Paint paint) {
        paint.setColor(getResources().getColor(R.color.colorScore));
        paint.setTextSize(0.04F * mViewHeight);
        String scoreMsg = "" + mScore;
        canvas.drawText(scoreMsg, 0.8F * mViewWidth, 0.08F * mViewHeight, paint);
    }

    private void drawLevel(Canvas canvas, Paint paint) {
        paint.setColor(getResources().getColor(R.color.colorLevel));
        paint.setTextSize(0.06F * mViewHeight);
        String scoreMsg = "" + mLevel;
        canvas.drawText(scoreMsg, 0.5F * mViewWidth, 0.15F * mViewHeight, paint);
    }

    private void drawSum(Canvas canvas, Paint paint) {
        paint.setColor(getResources().getColor(R.color.colorSum));
        paint.setTextSize(0.02F * mViewHeight);
        String scoreMsg = "" + mSum;
        if (mSelectedDotList.size() < 2) {
            scoreMsg = getResources().getString(R.string.sum_target) + " " + mLevel;
        }
        canvas.drawText(scoreMsg, 0.5F * mViewWidth, 0.19F * mViewHeight, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSelectedDotList.size() <= 1) {
            mSum = 0;
        } else {
            mSum = 0;
            for (int i = 0; i < mSelectedDotList.size(); i++) {
                mSum += mSelectedDotList.get(i).getNumber();
            }
        }

        int action = event.getAction();
        float eventX = event.getX();
        float eventY = event.getY();

        if (action == MotionEvent.ACTION_UP) {
            if (mSum == mLevel) {
                // 加分
                mScore += mLevel * mSelectedDotList.size();

                // 下一关
                mLevel++;
                setupDotGridNumber(mLevel);
            }

            mSum = 0;
            mLastDot = null;
            for (Dot dot : mSelectedDotList) {
                dot.setSelected(false);
            }
            mSelectedDotList.clear();

            return true;
        }


        for (int y = 0; y < mGridSizeY; y++) {
            for (int x = 0; x < mGridSizeX; x++) {
                Dot dot = mDotGrid[y][x];
                if (SpriteControl.isClicked(dot, eventX, eventY)) {

                    // 添加第一个节点
                    if (mLastDot == null) {
                        dot.setSelected(true);
                        mSelectedDotList.add(dot);
                        mLastDot = dot;
                        // TODO 播放音效

                    } else if (dot != mLastDot) {

                        //如果重复了，就删除重复点之后添加的。
                        if (mSelectedDotList.contains(dot)) {
                            //找到重复点之后那个点的index
                            int index = mSelectedDotList.indexOf(dot) + 1;
                            if (index == mSelectedDotList.size() - 1) {

                                int size = mSelectedDotList.size();
                                for (int i = index; i < size; i++) {
                                    Dot deleteDot = mSelectedDotList.get(index);
                                    deleteDot.setSelected(false);
                                    mSelectedDotList.remove(deleteDot);
                                }
                                mLastDot = dot;
                                // TODO 播放音效

                            }

                        } else {

                            //如果当前节点与上一个节点不是邻居(即距离大于1)，则跳过不处理
                            if (!isNeighbor(dot, mLastDot)) {
                                return true;
                            }

                            //添加节点
                            dot.setSelected(true);
                            mSelectedDotList.add(dot);
                            mLastDot = dot;
                            // TODO 播放音效

                        }

                    }
                    return true;
                }
            }
        }

        return true;
        //        return super.onTouchEvent(event);
    }

    private boolean isNeighbor(Dot dot0, Dot dot1) {
        return dot0 != null && dot1 != null
                && Math.abs(dot0.getIndexX() - dot1.getIndexX()) <= 1
                && Math.abs(dot0.getIndexY() - dot1.getIndexY()) <= 1;
    }


}
