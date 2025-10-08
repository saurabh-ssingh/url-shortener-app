package io.shortlink.tinyurler.controller;

import io.shortlink.tinyurler.dto.ShortenUrlRequest;
import io.shortlink.tinyurler.dto.ShortenUrlResponse;
import io.shortlink.tinyurler.service.UrlService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller that handles URL shortening and redirection.
 *
 * <p>
 *     - POST /url/shorten → Creates a short URL for the given original URL.
 *     - GET /url/{shortCode} → Redirects to the original URL.
 */
@RestController
@RequestMapping("/url")
@Log4j2
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  /**
   * Shortens the given original URL and returns a response containing the short URL.
   *
   * @param request DTO containing the original URL and optional expiry date.
   * @return ResponseEntity with the shortened URL details.
   */
  @PostMapping("/shorten")
  public ResponseEntity<ShortenUrlResponse> shortenUrl(
      @Valid @RequestBody ShortenUrlRequest request) {
    log.info("Received request to shorten URL :{}", request.getOriginalUrl());
    final ShortenUrlResponse response = urlService.shortenUrl(request);
    log.info(
        "Generated short URL : {} for original URL : {} ",
        response.getShortUrl(),
        response.getOriginalUrl());
    return ResponseEntity.ok(response);
  }

  /**
   * Redirects to the original URL mapped from the provided short code.
   *
   * @param shortCode Short code identifier for the original URL.
   * @return RedirectView that forwards the client to the original URL.
   */
  @GetMapping("/{shortCode}")
  public RedirectView redirect(@PathVariable String shortCode) {
    log.info("Redirect request received for short code: {}", shortCode);
    final String originalUrl = this.urlService.getOriginalUrl(shortCode);
    log.info("Redirecting short code: {} to original URL: {}", shortCode, originalUrl);
    return new RedirectView(originalUrl);
  }


  @GetMapping("/test")
    public String testCode(){
      return "test Code";
  }
}
