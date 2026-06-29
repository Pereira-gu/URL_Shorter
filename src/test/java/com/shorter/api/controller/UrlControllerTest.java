package com.shorter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorter.api.dto.UrlRequest;
import com.shorter.api.entity.ShortLink;
import com.shorter.api.service.ClickLogService;
import com.shorter.api.service.ShortLinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShortLinkService shortLinkService;

    @MockitoBean
    private ClickLogService clickLogService;

    @Test
    @DisplayName("Deve retornar 21 Created e o JSON do link encurtado ao enviar URL válida")
    void deveCriarShortLinkComSucesso() throws Exception {
        // Arrange
        UrlRequest request = new UrlRequest();
        request.setUrl("https://github.com/gustavodspereira");

        ShortLink mockLink = new ShortLink("https://github.com/gustavodspereira", "aB8z9X");
        when(shortLinkService.createShortLink(anyString())).thenReturn(mockLink);

        // Act & Assert
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("aB8z9X"))
                .andExpect(jsonPath("$.originalUrl").value("https://github.com/gustavodspereira"));

        verify(shortLinkService, times(1)).createShortLink(anyString());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao enviar uma URL inválida")
    void deveRetornarErroAoCriarComUrlInvalida() throws Exception {
        // Arrange
        UrlRequest request = new UrlRequest();
        request.setUrl("url-invalida-com-erro");

        // Act & Assert
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(shortLinkService, never()).createShortLink(anyString());
    }

    @Test
    @DisplayName("Deve retornar 302 Found e redirecionar para a URL original")
    void deveRedirecionarComSucesso() throws Exception {
        // Arrange
        String code = "aB8z9X";
        ShortLink mockLink = new ShortLink("https://github.com/gustavodspereira", code);
        when(shortLinkService.getOriginalUrl(code)).thenReturn(Optional.of(mockLink));

        // Act & Assert
        mockMvc.perform(get("/" + code)
                        .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://github.com/gustavodspereira"));

        // Garante que o serviço assíncrono de log foi chamado no redirecionamento
        verify(clickLogService, times(1)).logClick(eq(mockLink), anyString());
    }
}