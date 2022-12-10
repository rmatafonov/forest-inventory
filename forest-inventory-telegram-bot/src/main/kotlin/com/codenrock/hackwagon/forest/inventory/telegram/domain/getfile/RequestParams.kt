package com.codenrock.hackwagon.forest.inventory.telegram.domain.getfile

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestParams(
    @field:JsonProperty("file_id") val fileId: String
)
