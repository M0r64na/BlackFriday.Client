package com.example.client.web;

import com.example.client.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.dto.CampaignCreationDto;
import com.example.client.dto.LoginRequestDto;
import common.dto.ErrorResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/campaigns")
public class CampaignRestClient {
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public CampaignRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentCampaign(@RequestBody(required = false) LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            return this.restTemplate.exchange("http://localhost:8080/campaigns", HttpMethod.GET,
                    httpEntity, String.class);
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
    public ResponseEntity<?> startNewCampaign(@RequestBody CampaignCreationDto campaignCreation) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(campaignCreation.loginRequest());
        HttpEntity<?> httpEntity = new HttpEntity<>(campaignCreation.campaignSummary(), httpHeaders);

        try {
            ResponseEntity<?> response = this.restTemplate.exchange("http://localhost:8080/campaigns", HttpMethod.POST,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body("Successfully placed order");
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
    public ResponseEntity<?> stopCurrentCampaign(@RequestBody(required = false) LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<?> response = this.restTemplate.exchange("http://localhost:8080/campaigns", HttpMethod.PUT,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body("Successfully stopped current campaign");
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