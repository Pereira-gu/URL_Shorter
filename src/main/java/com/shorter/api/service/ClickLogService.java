package com.shorter.api.service;

import com.shorter.api.entity.ClickLog;
import com.shorter.api.entity.ShortLink;
import com.shorter.api.repository.ClickLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ClickLogService {

    private final ClickLogRepository clickLogRepository;

    public ClickLogService(ClickLogRepository clickLogRepository) {
        this.clickLogRepository = clickLogRepository;
    }

    // O Spring executará este método em uma Thread separada automaticamente!
    @Async
    public void logClick(ShortLink shortLink, String userAgent) {
        String browser = parseBrowser(userAgent);
        String device = parseDevice(userAgent);

        ClickLog log = new ClickLog(shortLink, browser, device);
        clickLogRepository.save(log);
    }

    // Parser simples do User-Agent para identificar o navegador
    private String parseBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();

        if (ua.contains("edg")) return "Edge";
        if (ua.contains("chrome") && !ua.contains("chromium")) return "Chrome";
        if (ua.contains("safari") && !ua.contains("chrome")) return "Safari";
        if (ua.contains("firefox")) return "Firefox";
        return "Other";
    }

    // Parser simples do User-Agent para identificar o dispositivo
    private String parseDevice(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();

        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone")) {
            return "Mobile";
        }
        return "Desktop";
    }

    public com.shorter.api.dto.UrlAnalyticsSummaryDTO getLinkAnalytics(ShortLink shortLink) {
        long totalClicks = clickLogRepository.countByShortLinkId(shortLink.getId());
        var browserStats = clickLogRepository.countClicksByBrowser(shortLink.getId());
        var deviceStats = clickLogRepository.countClicksByDevice(shortLink.getId());

        return new com.shorter.api.dto.UrlAnalyticsSummaryDTO(totalClicks, browserStats, deviceStats);
    }
}