package com.example.android.colorup.game;


import android.content.Context;

import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.UpdateSquare;

import java.util.ArrayList;

public interface GameContract {

    interface View{
        void setSavedBoard(int[][] savedBoard);
        void addNewSquare(int row, int column);
        void update(ArrayList<UpdateSquare> updates, int[][] board, int lastDirection);
        void gameOver();
        void allowFling();
        void undoUpdate(int[][] undoBoard);
        void restartUpdate(Coordinates newSquareCoords1, Coordinates newSquareCoords2);
        void setScore(String score);
        void setHighScore(String highScore);
    }

    interface Presenter{
        void swipe(int direction);
        void animationComplete();
        void undo();
        void restart();
        void save();
    }

}
