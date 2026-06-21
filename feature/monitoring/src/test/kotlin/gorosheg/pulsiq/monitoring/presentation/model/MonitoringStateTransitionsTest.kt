package gorosheg.pulsiq.monitoring.presentation.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MonitoringStateTransitionsTest {

    @Test
    fun `GIVEN active tracking WHEN stop confirmation is requested THEN tracking continues and dialog is shown`() {
        val state = MonitoringState(
            isTracking = true,
            pulse = 93,
            isSetNameDialogShow = false,
            sessionName = "Morning"
        )

        val result = state.requestStopConfirmation()

        assertTrue(result.isTracking)
        assertEquals(93, result.pulse)
        assertTrue(result.isSetNameDialogShow)
        assertEquals("Morning", result.sessionName)
    }

    @Test
    fun `GIVEN stop confirmation is shown WHEN stop is cancelled THEN tracking continues and dialog closes`() {
        val state = MonitoringState(
            isTracking = true,
            pulse = 93,
            isSetNameDialogShow = true,
            sessionName = "Edited"
        )

        val result = state.cancelStopConfirmation()

        assertTrue(result.isTracking)
        assertEquals(93, result.pulse)
        assertFalse(result.isSetNameDialogShow)
        assertEquals("", result.sessionName)
    }

    @Test
    fun `GIVEN stop confirmation is shown WHEN stop is confirmed THEN tracking stops and dialog closes`() {
        val state = MonitoringState(
            isTracking = true,
            pulse = 93,
            isSetNameDialogShow = true,
            sessionName = "Morning"
        )

        val result = state.confirmStop()

        assertFalse(result.isTracking)
        assertEquals(0, result.pulse)
        assertFalse(result.isSetNameDialogShow)
        assertEquals("", result.sessionName)
    }
}
