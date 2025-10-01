package io.shortlink.tinyurler.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortenUrlResponse {
  private String shortUrl;
  private String originalUrl;
  private String message;
  private LocalDateTime expiredAt;
}
