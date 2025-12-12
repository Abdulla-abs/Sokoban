package funny.abbas.sokoban.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import funny.abbas.sokoban.core.LevelVo
import funny.abbas.sokoban.util.Resource
import funny.abbas.sokoban.util.SokobanParser
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

class StandardLevelRepositoryImpl @Inject constructor(

) : StandardLevelRepository {

    override fun getLevels(inputStream : InputStream): Flow<Resource<MutableList<LevelVo>>> = flow {
        try {
            emit(Resource.Loading)
            val text = inputStream.bufferedReader().use { it.readText() }
            emit(Resource.Success(SokobanParser.parseLevels(text)))
        } catch (e: IOException) {
            emit(Resource.Error("解析本地关卡文件失败"))
        }
    }
}
