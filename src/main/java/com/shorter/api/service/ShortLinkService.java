package com.shorter.api.service;

import com.shorter.api.entity.ShortLink;
import com.shorter.api.repository.ShortLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;

    // Caracteres usados para gerar o código curto (Base62)
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    // Injeção de dependência via construtor (Boa prática recomendada pelo Spring)
    public ShortLinkService(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    @Transactional
    public ShortLink createShortLink(String originalUrl) {
        String shortCode;

        // Loop de segurança para garantir que o código gerado é único no banco
        do {
            shortCode = generateRandomCode();
        } while (shortLinkRepository.existsByShortCode(shortCode));

        ShortLink shortLink = new ShortLink(originalUrl, shortCode);
        return shortLinkRepository.save(shortLink);
    }

    public Optional<ShortLink> getOriginalUrl(String shortCode) {
        return shortLinkRepository.findByShortCode(shortCode);
    }

    // Algoritmo simples e eficiente para geração de hashes alfanuméricos
    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }
        return code.toString();
    }
}