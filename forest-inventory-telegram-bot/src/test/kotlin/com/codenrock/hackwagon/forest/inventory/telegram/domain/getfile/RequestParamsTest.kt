package com.codenrock.hackwagon.forest.inventory.telegram.domain.getfile

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.http.client.MockClientHttpRequest

internal class RequestParamsTest {
    @Test
    fun test() {
        val output = MockClientHttpRequest()
        MappingJackson2HttpMessageConverter().write(RequestParams("123"), MediaType.APPLICATION_JSON, output)
        println(output.bodyAsString)
    }
}