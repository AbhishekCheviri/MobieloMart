package in.nullify.mobielomart;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * Created by Abhishekpalodath on 03-07-2018.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            FirebaseApp.initializeApp(getApplicationContext());
        } catch (Exception e) {
        }
    }

    public static Context getInstance() {
        return mInstance;
    }
}