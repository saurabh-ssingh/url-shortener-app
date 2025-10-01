package io.shortlink.tinyurler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity // Marks this class as a JPA entity (mapped to a database table)
@Table(name = "url_mapping") // Maps this entity to a table (defaults to class name if no attributes given)
@Getter // Lombok: auto-generates getter methods for all fields
@Setter // Lombok: auto-generates setter methods for all fields
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates primary key using DB identity strategy
    private Long id;

    @Column(nullable = false, unique = true, length = 10) // Column: required, unique, max length 10 (short code)
    private String shortCode;

    @Column(nullable = false, columnDefinition = "TEXT") // Column: required, stored as TEXT (original URL)
    private String originalUrl;

    private LocalDateTime createdAt = LocalDateTime.now(); // Stores creation timestamp (default: current time)

    private LocalDateTime expiresAt; // Expiration timestamp (nullable)


}

