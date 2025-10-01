package io.shortlink.tinyurler.service;

import io.shortlink.tinyurler.dto.ShortenUrlRequest;
import io.shortlink.tinyurler.dto.ShortenUrlResponse;
import jakarta.validation.Valid;

public interface UrlService {
    ShortenUrlResponse shortenUrl(@Valid ShortenUrlRequest request);

    String getOriginalUrl(String shortCode);
}
