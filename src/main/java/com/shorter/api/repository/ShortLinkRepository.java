package com.shorter.api.repository;

import com.shorter.api.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, UUID> {

    // O Spring Data gera a consulta SELECT baseada no nome do método automaticamente!
    Optional<ShortLink> findByShortCode(String shortCode);

    // Método auxiliar para verificarmos se um código gerado já existe no banco
    boolean existsByShortCode(String shortCode);
}