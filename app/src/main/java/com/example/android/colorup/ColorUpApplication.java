package com.example.android.colorup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.android.colorup.game.GameContract;
import com.example.android.colorup.game.GameModule;

public class ColorUpApplication extends Application {

    ColorUpApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerColorUpApplicationComponent.builder().build();
    }

    public ColorUpApplication get(Activity activity){
        return (ColorUpApplication) activity.getApplication();
    }

    public ColorUpApplicationComponent getComponent(){
        return mComponent;
    }

    public GameModule getGameModule(Context context,GameContract.View view,int rows,int columns){
        return new GameModule(context,view,rows,columns);
    }

}
