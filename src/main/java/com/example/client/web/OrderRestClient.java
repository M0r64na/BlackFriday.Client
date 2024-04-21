package com.example.client.web;

import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constants.RestTemplateRequest;
import com.example.client.common.constants.RestTemplateResponse;
import com.example.client.common.dto.LoginRequestDto;
import com.example.client.common.dto.OrderCreationDto;
import com.example.client.common.dto.OrderSummaryDto;
import common.dto.ErrorResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = RestTemplateRequest.ORDERS_ENDPOINT)
public class OrderRestClient {
    private static final String ORDERS_URL = RestTemplateRequest.BASE_URL + RestTemplateRequest.ORDERS_ENDPOINT;
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public OrderRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestBody(required = false) LoginRequestDto loginRequest,
                                       @RequestParam(name = "id", required = false) String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            if(id == null) return this.restTemplate.exchange(ORDERS_URL, HttpMethod.GET, httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(ORDERS_URL)
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

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
    public ResponseEntity<?> placeOrder(@RequestBody OrderCreationDto orderCreation) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(orderCreation.loginRequest());
        HttpEntity<OrderSummaryDto> httpEntity = new HttpEntity<>(orderCreation.orderSummary(), httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(ORDERS_URL, HttpMethod.POST, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.PLACED_ORDER_MESSAGE);
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