package funny.abbas.sokoban.page.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import funny.abbas.sokoban.domain.Level
import funny.abbas.sokoban.domain.LevelMapper
import funny.abbas.sokoban.domain.LevelVo

class MainViewModel : ViewModel() {
    val allLevel: MutableLiveData<MutableList<LevelVo>?> = MutableLiveData<MutableList<LevelVo>?>(
        mutableListOf<LevelVo>()
    )
    val levelIndex: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val currentLevel: MediatorLiveData<Level?> = MediatorLiveData<Level?>()

    init {
        currentLevel.addSource<MutableList<LevelVo>?>(
            allLevel,
            object : Observer<MutableList<LevelVo>?> {
                override fun onChanged(levels: MutableList<LevelVo>?) {
                    onLevelIndexCombine(levels, levelIndex.getValue())
                }
            })
        currentLevel.addSource<Int?>(levelIndex, object : Observer<Int> {
            override fun onChanged(integer: Int) {
                onLevelIndexCombine(allLevel.getValue(), integer)
            }
        })
    }

    private fun onLevelIndexCombine(list: MutableList<LevelVo>?, index: Int?) {
        if (list == null) return
        if (list.isEmpty()) return
        try {
            index?.let {
                val levelVo = list[it]
                currentLevel.postValue(LevelMapper.mapper(levelVo))
            }
        } catch (e: Exception) {
            //wtf
        }
    }


    fun nextLevel() {
        var value = levelIndex.getValue()
        if (value != null) {
            levelIndex.setValue(++value)
        } else {
            levelIndex.setValue(0)
        }
    }

    fun preLevel() {
        var value = levelIndex.getValue()
        if (value != null && value > 0) {
            levelIndex.setValue(--value)
        } else {
            levelIndex.setValue(0)
        }
    }

    fun reloadLevel() {
        onLevelIndexCombine(
            allLevel.value,
            levelIndex.value
        )
    }
}
