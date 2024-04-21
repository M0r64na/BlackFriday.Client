package com.example.client.common.dto;

import java.util.Map;

public record OrderSummaryDto(Map<String, Integer> productNamesAndQuantities) { }