package funny.abbas.sokoban;

import android.app.Application;

import funny.abbas.sokoban.database.AppDatabase;

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
