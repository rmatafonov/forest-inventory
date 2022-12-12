package com.codenrock.hackwagon.forest.inventory.telegram.service

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.YCFolderId
import com.codenrock.hackwagon.forest.inventory.telegram.annotation.YCToken
import com.codenrock.hackwagon.forest.inventory.telegram.domain.RecognitionResultDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

@Service
class YandexCloudSpeechRecognitionRestApiClient(
    @YCToken private val ycToken: String,
    @YCFolderId private val ycFolderId: String,
    @Value("com.codenrock.hackwagon.forest.inventory.yandex.ai.voice.recognize.uri") private val ycVoiceRecognizeUri: String,
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient
) {
    fun recognizeVoice(voiceInputStream: InputStream): RecognitionResultDto? {
        val uri = UriComponentsBuilder
            .fromUri(URI.create(ycVoiceRecognizeUri))
            .queryParam("folderId", ycFolderId)
            .queryParam("lang", "ru-RU")
            .build()
            .toUri()

        val request = HttpRequest.newBuilder()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Api-Key $ycToken")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .POST(BodyPublishers.ofInputStream { voiceInputStream })
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == HttpStatus.OK.value()) {
            return objectMapper.readValue(response.body(), RecognitionResultDto::class.java)
        }

        return null
    }
}