package com.codenrock.hackwagon.forest.inventory.telegram.service.processor

import com.codenrock.hackwagon.forest.inventory.telegram.domain.message.MessageParameters
import org.telegram.telegrambots.meta.api.objects.Update

interface BotUpdateProcessor {
    fun isMine(update: Update): Boolean
    fun process(update: Update): MessageParameters?
}