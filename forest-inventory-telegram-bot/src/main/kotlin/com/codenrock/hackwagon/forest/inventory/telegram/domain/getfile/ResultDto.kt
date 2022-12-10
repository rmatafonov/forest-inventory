package com.codenrock.hackwagon.forest.inventory.telegram.domain.getfile

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResultDto(
    @field:JsonProperty("file_id") val fileId: String,
    @field:JsonProperty("file_unique_id") val fileUniqueId: String,
    @field:JsonProperty("file_size") val fileSize: Int,
    @field:JsonProperty("file_path") val filePath: String
)
