package com.example.android.colorup.game;

import android.content.Context;

import com.example.android.colorup.ActivityScope;
import com.example.android.colorup.model.GameBoard;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    private final Context mContext;
    private final GameContract.View mView;
    private int mRows;
    private int mColumns;

    public GameModule( Context context,GameContract.View view, int rows, int columns) {
        mView = view;
        mContext = context;
        mRows = rows;
        mColumns = columns;
    }

    @Provides
    @ActivityScope
    public GameContract.Presenter provideGamePresenter(GameBoard gameBoard){
        return new GamePresenter(mContext,mView,gameBoard,mRows,mColumns);
    }

}
