package com.example.client.web;

import com.example.client.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.dto.LoginRequestDto;
import com.example.client.dto.ProductCreationDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<String> getProducts(@RequestParam(value = "campaign", required = false) boolean campaign,
                                              @RequestParam(value = "name", required = false) String name) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            if(campaign) return this.restTemplate.exchange("http://localhost:8080/campaigns", HttpMethod.GET, httpEntity, String.class);

            if(name == null) return this.restTemplate.exchange("http://localhost:8080/products", HttpMethod.GET, httpEntity, String.class);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/products")
                    .queryParam("name", name);
            String url = uriComponentsBuilder.encode().toUriString();

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
    public ResponseEntity<String> createProduct(@RequestBody ProductCreationDto productCreation) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(productCreation.loginRequest());
        HttpEntity<Object> httpEntity = new HttpEntity<>(productCreation.product(), httpHeaders);

        try {
            return this.restTemplate.exchange("http://localhost:8080/products", HttpMethod.POST, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody ProductCreationDto productCreation,
                                                @RequestParam(value = "prevName") String prevName) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(productCreation.loginRequest());
        HttpEntity<Object> httpEntity = new HttpEntity<>(productCreation.product(), httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/products")
                    .queryParam("prevName", prevName);
            String url = uriComponentsBuilder.encode().toUriString();

            return this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody(required = false) LoginRequestDto loginRequest,
                                             @RequestParam(value = "id") String id) {
        HttpHeaders httpHeaders = this.httpHeadersBuilder.buildHeaders(loginRequest);
        HttpEntity<Object> httpEntity = new HttpEntity<>(loginRequest, httpHeaders);

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8080/products")
                    .queryParam("id", id);
            String url = uriComponentsBuilder.toUriString();

            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(httpHeaders)
                    .body("Successfully deleted product");
        }
        catch (HttpServerErrorException | HttpClientErrorException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(httpHeaders)
                    .body(ex.getMessage());
        }
    }
}