package com.example.client.web;

import com.example.client.common.builder.interfaces.IExceptionResponseBuilder;
import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constant.RestTemplateRequest;
import com.example.client.common.constant.RestTemplateResponse;
import com.example.client.common.dto.LoginRequestDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = RestTemplateRequest.USERS_ENDPOINT)
public class UserRestClient {
    private static final String USERS_URL = RestTemplateRequest.BASE_URL + RestTemplateRequest.USERS_ENDPOINT;
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;
    private final IExceptionResponseBuilder exceptionResponseBuilder;

    public UserRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder, IExceptionResponseBuilder exceptionResponseBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
        this.exceptionResponseBuilder = exceptionResponseBuilder;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestBody(required = false) LoginRequestDto loginRequest,
                                      @RequestParam(value = "username", required = false) String username) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            if(username == null) return this.restTemplate.exchange(USERS_URL, HttpMethod.GET,
                    httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(USERS_URL)
                    .queryParam("username", username);
            String url = uriComponentsBuilder.encode().toUriString();

            return this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(loginRequest, httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(USERS_URL, HttpMethod.POST,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.REGISTERED_USER_MESSAGE);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody(required = false) LoginRequestDto loginRequest,
                                        @RequestParam(value = "password") String password) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(USERS_URL)
                    .queryParam("password", password);
            String url = uriComponentsBuilder.encode().toUriString();

            return this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody(required = false) LoginRequestDto loginRequest,
                                        @RequestParam(value = "id") String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(USERS_URL)
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.DELETED_USER_MESSAGE);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }
}