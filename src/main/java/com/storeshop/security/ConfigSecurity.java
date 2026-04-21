package com.storeshop.security;

import com.storeshop.services.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Configuration de la sécurité de l'application.
 * Définit les règles d'accès, la gestion du formulaire de login, du logout et la redirection après authentification.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class ConfigSecurity {

  private final UserDetailsServiceImpl userDetailsServiceImpl;

  /**
   * Définit le succès de l'authentification.
   * Redirige vers le dashboard si l'utilisateur est un administrateur, 
   * sinon vers la page d'accueil publique.
   * 
   * @return L'instance de AuthenticationSuccessHandler.
   */
  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return (request, response, authentication) -> {
      boolean isAdmin =
          authentication.getAuthorities().stream()
              .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

      response.sendRedirect(isAdmin ? "/admin/dashboard" : "/");
    };
  }

  /**
   * Configuration de la chaîne de filtres de sécurité.
   * - Formulaire de login personnalisé.
   * - Gestion de la session et des cookies au logout.
   * - Définition des accès aux ressources statiques et aux URLs sensibles.
   * 
   * @param httpSecurity                 Entrée de configuration de sécurité.
   * @param authenticationSuccessHandler Handler de succès de login.
   * @return L'instance SecurityFilterChain configurée.
   * @throws Exception En cas d'erreur de configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity, AuthenticationSuccessHandler authenticationSuccessHandler)
      throws Exception {

    return httpSecurity
        .formLogin(
            form ->
                form.loginPage("/login").successHandler(authenticationSuccessHandler).permitAll())
        .userDetailsService(userDetailsServiceImpl)
        .logout(
            logout ->
                logout
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/home",
                        "/produits",
                        "/produits/**",
                        "/register",
                        "/login",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/uploads/**",
                        "/webjars/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .build();
  }
}
