package gorosheg.pulsiq.common.viewModel.ui_state_mapper

interface UiStateMapper<State, UiState> {

	fun State.map(): UiState

	fun mapState(state: State): UiState {
		return state.map()
	}
}