package com.shorter.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_click_logs")
public class ClickLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "short_link_id", nullable = false)
    private ShortLink shortLink;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    private String browser;
    private String device;

    // Construtor padrão exigido pela JPA
    public ClickLog() {}

    // Construtor auxiliar
    public ClickLog(ShortLink shortLink, String browser, String device) {
        this.shortLink = shortLink;
        this.browser = browser;
        this.device = device;
        this.clickedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ShortLink getShortLink() { return shortLink; }
    public void setShortLink(ShortLink shortLink) { this.shortLink = shortLink; }

    public LocalDateTime getClickedAt() { return clickedAt; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
}