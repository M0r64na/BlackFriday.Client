package com.example.client.web;

import com.example.client.common.builder.interfaces.IExceptionResponseBuilder;
import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constant.RestTemplateRequest;
import com.example.client.common.constant.RestTemplateResponse;
import com.example.client.common.dto.LoginRequestDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/logout")
public class LogoutRestClient {
    private static final String LOGOUT_URL = RestTemplateRequest.BASE_URL + RestTemplateRequest.LOGOUT_ENDPOINT;
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;
    private final IExceptionResponseBuilder exceptionResponseBuilder;

    public LogoutRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder, IExceptionResponseBuilder exceptionResponseBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
        this.exceptionResponseBuilder = exceptionResponseBuilder;
    }

    @PostMapping
    public ResponseEntity<?> logoutCurrentUser(@RequestBody(required = false) LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(LOGOUT_URL, HttpMethod.POST,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.LOGGED_OUT_USER_MESSAGE);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }
}