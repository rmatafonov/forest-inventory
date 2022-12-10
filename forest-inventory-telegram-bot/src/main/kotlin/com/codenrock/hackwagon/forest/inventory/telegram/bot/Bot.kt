package com.codenrock.hackwagon.forest.inventory.telegram.bot

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.BotToken
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.MessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.service.processor.BotUpdateProcessor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class Bot(
    @BotToken private val botToken: String,
    private val updateProcessors: List<BotUpdateProcessor>,
    @Value("com.codenrock.hackwagon.forest.inventory.telegram.bot.name")
    private val botName: String
) : TelegramLongPollingBot() {
    private val log = LoggerFactory.getLogger(Bot::class.java)

    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        log.info(update.toString())
        updateProcessors.forEach { processor ->
            if (processor.isMine(update)) {
                processor.process(update)?.let { sendMessage(it) }
            }
        }
    }

    private fun sendMessage(textMessageParameters: MessageParameters) {
        try {
            val executed = execute(textMessageParameters.getSendMessage())
            log.info("The message has been sent: $executed")
        } catch (e: TelegramApiException) {
            log.error("Error sending the message '$textMessageParameters", e)
        }
    }
}