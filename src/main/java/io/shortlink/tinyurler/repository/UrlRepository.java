package io.shortlink.tinyurler.repository;

import io.shortlink.tinyurler.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping,Long> {
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
    Optional<UrlMapping> findByShortCode(String shortCode);
}
