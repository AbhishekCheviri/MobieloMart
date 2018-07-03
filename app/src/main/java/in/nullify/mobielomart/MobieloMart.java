package in.nullify.mobielomart;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * Created by Abhishekpalodath on 03-07-2018.
 */

public class MobieloMart extends Application {

    private static MobieloMart mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
        }
    }

    public static Context getInstance() {
        return mInstance;
    }
}