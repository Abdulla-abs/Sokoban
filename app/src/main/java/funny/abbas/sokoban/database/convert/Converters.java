package funny.abbas.sokoban.database.convert;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

// 可以把这个类放在任意位置，建议单独建一个文件叫 Converters.java
public class Converters {

    private static final Gson gson = new Gson();

    // ==================== List<Integer> <-> String ====================
    @TypeConverter
    public static String fromIntegerList(List<Integer> data) {
        if (data == null) {
            return null;
        }
        return gson.toJson(data);
    }

    @TypeConverter
    public static List<Integer> toIntegerList(String data) {
        if (data == null) {
            return null;
        }
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    // ==================== Date <-> Long (时间戳) ====================
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}