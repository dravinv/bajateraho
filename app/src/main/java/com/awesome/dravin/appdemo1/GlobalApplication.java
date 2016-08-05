package com.awesome.dravin.appdemo1;

/**
 * Created by nikita on 6/2/16.
 */
import android.content.Context;

public class GlobalApplication extends android.app.Application{
    private static Context GlobalContext;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if(GlobalApplication.GlobalContext == null){
            GlobalApplication.GlobalContext = getApplicationContext();
        }
    }

    public static Context getGlobalAppContext() {
        return GlobalApplication.GlobalContext;
    }
}

