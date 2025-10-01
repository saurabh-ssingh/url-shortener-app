package io.shortlink.tinyurler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShortenUrlRequest {
    @NotBlank(message = "URL cannot be empty")
    @Pattern(
            regexp = "^(https?://).*",
            message = "URL must start with http:// or https://"
    )
    private String originalUrl;

    private LocalDateTime expiresAt; // optional
}
