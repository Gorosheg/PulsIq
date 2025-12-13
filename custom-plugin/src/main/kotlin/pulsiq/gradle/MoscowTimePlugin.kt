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
import java.time.LocalDateTime
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
            .connectTimeout(Duration.ofSeconds(60))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .timeout(Duration.ofSeconds(120))
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

            val dateTime = DATETIME_RE.find(body)?.groupValues?.getOrNull(1)
            val timeZone = TIMEZONE_RE.find(body)?.groupValues?.getOrNull(1)

            if (dateTime.isNullOrBlank()) {
                logger.lifecycle("[MoscowTimePlugin] Received response but could not parse dateTime. Body: $body")
                return
            }

            val zone = try {
                ZoneId.of(timeZone ?: FALLBACK_ZONE_ID)
            } catch (_: Exception) {
                ZoneId.of(FALLBACK_ZONE_ID)
            }

            val zoneDateTime = parseApiDateTime(dateTime, zone)

            logger.lifecycle("[MoscowTimePlugin] Local time in Moscow: $zoneDateTime")
        } catch (e: Exception) {
            logger.error("[MoscowTimePlugin] Error while fetching Moscow time", e)
        }
    }

    private fun parseApiDateTime(value: String, zone: ZoneId): String {
        return LocalDateTime
            .parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    private companion object {
        private const val API_URL = "https://www.timeapi.io/api/Time/current/zone?timeZone=Europe/Moscow"
        private const val FALLBACK_ZONE_ID = "Europe/Moscow"
        private val DATETIME_RE = Regex("\"dateTime\"\\s*:\\s*\"([^\"]+)\"")
        private val TIMEZONE_RE = Regex("\"timeZone\"\\s*:\\s*\"([^\"]+)\"")
    }
}

class MoscowTimePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("printMoscowTime", PrintMoscowTimeTask::class.java)
    }
}
