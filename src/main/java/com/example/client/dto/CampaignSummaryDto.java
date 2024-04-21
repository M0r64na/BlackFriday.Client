package com.example.client.dto;

import java.util.Map;

public record CampaignSummaryDto(Map<String, Double> productNamesAndDiscountPercentages) { }