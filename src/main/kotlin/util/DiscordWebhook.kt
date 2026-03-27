package util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import logger
import java.time.Instant

object DiscordWebhook {
    private val client = HttpClient(CIO)

    suspend fun sendReport(webhookUrl: String, playerName: String, reason: String) {
        if (webhookUrl.isBlank()) {
            logger.warning("Discord report webhook URL is not configured.")
            return
        }

        val escapedReason = reason.replace("\\", "\\\\").replace("\"", "\\\"")
        val escapedPlayer = playerName.replace("\\", "\\\\").replace("\"", "\\\"")
        val avatar = "https://skinmc.net/api/v1/face/username/$playerName/600"

        val timestamp = Instant.now().toString()

        val payload = """
            {
              "username": "$escapedPlayer",
              "avatar_url": "$avatar",
              "embeds": [{
                "title": "🚨 New Player Report",
                "color": 16711680,
                "fields": [
                  { "name": "Player", "value": "$escapedPlayer", "inline": true },
                  { "name": "Reason", "value": "$escapedReason", "inline": false }
                ],
                "timestamp": "$timestamp",
                "footer": { "text": "Cloudie SMP" }
              }]
            }
        """.trimIndent()

        try {
            val response: HttpResponse = client.post(webhookUrl) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
            if (!response.status.isSuccess()) {
                logger.warning("Discord webhook returned ${response.status}: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            logger.severe("Failed to send Discord report webhook: ${e.message}")
        }
    }
}

