package com.shorter.api.controller;

import com.shorter.api.dto.UrlRequest;
import com.shorter.api.entity.ShortLink;
import com.shorter.api.service.ClickLogService;
import com.shorter.api.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class UrlController {

    private final ShortLinkService shortLinkService;
    private final ClickLogService clickLogService;

    public UrlController(ShortLinkService shortLinkService, ClickLogService clickLogService) {
        this.shortLinkService = shortLinkService;
        this.clickLogService = clickLogService;
    }

    // Endpoint para criar o link encurtado
    @PostMapping("/api/urls")
    public ResponseEntity<Map<String, String>> createShortLink(@Valid @RequestBody UrlRequest request) {
        ShortLink shortLink = shortLinkService.createShortLink(request.getUrl());

        // Retorna um JSON amigável contendo o código gerado
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "shortCode", shortLink.getShortCode(),
                "originalUrl", shortLink.getOriginalUrl()
        ));
    }

    // Endpoint que faz o redirecionamento e captura o analytics
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletRequest request) {
        return shortLinkService.getOriginalUrl(shortCode)
                .map(shortLink -> {
                    // Captura o cabeçalho User-Agent da requisição HTTP
                    String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

                    // Executa de forma ASSÍNCRONA (não trava a resposta do usuário)
                    clickLogService.logClick(shortLink, userAgent);

                    // Faz o redirecionamento HTTP 302 injetando a URL original no Location Header
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .header(HttpHeaders.LOCATION, shortLink.getOriginalUrl())
                            .<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}