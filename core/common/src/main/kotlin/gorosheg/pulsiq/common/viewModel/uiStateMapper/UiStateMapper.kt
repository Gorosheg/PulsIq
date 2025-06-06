package gorosheg.pulsiq.common.viewModel.uiStateMapper

interface UiStateMapper<State, UiState> {

	fun State.map(): UiState

	fun mapState(state: State): UiState {
		return state.map()
	}
}