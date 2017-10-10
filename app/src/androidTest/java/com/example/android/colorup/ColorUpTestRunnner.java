package com.example.android.colorup;


import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class ColorUpTestRunnner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws
            IllegalAccessException, ClassNotFoundException, InstantiationException {
        return super.newApplication(cl, TestColorUpApplication.class.getName(), context);
    }
}
