package com.codenrock.hackwagon.forest.inventory.telegram

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.*
import com.codenrock.hackwagon.forest.inventory.telegram.bot.Bot
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.net.http.HttpClient
import java.nio.charset.StandardCharsets
import java.time.Duration

fun main(args: Array<String>) {
    val applicationContext = runApplication<BotLauncher>(*args)
    val bot = applicationContext.getBean(Bot::class.java)
    TelegramBotsApi(DefaultBotSession::class.java).also { it.registerBot(bot) }
}

@SpringBootApplication(scanBasePackages = ["com.codenrock.hackwagon.forest.inventory.telegram"])
class BotLauncher {
    @Bean
    @BotToken
    fun botToken() = System.getenv("FOREST_INVENTORY_BOT_TOKEN")
        ?: throw IllegalStateException("Please specify FOREST_INVENTORY_BOT_TOKEN env variable")

    @Bean
    @YCToken
    fun ycToken() = System.getenv("FOREST_INVENTORY_YC_TOKEN")
        ?: throw IllegalStateException("Please specify FOREST_INVENTORY_YC_TOKEN env variable")

    @Bean
    @YCFolderId
    fun ycFolderId() = System.getenv("FOREST_INVENTORY_YC_FOLDER_ID")
        ?: throw IllegalStateException("Please specify FOREST_INVENTORY_YC_FOLDER_ID env variable")

    @Bean
    fun httpClient(): HttpClient =
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build()

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()

    @Bean
    @BotWelcomeMessage
    fun welcomeMessage() = IOUtils.toString(
        ClassPathResource("welcomeMessage.txt").inputStream,
        StandardCharsets.UTF_8
    )

    @Bean
    @VoiceRecognitionInlineKeyboardMarkup
    fun voiceRecognitionResultKeyboardMarkup(): InlineKeyboardMarkup = InlineKeyboardMarkup.builder()
        .keyboardRow(
            listOf(
                InlineKeyboardButton.builder().text("??????????????????").callbackData("save_recognized_text").build()
            )
        )
        .build()
}