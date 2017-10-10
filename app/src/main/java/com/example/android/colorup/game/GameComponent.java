package com.example.android.colorup.game;

import com.example.android.colorup.ActivityScope;
import com.example.android.colorup.ColorUpApplicationComponent;

import dagger.Component;

@ActivityScope
@Component(modules = GameModule.class, dependencies = ColorUpApplicationComponent.class)
public interface GameComponent {
    void inject(GameActivity activity);
}
