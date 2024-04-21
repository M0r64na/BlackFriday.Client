package com.example.client.dto;

import jakarta.annotation.Nullable;

public record CampaignCreationDto(@Nullable LoginRequestDto loginRequest, CampaignSummaryDto campaignSummary) { }