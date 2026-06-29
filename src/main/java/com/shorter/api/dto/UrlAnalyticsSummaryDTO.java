package com.shorter.api.dto;

import java.util.List;

public record UrlAnalyticsSummaryDTO(
        Long totalClicks,
        List<BrowserAnalyticsDTO> clicksByBrowser,
        List<DeviceAnalyticsDTO> clicksByDevice
) {}