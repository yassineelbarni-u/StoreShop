package com.storeshop;

import com.storeshop.services.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreShopApplication {
  public static void main(String[] args) {
    SpringApplication.run(StoreShopApplication.class, args);
  }

  @Bean
  CommandLineRunner initAdmin(
      AccountService accountService,
      @Value("${app.admin.username:admin}") String adminUsername,
      @Value("${app.admin.password:admin123}") String adminPassword,
      @Value("${app.admin.email:admin@storeshop.local}") String adminEmail) {
    return args -> accountService.ensureUserExists(adminUsername, adminPassword, adminEmail);
  }
}
