package com.shorter.api.repository;

import com.shorter.api.entity.ClickLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long> {

    // Método para contar quantos cliques um link específico teve (útil para o analytics básico)
    long countByShortLinkId(UUID shortLinkId);
}