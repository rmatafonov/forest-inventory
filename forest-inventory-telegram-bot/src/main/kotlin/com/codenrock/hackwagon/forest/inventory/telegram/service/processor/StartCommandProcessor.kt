package com.codenrock.hackwagon.forest.inventory.telegram.service.processor

import com.codenrock.hackwagon.forest.inventory.telegram.annotation.BotWelcomeMessage
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.MessageParameters
import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.TextMessageParameters
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class StartCommandProcessor(
    @BotWelcomeMessage private val welcomeMessage: String
) : BotUpdateProcessor {
    override fun isMine(update: Update): Boolean = update.message?.let { it.isCommand && it.text == "/start" } ?: false

    override fun process(update: Update): MessageParameters? = TextMessageParameters(
        who = update.message.from.id.toString(),
        what = welcomeMessage
    )
}