package com.example.client.dto;

import common.dto.ProductDto;
import jakarta.annotation.Nullable;

public record ProductCreationDto(@Nullable LoginRequestDto loginRequest, ProductDto product) { }