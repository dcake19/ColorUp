package com.example.android.colorup.gameboardview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import com.example.android.colorup.R;


public class GameOverRect extends RectF {

    private boolean mGameOver = false;
    private float mSquareCornerRadius;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private String mGameOverText;

    public GameOverRect(Context context, float left, float top, float right, float bottom, int squareCornerRadius) {
        super(left, top, right, bottom);
        mSquareCornerRadius = squareCornerRadius;
        mGameOverText = context.getString(R.string.game_over);

        int textSize = (int) ((right - left)/7);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(getBackgroundColor(context));
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(getTextColor(context));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    private int getBackgroundColor(Context context){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getColor(R.color.colorGameOverBackground);
        else
            return context.getColor(R.color.colorGameOverBackground);
    }

    private int getTextColor(Context context){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getColor(R.color.colorGameOverText);
        else
            return context.getColor(R.color.colorGameOverText);
    }

    public void setGameOver(boolean gameOver){
        mGameOver = gameOver;
    }

    public boolean isGameOver(){return mGameOver;}

    public void drawToCanvas(Canvas canvas){
        if(mGameOver) {
            canvas.drawRoundRect(this, mSquareCornerRadius, mSquareCornerRadius, mBackgroundPaint);
            canvas.drawText(mGameOverText, (right + left) / 2, (bottom + top) / 2 + mTextPaint.getTextSize() / 2, mTextPaint);
        }
    }
}
