package com.example.android.colorup.game;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.colorup.ColorUpApplication;
import com.example.android.colorup.R;
import com.example.android.colorup.gameboardview.GameBoardView;
import com.example.android.colorup.idlingResource.SimpleIdlingResource;
import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.UpdateSquare;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity
        implements GameContract.View,GestureDetector.OnGestureListener{

    private static String ROWS = "rows";
    private static String COLUMNS = "columns";

    @BindView(R.id.game_board_view) GameBoardView mGameBoardView;
    @BindView(R.id.btn_undo) ImageButton mBtnUndo;
    @BindView(R.id.btn_restart) ImageButton mBtnRestart;
    @BindView(R.id.score) TextView mTextViewScore;
    @BindView(R.id.high_score) TextView mTextViewHighScore;

    @Inject GameContract.Presenter mPresenter;

    private GestureDetectorCompat mGestureDetector;
    private boolean mAllowFling = false;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        ButterKnife.bind(this);

        mBtnUndo.setEnabled(false);

        mGameBoardView.setDimensions(getIntent().getIntExtra(ROWS,4),getIntent().getIntExtra(COLUMNS,4));

        ColorUpApplication colorUpApplication = (ColorUpApplication) getApplication();

        DaggerGameComponent.builder()
                .colorUpApplicationComponent(colorUpApplication.get(this).getComponent())
                .gameModule(colorUpApplication.getGameModule(this,this,mGameBoardView.getRows(),mGameBoardView.getColumns()))
                .build().inject(this);

        mGameBoardView.addAnimationCompleteListener(new GameBoardView.AnimationCompleteListener(){
            @Override
            public void animationComplete() {
                mPresenter.animationComplete();
                if (mIdlingResource != null) mIdlingResource.setIdleState(true);
            }
        });
        mGestureDetector = new GestureDetectorCompat(this,this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.save();
    }

    @Override
    public void setSavedBoard(int[][] savedBoard) {
        //if (mIdlingResource != null) mIdlingResource.setIdleState(false);
        mGameBoardView.setBoard(savedBoard);
    }

    @Override
    public void addNewSquare(int row, int column) {
        mGameBoardView.addNewSquare(row,column);
    }

    @Override
    public void update(ArrayList<UpdateSquare> updates, int[][] board, int lastDirection) {
       // if (mIdlingResource != null) mIdlingResource.setIdleState(false);
        mGameBoardView.update(updates,board,lastDirection);
        mBtnUndo.setEnabled(true);
    }

    @Override
    public void gameOver() {
        mGameBoardView.gameOver();
    }

    @Override
    public void allowFling() {
        mAllowFling = true;
    }

    @Override
    public void undoUpdate(int[][] undoBoard) {
        mGameBoardView.undo(undoBoard);
        mAllowFling = true;
    }

    @Override
    public void restartUpdate(Coordinates newSquareCoords1, Coordinates newSquareCoords2) {
        mGameBoardView.restart();
        mGameBoardView.addNewSquare(newSquareCoords1.i,newSquareCoords1.j);
        mGameBoardView.addNewSquare(newSquareCoords2.i,newSquareCoords2.j);
        mAllowFling = true;
    }

    @Override
    public void setScore(String score) {
        mTextViewScore.setText(score);
    }

    @Override
    public void setHighScore(String highScore) {
        mTextViewHighScore.setText(highScore);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return fling(motionEvent,motionEvent1);
    }

    private synchronized boolean fling(MotionEvent motionEvent,MotionEvent motionEvent1){
        if(mAllowFling) {
            mAllowFling = false;
            float horizontal = motionEvent.getX() - motionEvent1.getX();
            float vertical = motionEvent.getY() - motionEvent1.getY();

            if (Math.abs(horizontal) > Math.abs(vertical)) {
                if (horizontal > 0)
                    mPresenter.swipe(GameBoard.DIRECTION_LEFT);
                else
                    mPresenter.swipe(GameBoard.DIRECTION_RIGHT);
            } else {
                if (vertical > 0)
                    mPresenter.swipe(GameBoard.DIRECTION_UP);
                else
                    mPresenter.swipe(GameBoard.DIRECTION_DOWN);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @OnClick(R.id.btn_undo)
    public void undo(){
        buttonAnimation(mBtnUndo);
        mPresenter.undo();
    }

    @OnClick(R.id.btn_restart)
    public void restart(){
        buttonAnimation(mBtnRestart);
        mPresenter.restart();
        mTextViewScore.setText("0");
    }

    private void buttonAnimation(ImageButton button){
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(button,
                View.ALPHA, 0.7f);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.start();
    }

    public static Intent getIntent(Context context,Integer rows, Integer columns){
        Intent intent = new Intent(context,GameActivity.class);
        intent.putExtra(ROWS,rows);
        intent.putExtra(COLUMNS,columns);

        return intent;
    }


    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    public void setNotIdle(){
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);
    }


}
