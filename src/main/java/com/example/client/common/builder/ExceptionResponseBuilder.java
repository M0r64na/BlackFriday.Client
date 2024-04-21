package com.example.client.common.builder;

import com.example.client.common.builder.interfaces.IExceptionResponseBuilder;
import common.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class ExceptionResponseBuilder implements IExceptionResponseBuilder {
    @Override
    public ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatusCodeException ex, HttpHeaders httpHeaders) {
        ErrorResponseDto errorResponse = ex.getResponseBodyAs(ErrorResponseDto.class);

        return ResponseEntity.status(ex.getStatusCode()).headers(httpHeaders).body(errorResponse);
    }
}