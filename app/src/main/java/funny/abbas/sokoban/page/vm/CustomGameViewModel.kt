package funny.abbas.sokoban.page.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import funny.abbas.sokoban.MyApplication
import funny.abbas.sokoban.database.bean.CustomLevel
import funny.abbas.sokoban.core.Level
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CustomGameViewModel : ViewModel {

    private var pageSize = 5
    private var page = 0

    private var levelList: MutableList<CustomLevel> = ArrayList(pageSize)
    var currentIndex: Int = 0
        private set

    val levelFlow = MutableSharedFlow<Result<Level>>()

    constructor() : super() {
        val subscribe = getLevelOfPage().subscribe({ dataList ->
            viewModelScope.launch {
                val nextLevelInList = getNextLevelInList(currentIndex)
                levelFlow.emit(nextLevelInList)
            }
        }, { e ->
            Log.e("error", e.toString())
        })
    }

    private fun getLevelOfPage(): Single<List<CustomLevel>> {
        return MyApplication.appDatabase.customLevelDao
            .getLevelWithLimit(pageSize, page * pageSize)
            .subscribeOn(Schedulers.io())
            .map { dataList ->
                levelList.clear()
                levelList.addAll(dataList)
                return@map dataList
            }
    }

    fun getPreLevel() {
        if (currentIndex <= 0) {
            viewModelScope.launch {
                levelFlow.emit(Result.failure(IllegalStateException("没有-1关~")))
            }
            return
        }
        var preIndex = currentIndex - 1
        val start = pageSize * page
        val end = pageSize * (page + 1)
        if (preIndex in start..end) {
            viewModelScope.launch {
                val preLevelInList = getPreLevelInList(preIndex)
                if (preLevelInList.isSuccess) {
                    currentIndex = preIndex
                }
                levelFlow.emit(preLevelInList)
            }
        } else {
            if (page <= 0) {
                viewModelScope.launch {
                    levelFlow.emit(Result.failure(IllegalStateException("当前已经是第一页")))
                }
                return
            }
            page--
            val subscribe = getLevelOfPage().subscribe({
                viewModelScope.launch {
                    val preLevelInList = getPreLevelInList(preIndex)
                    if (preLevelInList.isSuccess) {
                        currentIndex = preIndex
                    }
                    levelFlow.emit(preLevelInList)
                }
            }, { e ->

            })
        }
    }

    fun getNextLevel() {
        var nextIndex = currentIndex + 1
        if (nextIndex >= pageSize + pageSize * page) {
            //fetch next page
            page++
            val subscribe = getLevelOfPage().subscribe({
                viewModelScope.launch {
                    var nextLevelInList = getNextLevelInList(nextIndex)
                    if (nextLevelInList.isSuccess) {
                        currentIndex = nextIndex
                    }
                    levelFlow.emit(nextLevelInList)
                }
            }, { e ->
                viewModelScope.launch {
                    levelFlow.emit(Result.failure(e))
                }
            })
        } else {
            viewModelScope.launch {
                var nextLevelInList = getNextLevelInList(nextIndex)
                if (nextLevelInList.isSuccess) {
                    currentIndex = nextIndex
                }
                levelFlow.emit(nextLevelInList)
            }
        }
    }

    private fun getPreLevelInList(index: Int): Result<Level> {
        val listIndex = index % pageSize
        return if (listIndex <= levelList.size - 1) {
            try {
                Result.success(Level.parse(levelList[listIndex].data))
            } catch (e: Exception) {
                Result.failure(e);
            }
        } else {
            Result.failure(IndexOutOfBoundsException("已经没有上一关了~"))
        }
    }

    private fun getNextLevelInList(index: Int): Result<Level> {
        val listIndex = index % pageSize
        return if (listIndex <= levelList.size - 1) {
            try {
                Result.success(Level.parse(levelList[listIndex].data))
            } catch (e: Exception) {
                Result.failure(e);
            }
        } else {
            Result.failure(IndexOutOfBoundsException("已经没有下一关了~"))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}