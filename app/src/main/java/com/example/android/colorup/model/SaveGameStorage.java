package com.example.android.colorup.model;


import android.content.Context;
import android.content.SharedPreferences;

public class SaveGameStorage {

    private final String HIGH_SCORES = "high_scores_data";
    private String mHighScoreKey = "high_score_";
    private final String SAVED_GAME = "saved_game";
    private String mSavedGameKey = "saved_game_";
    private SharedPreferences.Editor mHighScoreEditor;
    private SharedPreferences.Editor mSavedGameEditor;
    private int[][] mInitialBoard;
    private int mInitialScore;
    private int mInitialHighScore;
    private boolean mGameSaved;

    public SaveGameStorage(Context context,final int rows,final int columns) {
        setKeys(rows, columns);

        String noSavedGame = "no_saved_game";
        String savedGameString = getSavedGameString(context,noSavedGame);
        getHighScore(context);

        if(savedGameString.equals(noSavedGame)) {
            mInitialScore = 0;
            mInitialBoard = new int[rows][columns];
            mGameSaved = false;
        }
        else{
            mInitialBoard = convertFromStringToBoard(savedGameString,rows,columns);
            mInitialScore = getScore(savedGameString);
            mGameSaved = true;
        }

    }

    private void setKeys(int rows,int columns){
        String boardSize = rows + "x" + columns;
        mHighScoreKey += boardSize;
        mSavedGameKey += boardSize;
    }

    private String getSavedGameString(Context context, String noSavedGame){
        SharedPreferences sharedPreferencesSavedGame = context.getSharedPreferences(SAVED_GAME, Context.MODE_PRIVATE);
        //noSavedGame = "no_saved_game";
        String savedGameString = sharedPreferencesSavedGame.getString(mSavedGameKey,noSavedGame);
        mSavedGameEditor = sharedPreferencesSavedGame.edit();
        return  savedGameString;
    }

    private void getHighScore(Context context){
        SharedPreferences sharedPreferencesHighScore = context.getSharedPreferences(HIGH_SCORES, Context.MODE_PRIVATE);
        mInitialHighScore = sharedPreferencesHighScore.getInt(mHighScoreKey,0);
        mHighScoreEditor = sharedPreferencesHighScore.edit();
        //return sharedPreferencesHighScore.getInt(mHighScoreKey,0);
    }

    private String convertFromBoardToString(int[][] board,int score){
        StringBuilder saveBoard = new StringBuilder(score + "[");

        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++) {
                saveBoard.append(board[i][j]);
                if (i==board.length-1 && j==board[i].length-1)
                    saveBoard.append("]");
                else saveBoard.append(",");
            }

        return  saveBoard.toString();
    }

    private int[][] convertFromStringToBoard(String savedBoard,int rows,int columns){
        int[][] board = new int[rows][columns];

        int charPosition = savedBoard.indexOf("[") + 1;

        //int i,j;

        for(int k=0;k<rows*columns;k++){

            int lastComma = savedBoard.lastIndexOf(",");

            int valueEnd;

            if(charPosition<lastComma)
                valueEnd = savedBoard.indexOf(",",charPosition);
            else
                valueEnd = savedBoard.indexOf("]");

            int value = Integer.parseInt(savedBoard.substring(charPosition,valueEnd));

            int i  = k/columns;
            int j = k%columns;
            board[i][j] = value;

            charPosition = valueEnd+1;
        }

        return board;
    }

    private Integer getScore(String savedBoard){
        int boardStart = savedBoard.indexOf("[");
        return Integer.valueOf(savedBoard.substring(0,boardStart));
    }



    public void saveHighScore(int highScore){
        mHighScoreEditor.putInt(mHighScoreKey,highScore);
        mHighScoreEditor.commit();
    }

    public void saveGame(int[][] gameBoard,int highScore){
        mSavedGameEditor.putString(mSavedGameKey,convertFromBoardToString(gameBoard,highScore));
        mSavedGameEditor.commit();
    }

    public int[][] getInitialBoard() {
        return mInitialBoard;
    }

    public int getInitialScore() {
        return mInitialScore;
    }

    public int getInitialHighScore() {
        return mInitialHighScore;
    }

    public boolean isGameSaved(){
        return mGameSaved;
    }



}




