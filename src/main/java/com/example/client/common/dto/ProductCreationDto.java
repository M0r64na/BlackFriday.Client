package com.example.client.common.dto;

import common.dto.ProductDto;
import jakarta.annotation.Nullable;

public record ProductCreationDto(@Nullable LoginRequestDto loginRequest, ProductDto product) { }