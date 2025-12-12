package funny.abbas.sokoban;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import funny.abbas.sokoban.data.database.AppDatabase;

@HiltAndroidApp
public class MyApplication extends Application {

    public static Application application;
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        appDatabase = AppDatabase.getInstance(this);
    }
}
