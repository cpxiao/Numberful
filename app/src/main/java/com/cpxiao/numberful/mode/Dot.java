package com.cpxiao.numberful.mode;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/08/22.
 */

public class Dot extends Sprite {

    private int mIndexX;
    private int mIndexY;

    private boolean isSelected = false;
    private long mNumber;
    private int mBgColor;
    private int mTextColor;

    private RectF mDrawRectF = new RectF();

    Dot(Build build) {
        super(build);
        this.mIndexX = build.indexX;
        this.mIndexY = build.indexY;
    }

    public int getIndexX() {
        return mIndexX;
    }

    public int getIndexY() {
        return mIndexY;
    }

    public void setNumber(long number) {
        mNumber = number;
    }

    public long getNumber() {
        return mNumber;
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private float getR() {
        RectF rectF = getSpriteRectF();
        float maxR = 0.5F * Math.min(rectF.width(), rectF.height());
        long frame = getFrame();
        if (frame < 10) {
            return maxR * frame / 10;
        } else {
            return maxR;
        }
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        float r = getR();
        drawSmallCircle(canvas, paint, r);
        drawNumber(canvas, paint);
    }

    private void drawSmallCircle(Canvas canvas, Paint paint, float r) {
        paint.setColor(mBgColor);
        paint.setAlpha(255);
        drawRoundRect(canvas, paint, r);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float r) {
        float cX = getCenterX();
        float cY = getCenterY();
        float rXY = 0.18F * getWidth();
        mDrawRectF.set(cX - r, cY - r, cX + r, cY + r);
        canvas.drawRoundRect(mDrawRectF, rXY, rXY, paint);
    }

    private void drawNumber(Canvas canvas, Paint paint) {
        paint.setColor(mTextColor);

        String msg = mNumber + "";
        float textSize;
        if (msg.length() <= 2) {
            textSize = 0.5F * getWidth();
        } else if (msg.length() == 3) {
            textSize = 0.4F * getWidth();
        } else {
            textSize = 0.35F * getWidth();
        }
        paint.setTextSize(textSize);
        canvas.drawText(msg, getCenterX(), getCenterY() + 0.35F * textSize, paint);
    }


    public static class Build extends Sprite.Build {
        private int indexX;
        private int indexY;

        public Build setIndexX(int indexX) {
            this.indexX = indexX;
            return this;
        }

        public Build setIndexY(int indexY) {
            this.indexY = indexY;
            return this;
        }

        public Dot build() {
            return new Dot(this);
        }
    }
}
