package com.example.client.dto;

import java.util.Map;

public record OrderSummaryDto(Map<String, Integer> productNamesAndQuantities) { }