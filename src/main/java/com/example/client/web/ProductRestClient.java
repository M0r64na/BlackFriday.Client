package com.example.client.web;

import com.example.client.builder.interfaces.IHttpHeadersBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/products")
public class ProductRestClient {
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public ProductRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<String> getAllProducts() {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        // TODO check for name req param => url
        try {
            return restTemplate.exchange("http://localhost:8080/products", HttpMethod.GET, httpEntity, String.class);
        }
        catch (HttpServerErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }
}