package com.example.Eksamen.restDto

import org.springframework.http.ResponseEntity
import java.net.URI


object RestResponseFactory {

    fun <T> notFound(message: String): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(404).body(
                WrappedResponse<T>(code = 404, message = message)
                        .validated())
    }


    fun <T> payload(httpCode: Int, data: T): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse(code = httpCode, data = data)
                        .validated())
    }


}