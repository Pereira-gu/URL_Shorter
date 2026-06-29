package com.shorter.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UrlRequest {

    @NotBlank(message = "A URL original não pode estar em branco")
    @URL(message = "Por favor, insira uma URL válida")
    private String url;

    // Construtor padrão
    public UrlRequest() {}

    // Getter e Setter
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}