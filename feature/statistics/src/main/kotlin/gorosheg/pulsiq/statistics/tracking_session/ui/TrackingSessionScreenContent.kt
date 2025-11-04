package gorosheg.pulsiq.statistics.tracking_session.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.Gray
import gorosheg.pulsiq.ui.White
import kotlin.math.roundToInt

@Composable
internal fun TrackingSessionScreenContent(
    state: TrackingSessionUiState,
    onEditClick: () -> Unit,
    onCloseEditDialogClick: () -> Unit,
    onNameChanged: (String) -> Unit,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    if (state.pulse.isNotEmpty()) {
        LaunchedEffect(state.pulse) {
            val firstTs = state.pulse.first().second
            val xSec: List<Number> = state.pulse.map { ((it.second - firstTs).coerceAtLeast(0L) / 1000.0) }
            val yBpm: List<Number> = state.pulse.map { it.first.toDouble() }

            modelProducer.runTransaction {
                lineSeries {
                    series(xSec, yBpm)
                }
            }
        }
    }

    val yPadding = 3.0
    val rangeProvider = remember {
        object : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double =
                minY - yPadding

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double =
                maxY + yPadding

            override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = minX
            override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = maxX
        }
    }

    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, value, _ ->
            val total = value.roundToInt().coerceAtLeast(0)
            val m = total / 60
            val s = total % 60
            "%d:%02d".format(m, s)
        },
    )

    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = { _, v, _ -> v.roundToInt().toString() },
        itemPlacer = remember {
            VerticalAxis.ItemPlacer.count(count = { 6 })
        },
    )

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(rangeProvider = rangeProvider),
        startAxis = startAxis,
        bottomAxis = bottomAxis,
    )

    val shape: Shape = MaterialTheme.shapes.large

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = BlueGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val date = state.dateStart.substringBeforeLast(". ").ifBlank { state.dateStart }
                val timeStart = state.dateStart.substringAfterLast(" ")
                val timeEnd = state.dateEnd.substringAfterLast(" ")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = White,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        CapsuleIcon(icon = Icons.Default.Create, onClick = onEditClick)
                    }

                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = White,
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Capsule(text = timeStart)
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White,
                        modifier = Modifier.offset(y = 1.dp)
                    )
                    Capsule(text = timeEnd)
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = BlueGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Пульс, сводка",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = "Макс",
                        value = "${state.highestPulse} уд/мин",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Мин",
                        value = "${state.lowestPulse} уд/мин",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Средний",
                        value = "${state.averagePulse} уд/мин",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = BlueGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Динамика пульса",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пульс",
                            style = MaterialTheme.typography.bodySmall.copy(
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                lineHeight = MaterialTheme.typography.bodySmall.fontSize,
                                letterSpacing = 0.sp
                            ),
                            color = White,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            modifier = Modifier
                                .graphicsLayer { rotationZ = -90f }
                        )
                    }


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        if (state.pulse.isEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(shape),
                                color = Gray
                            ) {}
                            Text(
                                text = "Нет данных для построения графика",
                                style = MaterialTheme.typography.bodyMedium,
                                color = White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            CartesianChartHost(
                                chart = chart,
                                modelProducer = modelProducer,
                                scrollState = rememberVicoScrollState(scrollEnabled = false),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                Text(
                    text = "Время",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }

    if (state.isEditDialogShow) {
        EditNameDialog(
            state = state,
            onNameChanged = onNameChanged,
            onCloseEditDialogClick = onCloseEditDialogClick
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
        }
    }
}

@Composable
private fun Capsule(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CapsuleIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickable(onClick = { onClick.invoke() }),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "TrackingSessionScreenContent Preview")
@Composable
private fun TrackingSessionScreenContentPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        val pulseValues = listOf(72, 78, 85, 92, 110, 105, 98, 88, 95, 102, 96, 90)
        val pulsePairs = pulseValues.mapIndexed { index, value -> value to (1_700_000_000_000L + index * 60_000L) }
        TrackingSessionScreenContent(
            state = TrackingSessionUiState(
                name = "Утренняя тренировка",
                dateStart = "26.10. 10:00",
                dateEnd = "26.10. 11:00",
                pulse = pulsePairs,
                highestPulse = pulseValues.maxOrNull() ?: 0,
                lowestPulse = pulseValues.minOrNull() ?: 0,
                averagePulse = pulseValues.average().toInt(),
            ),
            onEditClick = {},
            onCloseEditDialogClick = {},
            onNameChanged = {},
        )
    }
}
