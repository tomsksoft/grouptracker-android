package com.grouptracker.android;

/**
 * Created by tieru on 6/29/14.
 */
public class ApplicationState {
    private static ApplicationState instance;

    public static ApplicationState getInstance(){
        if (instance == null){
            instance = new ApplicationState();
        }
        return instance;
    }

    private ApplicationState(){

    }
}