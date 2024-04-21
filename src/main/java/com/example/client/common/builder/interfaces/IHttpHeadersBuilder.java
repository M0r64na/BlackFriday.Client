package com.example.client.common.builder.interfaces;

import com.example.client.common.dto.LoginRequestDto;
import org.springframework.http.HttpHeaders;

public interface IHttpHeadersBuilder {
    HttpHeaders buildHeaders();
    HttpHeaders buildHeaders(LoginRequestDto loginRequest);
}