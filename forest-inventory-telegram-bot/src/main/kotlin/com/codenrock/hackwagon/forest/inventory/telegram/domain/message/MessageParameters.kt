package com.codenrock.hackwagon.forest.inventory.telegram.domain.message

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface MessageParameters {
    fun getSendMessage(): SendMessage
}