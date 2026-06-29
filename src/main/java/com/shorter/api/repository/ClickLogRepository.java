package com.shorter.api.repository;

import com.shorter.api.dto.BrowserAnalyticsDTO;
import com.shorter.api.dto.DeviceAnalyticsDTO;
import com.shorter.api.entity.ClickLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long> {

    long countByShortLinkId(UUID shortLinkId);

    // Consulta customizada para agrupar cliques por Navegador
    @Query("SELECT new com.shorter.api.dto.BrowserAnalyticsDTO(c.browser, COUNT(c)) " +
            "FROM ClickLog c WHERE c.shortLink.id = :linkId " +
            "GROUP BY c.browser")
    List<BrowserAnalyticsDTO> countClicksByBrowser(@Param("linkId") UUID linkId);

    // Consulta customizada para agrupar cliques por Dispositivo
    @Query("SELECT new com.shorter.api.dto.DeviceAnalyticsDTO(c.device, COUNT(c)) " +
            "FROM ClickLog c WHERE c.shortLink.id = :linkId " +
            "GROUP BY c.device")
    List<DeviceAnalyticsDTO> countClicksByDevice(@Param("linkId") UUID linkId);
}