package com.example.client.web;

import com.example.client.common.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.common.constants.RestTemplateRequest;
import com.example.client.common.constants.RestTemplateResponse;
import com.example.client.common.dto.LoginRequestDto;
import com.example.client.common.dto.ProductCreationDto;
import common.dto.ErrorResponseDto;
import common.dto.ProductDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = RestTemplateRequest.PRODUCTS_ENDPOINT)
public class ProductRestClient {
    private static final String PRODUCTS_URL = RestTemplateRequest.BASE_URL + RestTemplateRequest.PRODUCTS_ENDPOINT;
    private final RestTemplate restTemplate;
    private final IHttpHeadersBuilder httpHeadersBuilder;

    public ProductRestClient(RestTemplate restTemplate, IHttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(value = "campaign", required = false) boolean campaign,
                                         @RequestParam(value = "name", required = false) String name) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            if(campaign) return this.restTemplate.exchange(PRODUCTS_URL, HttpMethod.GET, httpEntity, String.class);

            if(name == null) return this.restTemplate.exchange(PRODUCTS_URL, HttpMethod.GET, httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(PRODUCTS_URL)
                    .queryParam("name", name);
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
    public ResponseEntity<?> createProduct(@RequestBody ProductCreationDto productCreation) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(productCreation.loginRequest());
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productCreation.product(), httpHeaders);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(PRODUCTS_URL, HttpMethod.POST, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.CREATED_PRODUCT_MESSAGE);
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
    public ResponseEntity<?> updateProduct(@RequestBody ProductCreationDto productCreation,
                                           @RequestParam(value = "prevName") String prevName) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(productCreation.loginRequest());
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productCreation.product(), httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(PRODUCTS_URL)
                    .queryParam("prevName", prevName);
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
    public ResponseEntity<?> deleteProduct(@RequestBody(required = false) LoginRequestDto loginRequest,
                                           @RequestParam(value = "id") String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(PRODUCTS_URL)
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body(RestTemplateResponse.DELETED_PRODUCT_MESSAGE);
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