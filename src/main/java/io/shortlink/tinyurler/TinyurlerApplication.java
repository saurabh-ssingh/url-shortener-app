package io.shortlink.tinyurler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TinyurlerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyurlerApplication.class, args);
  }
}
