package com.codenrock.hackwagon.forest.inventory.telegram.domain.message

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

data class TextMessageParameters(
    val who: String,
    val what: String,
    val keyboardMarkup: InlineKeyboardMarkup? = null
) : MessageParameters {
    override fun getSendMessage(): SendMessage {
        val sendMessageBuilder = SendMessage.builder()
            .chatId(who)
            .text(what)

        keyboardMarkup?.let { sendMessageBuilder.replyMarkup(keyboardMarkup) }

        return sendMessageBuilder.build()
    }
}
