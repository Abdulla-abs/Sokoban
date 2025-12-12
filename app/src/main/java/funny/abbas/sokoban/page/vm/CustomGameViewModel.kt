package funny.abbas.sokoban.page.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import funny.abbas.sokoban.core.Level
import funny.abbas.sokoban.data.repository.CustomLevelRepository
import funny.abbas.sokoban.page.CustomEffect
import funny.abbas.sokoban.page.CustomIntent
import funny.abbas.sokoban.page.CustomUiState
import funny.abbas.sokoban.page.LevelListState
import funny.abbas.sokoban.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomGameViewModel @Inject constructor(
    private val customRepository: CustomLevelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CustomUiState>(CustomUiState())
    val uiState: StateFlow<CustomUiState> = _uiState

    private val _sideEffect = Channel<CustomEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            customRepository.getCustomLevels().collect { resource ->
                if (resource is Resource.Error) {
                    _sideEffect.send(CustomEffect.ShowTips(resource.message))
                }
                _uiState.update { old ->
                    old.copy(
                        dataState = when (resource) {
                            is Resource.Success -> {
                                if (resource.data.isNotEmpty()) {
                                    LevelListState.Success(resource.data)
                                } else {
                                    LevelListState.Empty
                                }
                            }

                            is Resource.Error -> {
                                LevelListState.Error(resource.message)
                            }

                            else -> old.dataState
                        },
                        currentLevel = when(resource){
                            is Resource.Success -> {
                                if (resource.data.isNotEmpty()) {
                                    Level.parse(resource.data.getOrNull(old.currentIndex)?.data)
                                }else{
                                    null
                                }
                            }
                            else -> null
                        }
                    )
                }
            }
        }
    }

    fun onIntent(intent: CustomIntent) {
        when (intent) {
            is CustomIntent.PreviousLevel -> {
                onPreLevel()
            }

            is CustomIntent.NextLevel -> {
                onNextLevel()
            }

            is CustomIntent.ReloadLevel -> {
                onReload()
            }
        }
    }

    private fun onPreLevel() {
        if (_uiState.value.currentIndex <= 0) {
            viewModelScope.launch {
                _sideEffect.send(CustomEffect.ShowTips("没有上一关了~"))
            }
            return
        }
        _uiState.update { old ->
            old.copy(
                currentLevel = when (old.dataState) {
                    is LevelListState.Success -> {
                        Level.parse(old.dataState.levels.getOrNull(old.currentIndex - 1)?.data)
                    }

                    else -> old.currentLevel
                },
                currentIndex = old.currentIndex - 1
            )
        }
    }

    private fun onNextLevel() {
        if (_uiState.value.currentIndex >= _uiState.value.levelsSize-1) {
            viewModelScope.launch {
                _sideEffect.send(CustomEffect.ShowTips("没有下一关了~"))
            }
            return
        }
        _uiState.update { old ->
            old.copy(
                currentLevel = when (old.dataState) {
                    is LevelListState.Success -> {
                        Level.parse(old.dataState.levels.getOrNull(old.currentIndex + 1)?.data)
                    }

                    else -> old.currentLevel
                },
                currentIndex = old.currentIndex + 1
            )
        }
    }

    private fun onReload() {
        _uiState.update { old ->
            old.copy(
                currentLevel = when (old.dataState) {
                    is LevelListState.Success -> {
                        Level.parse(old.dataState.levels.getOrNull(old.currentIndex)?.data)
                    }

                    else -> old.currentLevel
                },
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}