package com.singorenko.climate;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       String action = intent.getAction();
       MyFirstIntentTask.executeTask(this, action);
    }
}
