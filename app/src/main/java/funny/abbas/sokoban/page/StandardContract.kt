package funny.abbas.sokoban.page

import funny.abbas.sokoban.core.Level
import funny.abbas.sokoban.core.LevelVo

interface StandardLevelState {
    data object IDLE : StandardLevelState
    data object Loading : StandardLevelState
    data class Success(val levels: List<LevelVo>) : StandardLevelState
    data class Error(val msg: String) : StandardLevelState
}

data class StandardGameUiState(
    val levelListState: StandardLevelState = StandardLevelState.IDLE,
    val currentLevel: Level? = null,
    val currentLevelIndex: Int = 0,

){
    val levelListSize: Int get() = (levelListState as? StandardLevelState.Success)?.levels?.size ?: 0
}

interface StandardGameIntent{
    class PreviousLevel : StandardGameIntent
    class NextLevel: StandardGameIntent
    class ReloadLevel: StandardGameIntent
}

interface StandardGameEffect{
    data class ShowTips(val msg: String): StandardGameEffect
}