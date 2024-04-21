package com.example.client.web;

import com.example.client.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.dto.LoginRequestDto;
import com.example.client.dto.OrderCreationDto;
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
@RequestMapping(value = "/orders")
public class OrderRestClient {
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public OrderRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<String> getOrders(@RequestBody(required = false) LoginRequestDto loginRequest,
                                            @RequestParam(name = "id", required = false) String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<Object> httpEntity = new HttpEntity<>(loginRequest, httpHeaders);

        try {
            if(id == null) return this.restTemplate.exchange("http://localhost:8080/orders", HttpMethod.GET, httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/orders")
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

            return this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderCreationDto orderCreation) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(orderCreation.loginRequest());
        HttpEntity<Object> httpEntity = new HttpEntity<>(orderCreation.orderSummary(), httpHeaders);

        try {
            return this.restTemplate.exchange("http://localhost:8080/orders", HttpMethod.POST, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }
}