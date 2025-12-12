package funny.abbas.sokoban.page

import funny.abbas.sokoban.core.Level
import funny.abbas.sokoban.data.database.bean.CustomLevel

sealed interface LevelListState {
    data class Error(val msg: String) : LevelListState
    data class Success(val levels: List<CustomLevel>) : LevelListState
    data object Empty : LevelListState
    data object IDLE : LevelListState
}

data class CustomUiState(
    val dataState: LevelListState = LevelListState.IDLE,
    val currentIndex: Int = 0,
    val currentLevel: Level? = null
) {

//    val currentLevel: Level?
//        get() = when (dataState) {
//            is LevelListState.Success -> {
//                Level.parse(dataState.levels.getOrNull(currentIndex)?.data)
//            }
//
//            else -> null
//        }

    val levelsSize: Int
        get() = when (dataState) {
            is LevelListState.Success -> {
                dataState.levels.size
            }

            else -> 0
        }
}

sealed interface CustomEffect {
    data class ShowTips(val msg: String) : CustomEffect
}

sealed interface CustomIntent {
    class PreviousLevel() : CustomIntent
    class NextLevel() : CustomIntent
    class ReloadLevel() : CustomIntent
}