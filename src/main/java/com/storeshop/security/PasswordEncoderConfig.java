package com.storeshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration de l'encodeur de mots de passe.
 * Utilise BCrypt pour le hachage sécurisé des mots de passe utilisateurs.
 */
@Configuration
public class PasswordEncoderConfig {

  /**
   * Définit le bean de l'encodeur de mot de passe.
   * 
   * @return Une instance de BCryptPasswordEncoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
