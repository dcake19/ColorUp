package com.example.android.colorup.gameboardview;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.android.colorup.R;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.UpdateSquare;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView extends View {

    private int mRows = 4;
    private int mColumns = 4;
    private float mSquareSideLength;
    private int mSquareMargin = 8;
    private int mBackgroundSquareCornerRadius = 24;
    private int mSquareCornerRadius = 24;
    private int mSquareMarginPx;
    private int mBackgroundColor;
    private int mEmptySquareColor;
    private RectF mBackgroundRect;
    private Paint mBackgroundPaint;
    private Paint mEmptySquarePaint;
    private RectF[][] mEmptySquares;
    private AnimatableRectF[][] mSquares;
    private GameOverRect mGameOverRect;
    private int mLastDirection = 0;
    private int mMoveSquareDuration = 100;

    @TargetApi(21)
    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mListeners = new ArrayList<>();
        initColors();
        init(context);
    }

    public GameBoardView(Context context) {
        super(context);
        initColors();
        mListeners = new ArrayList<>();
        init(context);
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
        mListeners = new ArrayList<>();
        init(context);
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColors();
        mListeners = new ArrayList<>();
        init(context);
    }

    private void initColors(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            mBackgroundColor = getContext().getResources().getColor(R.color.colorBackground);
            mEmptySquareColor = getContext().getResources().getColor(R.color.colorEmpty);
        }else {
           initApi23Colors();
        }
    }

    @TargetApi(23)
    private void initApi23Colors(){
        mBackgroundColor = getContext().getColor(R.color.colorBackground);
        mEmptySquareColor = getContext().getColor(R.color.colorEmpty);
    }

    private void init(Context context){
        mBackgroundSquareCornerRadius = context.getResources().getInteger(R.integer.square_corner_radius);
        mSquareCornerRadius = context.getResources().getInteger(R.integer.square_corner_radius);
        setDimensions();
    }

    private void setDimensions(){
        if(mRows > 4 && mRows<=6) mSquareMargin = 6;
        else if(mRows > 6) mSquareMargin = 4;

        mSquareMarginPx = dpToPx(mSquareMargin);

        mSquareSideLength = getSquareSideLength();


        mBackgroundRect = new RectF();
        mBackgroundRect.left = mSquareMarginPx;
        mBackgroundRect.top = mSquareMarginPx;
        mBackgroundRect.right = 2*mSquareMarginPx + mColumns*(mSquareSideLength+mSquareMarginPx);
        mBackgroundRect.bottom = 2*mSquareMarginPx + mRows*(mSquareSideLength+mSquareMarginPx);

        mGameOverRect = new GameOverRect(getContext(),mBackgroundRect.left,mBackgroundRect.top,
                mBackgroundRect.right, mBackgroundRect.bottom,mSquareCornerRadius);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);

        mEmptySquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEmptySquarePaint.setColor(mEmptySquareColor);

        mEmptySquares = new RectF[mRows][mColumns];

        for(int i=0;i<mEmptySquares.length;i++){
            for(int j=0;j<mEmptySquares[i].length;j++){
                float xStart = getPxLocation(j);
                float yStart = getPxLocation(i);
                mEmptySquares[i][j] = new RectF(xStart,yStart,xStart+mSquareSideLength,yStart+mSquareSideLength);
            }
        }

        mSquares = new AnimatableRectF[mRows][mColumns];

        int maxDistance = Math.max(mRows-1,mColumns-1);
        mMoveSquareDuration = 300/maxDistance;

    }

    public void setBoard(int[][] savedBoard){
        for(int i=0;i<savedBoard.length;i++)
            for(int j=0;j<savedBoard[i].length;j++)
                if(savedBoard[i][j]!=0)
                    mSquares[i][j] = new AnimatableRectF(getPxLocation(j),getPxLocation(i),
                           getPxLocation(j)+mSquareSideLength,
                           getPxLocation(i)+mSquareSideLength,
                           savedBoard[i][j],
                           mSquareCornerRadius);
        invalidate();
    }

    public void addNewSquare(int row,int column){
        mSquares[row][column] = new AnimatableRectF(getPxLocation(column),getPxLocation(row),
                getPxLocation(column)+mSquareSideLength,
                getPxLocation(row)+mSquareSideLength,
                mSquareCornerRadius);
        invalidate();
    }

    private void addNewSquare(int row,int column,int value){
        mSquares[row][column] = new AnimatableRectF(getPxLocation(column),getPxLocation(row),
                getPxLocation(column)+mSquareSideLength,
                getPxLocation(row)+mSquareSideLength,
                value,
                mSquareCornerRadius);
    }

    public void update(final ArrayList<UpdateSquare> updates, final int[][] board, int lastDirection){

        mLastDirection = lastDirection;

        ArrayList<Animator> updateAnimations = new ArrayList<>();

        for(final UpdateSquare us:updates){
            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator animatorTranslate = ObjectAnimator.ofFloat(
                            mSquares[us.getStartRow()][us.getStartColumn()],
                            us.getPropertyName(),
                            getPxLocation(us.getChangingLocation()),
                            getPxLocation(us.getChangingLocation()+us.getSignedDistance()));

            animatorTranslate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    invalidate();
                }
            });
            animatorTranslate.setDuration(mMoveSquareDuration*us.getDistance());

            if(us.getIncrease()){
                ObjectAnimator animatorScale = ObjectAnimator.ofFloat(
                        mSquares[us.getStartRow()][us.getStartColumn()],
                        "scale",
                        mEmptySquares[us.getEndRow()][us.getEndColumn()].bottom,
                        mEmptySquares[us.getEndRow()][us.getEndColumn()].bottom+mSquareMarginPx/2);
                animatorScale.setDuration(200);
                animatorScale.setRepeatCount(1);
                animatorScale.setRepeatMode(ValueAnimator.REVERSE);
                animatorScale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        invalidate();
                    }
                });

                ObjectAnimator colorAnimator = ObjectAnimator.ofInt(mSquares[us.getStartRow()][us.getStartColumn()].getShapeForegroundPaint(),
                        "color",mSquares[us.getStartRow()][us.getStartColumn()].getColor(),mSquares[us.getStartRow()][us.getStartColumn()].getNextColor());

                colorAnimator.setDuration(200);
                colorAnimator.setEvaluator(new ArgbEvaluator());
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        invalidate();
                    }
                });
                animatorSet.play(animatorScale).with(colorAnimator).after(animatorTranslate);
            }else{
                animatorSet.play(animatorTranslate);
            }

            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    if(us.getIncrease()){
                        mSquares[us.getStartRow()][us.getStartColumn()].incrementValue();
                        invalidate();
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });

            updateAnimations.add(animatorSet);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(updateAnimations);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                for(UpdateSquare us:updates){
                    if(us.getIncrease()) {
                        mSquares[us.getEndRow()][us.getEndColumn()] = null;
                    }
                    mSquares[us.getEndRow()][us.getEndColumn()] = mSquares[us.getStartRow()][us.getStartColumn()];
                }

                for(int i=0;i<board.length;i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if(board[i][j]==0) mSquares[i][j] = null;
                    }
                }
                invalidate();
                for(AnimationCompleteListener acl:mListeners){
                    acl.animationComplete();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        set.start();
    }

    private float getSquareSideLength(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        width -= 4*mSquareMarginPx;
        return width/mColumns - mSquareMarginPx;
    }

    public void setDimensions(int rows,int columns){
        mRows = rows;
        mColumns = columns;
        setDimensions();
        invalidate();
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private float getPxLocation(int count){
        return 2*mSquareMarginPx+(mSquareMarginPx+mSquareSideLength)*count;
    }

    public int getRows() {
        return mRows;
    }

    public int getColumns() {
        return mColumns;
    }

    public void undo(int[][] undoBoard){
        for(int i=0;i<mSquares.length;i++)
            for(int j=0;j<mSquares[i].length;j++)
                mSquares[i][j] = null;

        for(int i=0;i<mSquares.length;i++)
            for(int j=0;j<mSquares[i].length;j++)
                if(undoBoard[i][j]>0) addNewSquare(i,j,undoBoard[i][j]);

        mGameOverRect.setGameOver(false);

        invalidate();
    }

    public void gameOver(){
        mGameOverRect.setGameOver(true);
    }

    public void restart(){
        for(int i=0;i<mSquares.length;i++)
            for(int j=0;j<mSquares[i].length;j++)
                mSquares[i][j] = null;

        mGameOverRect.setGameOver(false);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRoundRect(mBackgroundRect,mBackgroundSquareCornerRadius,mBackgroundSquareCornerRadius,mBackgroundPaint);
        for(int i=0;i<mEmptySquares.length;i++)
            for(int j=0;j<mEmptySquares[i].length;j++)
                canvas.drawRoundRect(mEmptySquares[i][j],mSquareCornerRadius,mSquareCornerRadius,mEmptySquarePaint);

        if(mLastDirection == GameBoard.DIRECTION_LEFT || mLastDirection == GameBoard.DIRECTION_UP) {
            for (int i = 0; i < mSquares.length; i++)
                for (int j = 0; j < mSquares[i].length; j++)
                    if (mSquares[i][j] != null) mSquares[i][j].drawToCanvas(canvas);
        }else {
            for (int i = mSquares.length-1; i >= 0; i--)
                for (int j = mSquares[i].length-1; j >= 0 ; j--)
                    if (mSquares[i][j] != null) mSquares[i][j].drawToCanvas(canvas);
        }

        mGameOverRect.drawToCanvas(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)(mBackgroundRect.right+mSquareMarginPx),(int)(mBackgroundRect.bottom+mSquareMarginPx));
    }

   List<AnimationCompleteListener> mListeners;

    public void addAnimationCompleteListener(AnimationCompleteListener listener){
        mListeners.add(listener);
    }

    public interface AnimationCompleteListener{
        void animationComplete();
    }

    public int[][] getBoard(){
        int[][] board = new int[mRows][mColumns];

        for (int i = 0; i < mSquares.length; i++)
            for (int j = 0; j < mSquares[i].length; j++) {
                if(mSquares[i][j]!=null)
                    board[i][j] = mSquares[i][j].getValue();
                else
                    board[i][j] = 0;
            }

        return board;
    }

    public boolean isGameOver(){
        return mGameOverRect.isGameOver();
    }

}
