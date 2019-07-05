package com.singorenko.climate;

import android.content.Context;

public class MyFirstIntentTask {

    public static final String ACTION_INTENT_TASK = "action_intent_task";


    public static void executeTask(Context context, String action){
        if(ACTION_INTENT_TASK.equals(action)){
            myMethod(context);
        }
    }

    private static void myMethod(Context context){
        //TODO: do something
    }
}
