package funny.abbas.sokoban.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import funny.abbas.sokoban.database.bean.CustomLevel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CustomLevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCustomLevel(CustomLevel customLevel);

    @Delete
    void  deleteCustomLevel(CustomLevel customLevel);



    @Query("SELECT * FROM CustomLevel")
    Observable<List<CustomLevel>> observableAll();

    @Query("SELECT * FROM CustomLevel")
    Single<List<CustomLevel>> singleAll();

    @Query("SELECT * FROM CustomLevel LIMIT :pageSize OFFSET :startIndex")
    Single<List<CustomLevel>> getLevelWithLimit(int pageSize,int startIndex);


}
