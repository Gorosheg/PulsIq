package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import gorosheg.pulsiq.statistics.tracking_session.ui.model.ChartState
import kotlin.math.roundToInt

@Composable
internal fun PulseChart(
    state: ChartState
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    if (state.timeList.isNotEmpty()) {
        LaunchedEffect(state.timeList) {
            modelProducer.runTransaction {
                lineSeries {
                    series(state.timeList, state.pulseList)
                }
            }
        }
    }

    val rangeProvider = remember {
        object : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double = minY - state.chartPadding
            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double = maxY + state.chartPadding
            override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = minX
            override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = maxX
        }
    }

    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = { _, v, _ -> v.roundToInt().toString() },
        itemPlacer = remember {
            VerticalAxis.ItemPlacer.count(count = { 6 })
        },
    )

    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, value, _ ->
            val total = value.roundToInt().coerceAtLeast(0)
            val m = total / 60
            val s = total % 60
            "%d:%02d".format(m, s)
        },
    )

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(rangeProvider = rangeProvider),
        startAxis = startAxis,
        bottomAxis = bottomAxis,
    )

    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        modifier = Modifier
            .fillMaxSize()
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "PulseChart Preview")
@Composable
private fun PulseChartPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        val pulseValues: List<Int> = listOf(72, 78, 85, 92, 110, 105, 98, 88, 95, 102, 96, 90)
        val timeSeconds: List<Int> = pulseValues.indices.map { it * 60 }
        PulseChart(
            state = ChartState(
                timeList = timeSeconds,
                pulseList = pulseValues,
                chartPadding = 5.0
            )
        )
    }
}
