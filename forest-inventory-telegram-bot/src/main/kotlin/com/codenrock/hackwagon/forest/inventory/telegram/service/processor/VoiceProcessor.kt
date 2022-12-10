package com.codenrock.hackwagon.forest.inventory.telegram.service.processor

import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.MessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.TextMessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.service.TelegramRestApiClient
import com.codenrock.hackwagon.forest.inventory.telegram.service.YandexCloudSpeechRecognitionRestApiClient
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class VoiceProcessor(
    private val telegramRestApiClient: TelegramRestApiClient,
    private val speechRecognizer: YandexCloudSpeechRecognitionRestApiClient
) : BotUpdateProcessor {
    override fun isMine(update: Update): Boolean = update.message?.hasVoice() == true

    override fun process(update: Update): MessageParameters? {
        try {
            val filePath = telegramRestApiClient.getVoiceFilePathForDownload(update.message.voice.fileId)
            filePath?.let { fp ->
                val voiceInputStream = telegramRestApiClient.downloadFile(fp)
                voiceInputStream?.let { voice ->
                    return speechRecognizer.recognizeVoice(voice)?.let {
                        TextMessageParameters(
                            who = update.message.from.id.toString(),
                            what = it.result
                        )
                    }
                } ?: run {
                    return formatErrorReply(update, "Не получилось скачать голос с серверов Telegram")
                }
            } ?: run {
                return formatErrorReply(update, "Не нашел файл на серверах Telegram")
            }
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