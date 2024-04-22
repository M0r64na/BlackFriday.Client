package com.example.client.web;

import com.example.client.common.builder.interfaces.IExceptionResponseBuilder;
import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constant.RestTemplateRequest;
import com.example.client.common.constant.RestTemplateResponse;
import com.example.client.common.dto.CampaignCreationDto;
import com.example.client.common.dto.CampaignSummaryDto;
import com.example.client.common.dto.LoginRequestDto;
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
    private final IExceptionResponseBuilder exceptionResponseBuilder;

    public CampaignRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder, IExceptionResponseBuilder exceptionResponseBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
        this.exceptionResponseBuilder = exceptionResponseBuilder;
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
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
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
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
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
            return this.exceptionResponseBuilder.buildErrorResponse(ex, httpHeaders);
        }
    }
}