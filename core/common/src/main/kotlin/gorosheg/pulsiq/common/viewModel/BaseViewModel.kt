package gorosheg.pulsiq.common.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.viewModel.uiStateMapper.UiStateMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


open class BaseViewModel<State, UiState, Effect>(
    application: Application,
    initState: State,
    uiStateMapper: UiStateMapper<State, UiState>,
) : AndroidViewModel(application) {

    private val state: MutableStateFlow<State> = MutableStateFlow(initState)

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(uiStateMapper.mapState(initState))
    val uiState: StateFlow<UiState> = _uiState

    private val eventChannel: Channel<Effect> = Channel<Effect>(Channel.BUFFERED)
    val eventsFlow: Flow<Effect> = eventChannel.receiveAsFlow()

    init {
        state.map(uiStateMapper::mapState)
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    protected fun state(update: State.() -> State) {
        state.value = update.invoke(state.value)
    }

    protected fun sendEffect(event: Effect) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}