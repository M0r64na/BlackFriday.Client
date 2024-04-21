package com.example.client.common.dto;

import java.util.Map;

public record CampaignSummaryDto(Map<String, Double> productNamesAndDiscountPercentages) { }