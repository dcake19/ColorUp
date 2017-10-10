package com.example.android.colorup;


import android.content.Context;

import com.example.android.colorup.game.GameContract;
import com.example.android.colorup.game.GameModule;

public class TestColorUpApplication extends ColorUpApplication{

    private GameModule mGameModule;

    @Override
    public GameModule getGameModule(Context context, GameContract.View view, int rows, int columns) {
        if(mGameModule == null)
            return super.getGameModule(context, view, rows, columns);
        return mGameModule;
    }

    public void setGameModule(GameModule gameModule){
        mGameModule = gameModule;
    }


}
