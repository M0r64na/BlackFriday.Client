package com.example.client.web;

import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constants.RestTemplateRequest;
import com.example.client.common.constants.RestTemplateResponse;
import com.example.client.common.dto.CampaignCreationDto;
import com.example.client.common.dto.CampaignSummaryDto;
import com.example.client.common.dto.LoginRequestDto;
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
@RequestMapping(value = RestTemplateRequest.CAMPAIGNS_ENDPOINT)
public class CampaignRestClient {
    private static final String CAMPAIGNS_URL = RestTemplateRequest.BASE_URL + RestTemplateRequest.CAMPAIGNS_ENDPOINT;
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
            return this.restTemplate.exchange(CAMPAIGNS_URL, HttpMethod.GET,
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
        HttpEntity<CampaignSummaryDto> httpEntity = new HttpEntity<>(campaignCreation.campaignSummary(), httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(CAMPAIGNS_URL, HttpMethod.POST,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.STARTED_NEW_CAMPAIGN_MESSAGE);
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
            ResponseEntity<String> response = this.restTemplate.exchange(CAMPAIGNS_URL, HttpMethod.PUT,
                    httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.STOPPED_CURRENT_CAMPAIGN_MESSAGE);
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