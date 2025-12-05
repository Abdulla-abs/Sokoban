package funny.abbas.sokoban.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import funny.abbas.sokoban.database.bean.CustomLevel;
import funny.abbas.sokoban.database.convert.Converters;
import funny.abbas.sokoban.database.dao.CustomLevelDao;

@Database(entities = {CustomLevel.class}, version = 2, exportSchema = false)
@TypeConverters(value = {Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract CustomLevelDao getCustomLevelDao();

    public static AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "sokoban.db"   // 数据库文件名
                )
                // 开发阶段可以用这个，上线前一定要写 Migration
                .fallbackToDestructiveMigration()
                .build();
    }
}
