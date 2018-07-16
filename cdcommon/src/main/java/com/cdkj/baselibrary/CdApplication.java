
package com.cdkj.baselibrary;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class CdApplication extends Application {
    /**
     * Global application context.
     */
    private static Context sContext;


    public CdApplication() {
        sContext = this;
    }

    public static void initialize(Context context) {
        sContext = context;
//        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return sContext;
    }

}
