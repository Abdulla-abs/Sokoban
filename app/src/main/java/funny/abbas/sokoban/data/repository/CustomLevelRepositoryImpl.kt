package funny.abbas.sokoban.data.repository

import funny.abbas.sokoban.data.database.bean.CustomLevel
import funny.abbas.sokoban.data.database.dao.CustomLevelDao
import funny.abbas.sokoban.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart

class CustomLevelRepositoryImpl @Inject constructor(
    private val customDao: CustomLevelDao
): CustomLevelRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCustomLevels(): Flow<Resource<List<CustomLevel>>> =
        customDao.getAllLevel()
            .mapLatest { Resource.Success(it) as Resource<List<CustomLevel>> }
            .onStart { emit(Resource.Loading) }
            .catch { emit(Resource.Error("get local database error")) }
}