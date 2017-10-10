package com.example.android.colorup.game;


import android.content.Context;

import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.SaveGameStorage;
import com.example.android.colorup.model.UpdateSquare;

import java.util.ArrayList;

public class GamePresenter implements GameContract.Presenter{

    private GameBoard mGameBoard;
    private SaveGameStorage mSaveGameStorage;
    private GameContract.View mView;
    private Integer mHighScore;

    public GamePresenter(Context context,GameContract.View view,GameBoard gameBoard,int rows,int columns) {
        mView = view;
        mGameBoard = gameBoard;
        mSaveGameStorage = new SaveGameStorage(context,rows,columns);
        setUp(rows, columns);
    }

    private void setUp(int rows, int columns){
        mHighScore = mSaveGameStorage.getInitialHighScore();
        mView.setHighScore(mHighScore.toString());
        if(!mSaveGameStorage.isGameSaved()) {
            mGameBoard.startNewGame(rows, columns);
            Coordinates newSquareCoords1 = mGameBoard.addRandomSquare();
            Coordinates newSquareCoords2 = mGameBoard.addRandomSquare();
            mView.addNewSquare(newSquareCoords1.i, newSquareCoords1.j);
            mView.addNewSquare(newSquareCoords2.i, newSquareCoords2.j);
        }
        else {
            mGameBoard.loadGame(mSaveGameStorage.getInitialBoard(),mSaveGameStorage.getInitialScore());
            mView.setSavedBoard(mGameBoard.getBoard());
        }
        mView.setScore(String.valueOf(mSaveGameStorage.getInitialScore()));

        if (mGameBoard.gameOver()) mView.gameOver();
        else mView.allowFling();
    }



    @Override
    public void swipe(int direction) {

        ArrayList<UpdateSquare> updates = mGameBoard.getUpdates(direction);

        if(updates.size()>0) {
            mView.update(updates,mGameBoard.getBoard(),mGameBoard.getLastDirection());
            Integer score = mGameBoard.getScore();
            mView.setScore(score.toString());
            if(score>mHighScore) {
                mHighScore = score;
                mView.setHighScore(score.toString());
                mSaveGameStorage.saveHighScore(mHighScore);
            }

        }else if(!mGameBoard.gameOver()) mView.allowFling();
    }

    @Override
    public void animationComplete() {
        Coordinates newSquareCoords = mGameBoard.addRandomSquare();
        if (newSquareCoords != null) mView.addNewSquare(newSquareCoords.i, newSquareCoords.j);

        if (mGameBoard.gameOver()) mView.gameOver();

        if(newSquareCoords!=null || !mGameBoard.gameOver()) mView.allowFling();
    }

    @Override
    public void undo() {
        mView.undoUpdate(mGameBoard.undo());
        mView.setScore(mGameBoard.getScore().toString());
    }

    @Override
    public void restart() {
        mGameBoard.restart();
        mView.restartUpdate(mGameBoard.addRandomSquare(),mGameBoard.addRandomSquare());
    }

    @Override
    public void save() {
        mSaveGameStorage.saveGame(mGameBoard.getBoard(),mGameBoard.getScore());
    }


}
