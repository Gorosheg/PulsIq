package pulsiq.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

abstract class PrintMoscowTimeTask : DefaultTask() {

    init {
        group = "pulsiq"
        description = "Gets current local time in Moscow from api and Displays it in logs."

        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun run() {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build()

        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val code = response.statusCode()

            if (code / 100 != 2) {
                logger.warn("[MoscowTimePlugin] Failed to fetch time. HTTP $code")
                return
            }

            val body = response.body()
            val datetime = DATETIME_RE.find(body)?.groupValues?.getOrNull(1)
            val timezone = TIMEZONE_RE.find(body)?.groupValues?.getOrNull(1)

            if (datetime == null) {
                logger.lifecycle("[MoscowTimePlugin] Received response but could not parse datetime. Body: $body")
                return
            }

            val zone = try {
                ZoneId.of(timezone ?: FALLBACK_ZONE_ID)
            } catch (_: Exception) {
                ZoneId.of(FALLBACK_ZONE_ID)
            }

            val zoneDateTime = OffsetDateTime
                .parse(datetime)
                .atZoneSameInstant(zone)

            val formatted = zoneDateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss OOOO"))

            logger.lifecycle("[MoscowTimePlugin] Local time in Moscow (${zoneDateTime.zone}): $formatted")
        } catch (e: Exception) {
            logger.error("[MoscowTimePlugin] Error while fetching Moscow time", e)
        }
    }

    private companion object {
        private const val API_URL = "https://worldtimeapi.org/api/timezone/Europe/Moscow"
        private const val FALLBACK_ZONE_ID = "Europe/Moscow"
        private val DATETIME_RE = Regex("\"datetime\"\\s*:\\s*\"([^\"]+)\"")
        private val TIMEZONE_RE = Regex("\"timezone\"\\s*:\\s*\"([^\"]+)\"")
    }
}

class MoscowTimePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("printMoscowTime", PrintMoscowTimeTask::class.java)
    }
}
