package com.example.client.builder.interfaces;

import com.example.client.dto.LoginRequestDto;
import org.springframework.http.HttpHeaders;

public interface IHttpHeadersBuilder {
    HttpHeaders buildHeaders();
    HttpHeaders buildHeaders(LoginRequestDto loginRequest);
}