package com.example.client.common.dto;

import jakarta.annotation.Nullable;

public record OrderCreationDto(@Nullable LoginRequestDto loginRequest, OrderSummaryDto orderSummary) { }