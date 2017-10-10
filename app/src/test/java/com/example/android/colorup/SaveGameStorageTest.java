package com.example.android.colorup;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.colorup.model.SaveGameStorage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaveGameStorageTest {

    @Mock
    Context mContext;

    @Mock
    SharedPreferences mSharedPreferences;

    SaveGameStorage mSaveGameStorage;

    String mSavedGame = "22[1,4,12,0,5,143,0,0,71]";

    String noSavedGame = "no_saved_game";
    private final String HIGH_SCORES = "high_scores_data";
    private String mHighScoreKey = "high_score_3x3";
    private final String SAVED_GAME = "saved_game";
    private String mSavedGameKey = "saved_game_3x3";
    private int initalHighScore = 600;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        when(mContext.getSharedPreferences(SAVED_GAME,Context.MODE_PRIVATE)).thenReturn(mSharedPreferences);
        when(mContext.getSharedPreferences(HIGH_SCORES, Context.MODE_PRIVATE)).thenReturn(mSharedPreferences);
        when(mSharedPreferences.getString(mSavedGameKey,noSavedGame)).thenReturn(mSavedGame);
        when(mSharedPreferences.getInt(mHighScoreKey,0)).thenReturn(initalHighScore);

        mSaveGameStorage = new SaveGameStorage(mContext,3,3);

    }

    @Test
    public void testSetKeys(){
        verify(mSharedPreferences).getString(mSavedGameKey,noSavedGame);
        verify(mSharedPreferences).getInt(mHighScoreKey,0);
    }

    @Test
    public void testInitial(){
        assertTrue(mSaveGameStorage.isGameSaved());

        int[][] board = {{1,4,12},{0,5,143},{0,0,71}};
        int[][] savedBoard = mSaveGameStorage.getInitialBoard();

        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++)
                assertTrue(board[i][j]==savedBoard[i][j]);

        assertTrue(mSaveGameStorage.getInitialHighScore()==initalHighScore);
        assertTrue(mSaveGameStorage.getInitialScore()==22);
    }

}
