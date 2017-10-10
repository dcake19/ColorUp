package com.example.android.colorup;


import com.example.android.colorup.model.GameBoard;

import dagger.Module;
import dagger.Provides;

@Module
public class GameBoardModule {

    @Provides
    @ColorUpApplicationScope
    public GameBoard provideGameBoard(){
        return new GameBoard();
    }
}
