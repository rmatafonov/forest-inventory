package com.codenrock.hackwagon.forest.inventory.telegram.service

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.BotToken
import com.codenrock.hackwagon.forest.inventory.telegram.domain.getfile.GetFileResponseDto
import com.codenrock.hackwagon.forest.inventory.telegram.domain.getfile.RequestParams
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class TelegramRestApiClient(
    @BotToken private val botToken: String,
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient
) {
    fun getVoiceFilePathForDownload(fileId: String): String? {
        val input = objectMapper.writeValueAsString(RequestParams(fileId))

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.telegram.org/bot${botToken}/getFile"))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .method(
                HttpMethod.GET.name(),
                HttpRequest.BodyPublishers.ofString(input)
            )
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == HttpStatus.OK.value()) {
            val responseDto = objectMapper.readValue(response.body(), GetFileResponseDto::class.java)
            if (responseDto.ok) {
                return responseDto.result.filePath
            }
        }

        return null
    }

    fun downloadFile(filePath: String): InputStream? {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.telegram.org/file/bot${botToken}/${filePath}"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream())

        if (response.statusCode() == HttpStatus.OK.value()) {
            return response.body()
        }

        return null
    }
}