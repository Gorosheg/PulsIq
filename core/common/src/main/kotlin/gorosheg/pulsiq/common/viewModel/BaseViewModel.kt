package gorosheg.pulsiq.common.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

open class BaseViewModel<State, UiState>(
    initState: State,
    uiStateMapper: UiStateMapper<State, UiState>,
) : ViewModel() {

    private val state: MutableStateFlow<State> = MutableStateFlow(initState)

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(uiStateMapper.mapState(initState))
    val uiState: StateFlow<UiState> = _uiState

    protected val getState: State
        get() = state.value


    init {
        state.map(uiStateMapper::mapState)
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    protected fun updateState(update: State.() -> State) {
        state.value = update.invoke(state.value)
    }
}