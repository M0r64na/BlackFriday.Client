package com.example.client.common.dto;

import jakarta.annotation.Nullable;

public record CampaignCreationDto(@Nullable LoginRequestDto loginRequest, CampaignSummaryDto campaignSummary) { }