package com.example.client.web;

import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.dto.LoginRequestDto;
import common.dto.ErrorResponseDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/users")
public class UserRestClient {
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public UserRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestBody(required = false) LoginRequestDto loginRequest,
                                      @RequestParam(value = "username", required = false) String username) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            if(username == null) return this.restTemplate.exchange("http://localhost:8080/users", HttpMethod.GET,
                    httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/users")
                    .queryParam("username", username);
            String url = uriComponentsBuilder.encode().toUriString();

            return this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            ErrorResponseDto errorResponse = ex.getResponseBodyAs(ErrorResponseDto.class);

            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:8080/users", HttpMethod.POST,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body("Successfully registered");
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            ErrorResponseDto errorResponse = ex.getResponseBodyAs(ErrorResponseDto.class);

            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(errorResponse);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody(required = false) LoginRequestDto loginRequest,
                                        @RequestParam(value = "password") String password) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/users")
                    .queryParam("password", password);
            String url = uriComponentsBuilder.encode().toUriString();

            return this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            ErrorResponseDto errorResponse = ex.getResponseBodyAs(ErrorResponseDto.class);

            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(errorResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody(required = false) LoginRequestDto loginRequest,
                                        @RequestParam(value = "id") String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/users")
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body("Successfully deleted user profile");
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            ErrorResponseDto errorResponse = ex.getResponseBodyAs(ErrorResponseDto.class);

            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(errorResponse);
        }
    }
}