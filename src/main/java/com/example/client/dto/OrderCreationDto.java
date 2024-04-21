package com.example.client.dto;

import jakarta.annotation.Nullable;

public record OrderCreationDto(@Nullable LoginRequestDto loginRequest, OrderSummaryDto orderSummary) { }