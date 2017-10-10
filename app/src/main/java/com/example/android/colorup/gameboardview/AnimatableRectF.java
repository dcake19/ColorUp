package com.example.android.colorup.gameboardview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


public class AnimatableRectF extends RectF {

    private Integer mValue = 1;
    private int mSquareCornerRadius;
    private Paint mShapeForegroundPaint;
    private Paint mTextPaint;

    public AnimatableRectF() {
        super();
        setPaints();
    }

    public AnimatableRectF(float left, float top, float right, float bottom,int squareCornerRadius) {
        super(left, top, right, bottom);
        mSquareCornerRadius = squareCornerRadius;
        setPaints();
    }

    public AnimatableRectF(float left, float top, float right, float bottom,int value,int squareCornerRadius) {
        super(left, top, right, bottom);
        mSquareCornerRadius = squareCornerRadius;
        mValue = value;
        setPaints();
    }

    private void setPaints(){
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapeForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int textSize = (int) (0.7*(right - left));
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        setColor();
    }

    private void setColor(){
        int round = mValue/255;
        int red =  (47*mValue + round)%255;
        int green = (83*mValue + round)%255;
        int blue = (173*mValue + round)%255;

        int color = android.graphics.Color.rgb(red,green,blue);

        mShapeForegroundPaint.setColor(color);

        int textColor  = 0xff000000;
        if(red+green+blue <= 255) textColor = 0xffFFFFFF;
        mTextPaint.setColor(textColor);
    }

    public int getColor(){
        return mShapeForegroundPaint.getColor();
    }

    public int getNextColor(){
        int round = mValue/255;
        int red =  (47*(mValue+1)+ round)%255;
        int green = (83*(mValue+1)+ round)%255;
        int blue = (173*(mValue+1)+ round)%255;
        int color = android.graphics.Color.rgb(red,green,blue);

       return color;
    }

    public void setTop(float top){
        this.top = top;
    }
    public void setBottom(float bottom){
        this.bottom = bottom;
    }
    public void setRight(float right){
        this.right = right;
    }
    public void setLeft(float left){
        this.left = left;
    }

    public void setTranslationX(float left) {
        float width = width();
        this.right = left + width;
        this.left = left;
    }

    public void setTranslationY(float top) {
        float height = height();
        this.top = top;
        this.bottom = top + height;
    }

    public void setScale(float bottom){
        float width = width();
        float change = bottom - this.bottom;
        this.top -= change;
        this.bottom += change;
        this.left -= change;
        this.right += change;
    }

    public int getValue(){
        return mValue;
    }

    public void incrementValue(){
        mValue++;
        setColor();
    }

    public Paint getShapeForegroundPaint() {
        return mShapeForegroundPaint;
    }

    public void drawToCanvas(Canvas canvas){
        canvas.drawRoundRect(this,mSquareCornerRadius,mSquareCornerRadius,mShapeForegroundPaint);
        changeTextSize();
        canvas.drawText(mValue.toString(),(right+left)/2,(bottom+top)/2 + mTextPaint.getTextSize()*3/8,mTextPaint);
    }

    private void changeTextSize(){
        float textWidth = mTextPaint.measureText(Integer.toString(mValue));
        float squareWidth = right-left;
        while(textWidth>0.75*squareWidth){
            mTextPaint.setTextSize(mTextPaint.getTextSize()-4);
            textWidth = mTextPaint.measureText(Integer.toString(mValue));
        }
    }


}


