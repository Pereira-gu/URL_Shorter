package com.shorter.api.service;

import com.shorter.api.entity.ShortLink;
import com.shorter.api.repository.ShortLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    private String originalUrl;

    @BeforeEach
    void setUp() {
        originalUrl = "https://github.com/gustavodspereira";
    }

    @Test
    @DisplayName("Deve criar um link encurtado com sucesso com código único")
    void deveCriarShortLinkComSucesso() {
        // Arrange
        when(shortLinkRepository.existsByShortCode(anyString())).thenReturn(false);
        when(shortLinkRepository.save(any(ShortLink.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ShortLink resultado = shortLinkService.createShortLink(originalUrl);

        // Assert
        assertNotNull(resultado);
        assertEquals(originalUrl, resultado.getOriginalUrl());
        assertNotNull(resultado.getShortCode());
        assertEquals(6, resultado.getShortCode().length());

        verify(shortLinkRepository, times(1)).save(any(ShortLink.class));
    }

    @Test
    @DisplayName("Deve buscar a URL original com base no código curto")
    void deveBuscarUrlOriginalPorCodigo() {
        // Arrange
        String code = "abc123";
        ShortLink mockLink = new ShortLink(originalUrl, code);
        when(shortLinkRepository.findByShortCode(code)).thenReturn(Optional.of(mockLink));

        // Act
        Optional<ShortLink> resultado = shortLinkService.getOriginalUrl(code);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(originalUrl, resultado.get().getOriginalUrl());
        assertEquals(code, resultado.get().getShortCode());
    }
}