package com.storeshop.services.impl;

import com.storeshop.entities.User;
import com.storeshop.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implémentation de {@link UserDetailsService} pour Spring Security.
 * Adapte les utilisateurs du domaine {@link User} pour le mécanisme d'authentification de Spring.
 * Transforme les rôles de l'application en autorités de sécurité.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AccountService accountService;

  /**
   * Charge un utilisateur par son nom d'utilisateur et construit un objet {@link UserDetails}.
   * 
   * @param username Le nom de l'utilisateur à charger.
   * @return Un objet UserDetails prêt pour l'authentification.
   * @throws UsernameNotFoundException Si l'utilisateur n'existe pas.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User appUser = accountService.loadUserByUsername(username);
    if (appUser == null) throw new UsernameNotFoundException("User not found");

    String role = appUser.getRole() != null ? appUser.getRole().name() : "USER";

    return org.springframework.security.core.userdetails.User.withUsername(appUser.getUsername())
        .password(appUser.getPassword())
        .roles(role)
        .build();
  }
}
