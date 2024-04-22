package com.example.client.common.constant;

public final class RestTemplateRequest {
    private RestTemplateRequest() {}

    public static final int PORT_NUMBER = 8080;
    public static final String BASE_URL = "http://localhost:" + PORT_NUMBER;
    public static final String USERS_ENDPOINT = "/users";
    public static final String PRODUCTS_ENDPOINT = "/products";
    public static final String ORDERS_ENDPOINT = "/orders";
    public static final String CAMPAIGNS_ENDPOINT = "/campaigns";
}