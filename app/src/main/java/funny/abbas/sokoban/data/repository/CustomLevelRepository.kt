package funny.abbas.sokoban.data.repository

import funny.abbas.sokoban.data.database.bean.CustomLevel
import funny.abbas.sokoban.util.Resource
import kotlinx.coroutines.flow.Flow

interface CustomLevelRepository {

    fun getCustomLevels() : Flow<Resource<List<CustomLevel>>>

}
