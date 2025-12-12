package funny.abbas.sokoban.data.repository

import funny.abbas.sokoban.core.LevelVo
import funny.abbas.sokoban.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StandardLevelRepository {

    fun getLevels(inputStream : InputStream): Flow<Resource<MutableList<LevelVo>>>

}