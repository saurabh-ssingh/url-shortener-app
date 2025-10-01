package io.shortlink.tinyurler.service.implementation;

import io.shortlink.tinyurler.dto.ShortenUrlRequest;
import io.shortlink.tinyurler.dto.ShortenUrlResponse;
import io.shortlink.tinyurler.entity.UrlMapping;
import io.shortlink.tinyurler.exception.UrlNotFoundException;
import io.shortlink.tinyurler.repository.UrlRepository;
import io.shortlink.tinyurler.service.UrlService;
import io.shortlink.tinyurler.util.UrlExpiryUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Log4j2
@Service
public class UrlServiceImplementation implements UrlService {
  private final UrlRepository urlRepository;

  @Value("${app.base-url:http://localhost:8080}")
  private String baseUrl;

  private static final String BASE62 =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int SHORT_CODE_LENGTH = 8;
  private final Random random = new Random();

  public UrlServiceImplementation(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  /**
   * Creates or retrieves a shortened URL for the given original URL.
   * <p>
   * If the URL already exists and is not expired, returns the existing short URL. - If it
   * exists but is expired, deletes the old mapping and creates a new one. - If it doesn't exist,
   * generates a new short code and stores the mapping.
   * </p>
   *
   * @param request contains the original URL and optional expiry time
   * @return {@link ShortenUrlResponse} with the short URL, original URL, expiry info, and message
   */
  public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {
    Optional<UrlMapping> existing = urlRepository.findByOriginalUrl(request.getOriginalUrl());

    if (existing.isPresent()) {
      UrlMapping mapping = existing.get();
      String message;
      final LocalDateTime expiredAt = mapping.getExpiresAt();
      if (expiredAt != null) {
        message =
            "Short URL already exists and is valid until "
                + expiredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      } else {
        message = "Short URL already exists and does not have an expiry date.";
      }

      // Check expiry with utility/validator
      if (!UrlExpiryUtil.isExpired(mapping.getExpiresAt())) {
        return new ShortenUrlResponse(
            baseUrl + "/url/" + mapping.getShortCode(),
            mapping.getOriginalUrl(),
            message,
            mapping.getExpiresAt());
      }

      // Expired → delete old one (optional cleanup)
      urlRepository.delete(mapping);
    }

    // Generate new one
    final String shortCode = generateUniqueShortCode();

    UrlMapping newMapping = new UrlMapping();
    newMapping.setShortCode(shortCode);
    newMapping.setOriginalUrl(request.getOriginalUrl());
    if (request.getExpiresAt() != null) {
      newMapping.setExpiresAt(request.getExpiresAt());
    }

    urlRepository.save(newMapping);

    return new ShortenUrlResponse(
        baseUrl + "/url/" + shortCode,
        request.getOriginalUrl(),
        "New short URL created successfully",
        request.getExpiresAt());
  }

  @Override
  @Cacheable(value = "shortUrls", key = "#shortCode")
  public String getOriginalUrl(String shortCode) {
    log.info("Cache MISS → fetching original URL from DB for shortCode: {}", shortCode);

    final UrlMapping mapping =
        this.urlRepository
            .findByShortCode(shortCode)
            .orElseThrow(
                () -> {
                  log.warn("ShortCode {} not found in DB", shortCode);
                  return new UrlNotFoundException("Short URL not found.");
                });

    if (UrlExpiryUtil.isExpired(mapping.getExpiresAt())) {
      log.warn("ShortCode {} has expired at {}", shortCode, mapping.getExpiresAt());
      throw new UrlNotFoundException("Short URL has expired.");
    }

    log.info(
        "Successfully fetched original URL [{}] for shortCode: {}",
        mapping.getOriginalUrl(),
        shortCode);
    return mapping.getOriginalUrl();
  }

  /**
   * Generates a unique short code that does not exist in the database.
   *
   * @return A unique short code as a String
   */
  private String generateUniqueShortCode() {
    // Declare a variable to store the generated code
    String code;

    // Keep generating codes until a unique one is found
    do {
      // Generate a random short code
      code = randomCode();
      // Check if the generated code already exists in the repository
    } while (urlRepository.findByShortCode(code).isPresent());

    // Return the unique short code
    return code;
  }

  /**
   * Generates a random alphanumeric code of length SHORT_CODE_LENGTH using BASE62 characters.
   *
   * @return Randomly generated code as a String
   */
  private String randomCode() {
    // Create a mutable sequence to build the code efficiently
    StringBuilder sb = new StringBuilder();

    // Append random characters from BASE62 until reaching the desired length
    for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
      sb.append(BASE62.charAt(random.nextInt(BASE62.length()))); // Pick a random character
    }

    // Convert StringBuilder to String and return
    return sb.toString();
  }
}
