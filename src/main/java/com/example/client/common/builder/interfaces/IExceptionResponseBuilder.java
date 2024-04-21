package com.example.client.common.builder.interfaces;

import common.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

public interface IExceptionResponseBuilder {
    ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatusCodeException ex, HttpHeaders httpHeaders);
}