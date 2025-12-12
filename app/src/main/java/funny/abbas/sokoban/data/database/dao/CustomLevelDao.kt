package funny.abbas.sokoban.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import funny.abbas.sokoban.data.database.bean.CustomLevel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomLevelDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun saveCustomLevel(customLevel: CustomLevel)

    @Delete
    fun deleteCustomLevel(customLevel: CustomLevel)


    @Query("SELECT * FROM CustomLevel")
    fun getAllLevel(): Flow<List<CustomLevel>>

    @Query("SELECT * FROM CustomLevel LIMIT :pageSize OFFSET :startIndex")
    fun getLevelWithLimit(pageSize: Int, startIndex: Int): Flow<List<CustomLevel>>
}
