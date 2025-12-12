package funny.abbas.sokoban.page.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import funny.abbas.sokoban.core.LevelMapper
import funny.abbas.sokoban.data.repository.StandardLevelRepository
import funny.abbas.sokoban.page.StandardGameEffect
import funny.abbas.sokoban.page.StandardGameIntent
import funny.abbas.sokoban.page.StandardGameUiState
import funny.abbas.sokoban.page.StandardLevelState
import funny.abbas.sokoban.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val standardLevelRepository: StandardLevelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StandardGameUiState())
    val uiState: StateFlow<StandardGameUiState> = _uiState

    private val _sideEffect = Channel<StandardGameEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            val inputStream = context.assets.open("level/sokoban.levels")
            standardLevelRepository.getLevels(inputStream).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _sideEffect.send(StandardGameEffect.ShowTips(resource.message))
                    }

                    else -> {
                        //do nothing
                    }
                }
                _uiState.update { old ->
                    old.copy(
                        levelListState = when (resource) {
                            is Resource.Success -> {
                                StandardLevelState.Success(resource.data)
                            }

                            is Resource.Error<*> -> {
                                StandardLevelState.Error(resource.message)
                            }

                            is Resource.Loading -> {
                                StandardLevelState.Loading
                            }

                            else -> StandardLevelState.Error("unKnow error")
                        },
                        currentLevel = when (resource) {
                            is Resource.Success -> {
                                LevelMapper.mapper(resource.data.getOrNull(old.currentLevelIndex))
                            }

                            else -> old.currentLevel
                        }
                    )
                }
            }
        }
    }

    fun onIntent(intent: StandardGameIntent) {
        when (intent) {
            is StandardGameIntent.PreviousLevel -> {
                preLevel()
            }

            is StandardGameIntent.NextLevel -> {
                nextLevel()
            }

            is StandardGameIntent.ReloadLevel -> {
                reloadLevel()
            }
        }
    }

    private fun nextLevel() {
        if (_uiState.value.currentLevelIndex >= _uiState.value.levelListSize - 1) {
            viewModelScope.launch {
                _sideEffect.send(StandardGameEffect.ShowTips("没有下一关了~"))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { old ->
                old.copy(
                    currentLevel = when (old.levelListState) {
                        is StandardLevelState.Success -> {
                            LevelMapper.mapper(old.levelListState.levels.getOrNull(old.currentLevelIndex + 1))
                        }

                        else -> old.currentLevel
                    },
                    currentLevelIndex = when (old.levelListState) {
                        is StandardLevelState.Success -> {
                            old.currentLevelIndex + 1
                        }

                        else -> old.currentLevelIndex
                    }
                )
            }
        }
    }

    private fun preLevel() {
        if (_uiState.value.currentLevelIndex <= 0) {
            viewModelScope.launch {
                _sideEffect.send(StandardGameEffect.ShowTips("没有上一关了~"))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { old ->
                old.copy(
                    currentLevel = when (old.levelListState) {
                        is StandardLevelState.Success -> {
                            LevelMapper.mapper(old.levelListState.levels.getOrNull(old.currentLevelIndex - 1))
                        }

                        else -> old.currentLevel
                    },
                    currentLevelIndex = when (old.levelListState) {
                        is StandardLevelState.Success -> {
                            old.currentLevelIndex - 1
                        }

                        else -> old.currentLevelIndex
                    }
                )
            }
        }
    }

    private fun reloadLevel() {
        viewModelScope.launch {
            _uiState.update { old ->
                old.copy(
                    currentLevel = when (old.levelListState) {
                        is StandardLevelState.Success -> {
                            LevelMapper.mapper(
                                old.levelListState.levels.getOrNull(old.currentLevelIndex)
                            )
                        }

                        else -> old.currentLevel
                    }
                )
            }
        }
    }
}
