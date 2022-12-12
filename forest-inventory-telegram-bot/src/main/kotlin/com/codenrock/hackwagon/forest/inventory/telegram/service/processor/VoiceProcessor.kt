package com.codenrock.hackwagon.forest.inventory.telegram.service.processor

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.VoiceRecognitionInlineKeyboardMarkup
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.MessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.TextMessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.service.TelegramRestApiClient
import com.codenrock.hackwagon.forest.inventory.telegram.service.YandexCloudSpeechRecognitionRestApiClient
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

@Service
class VoiceProcessor(
    private val telegramRestApiClient: TelegramRestApiClient,
    private val speechRecognizer: YandexCloudSpeechRecognitionRestApiClient,
    @VoiceRecognitionInlineKeyboardMarkup private val keyboardMarkup: InlineKeyboardMarkup
) : BotUpdateProcessor {
    override fun isMine(update: Update): Boolean = update.message?.hasVoice() == true

    override fun process(update: Update): MessageParameters? {
        try {
            val voiceInputStream = telegramRestApiClient.downloadFile(update.message.voice.fileId)
                ?: return formatErrorReply(update, "Не получилось скачать голос с серверов Telegram")

            val recognizedVoice = speechRecognizer.recognizeVoice(voiceInputStream)
                ?: return formatErrorReply(update, "Не удалось распознать текст из голосового сообщения")

            // TODO: request to the analyzer service

            return TextMessageParameters(
                who = update.message.from.id.toString(),
                what = recognizedVoice.result,
                keyboardMarkup = keyboardMarkup
            )
        } catch (e: Throwable) {
            return formatErrorReply(update, e.stackTraceToString())
        }
    }

    private fun formatErrorReply(update: Update, cause: String?): TextMessageParameters {
        return TextMessageParameters(
            who = update.message.from.id.toString(),
            what = """
                Извини, не смог распознать запись :(
                Обратись, пожалуйста, в поддержку. Вот причина: $cause 
            """.trimIndent()
        )
    }
}