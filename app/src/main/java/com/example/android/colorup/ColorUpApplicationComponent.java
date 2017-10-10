package com.example.android.colorup;

import com.example.android.colorup.model.GameBoard;

import dagger.Component;

@ColorUpApplicationScope
@Component(modules = {GameBoardModule.class})
public interface ColorUpApplicationComponent {

    GameBoard getGameBoard();

}
