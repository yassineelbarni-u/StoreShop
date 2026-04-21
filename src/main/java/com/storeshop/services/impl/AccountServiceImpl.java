package com.storeshop.services.impl;

import com.storeshop.entities.Role;
import com.storeshop.entities.User;
import com.storeshop.repositories.UserRepository;
import com.storeshop.services.AccountService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de gestion des comptes utilisateurs.
 * Gère l'inscription, la persistance et le chargement des utilisateurs.
 * Utilise {@link PasswordEncoder} pour sécuriser les mots de passe.
 */
@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final UserRepository UserRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Crée et enregistre un nouvel utilisateur de type CLIENT.
   * 
   * @param username        Nom d'utilisateur unique.
   * @param password        Mot de passe en clair.
   * @param email           Adresse email.
   * @param ConfirmPassword Confirmation du mot de passe.
   * @return L'utilisateur créé et sauvegardé.
   * @throws RuntimeException Si l'utilisateur existe déjà ou si les mots de passe ne correspondent pas.
   */
  @Override
  public User AddUser(String username, String password, String email, String ConfirmPassword) {
    User existingUser = UserRepository.findByUsername(username);
    if (existingUser != null) throw new RuntimeException("User already exists");

    if (!password.equals(ConfirmPassword)) throw new RuntimeException("Passwords  not match");

    User newUser =
        User.builder()
            .userId(UUID.randomUUID().toString())
            .username(username)
            .password(passwordEncoder.encode(password))
            .email(email)
            .role(Role.CLIENT)
            .build();

    return UserRepository.save(newUser);
  }

  /**
   * Assure l'existence d'un administrateur par défaut dans le système.
   * 
   * @param username Nom d'utilisateur admin.
   * @param password Mot de passe admin.
   * @param email    Email admin.
   * @return L'utilisateur trouvé ou créé.
   */
  @Override
  public User ensureUserExists(String username, String password, String email) {
    User existingUser = UserRepository.findByUsername(username);
    if (existingUser == null) {
      existingUser =
          User.builder()
              .userId(UUID.randomUUID().toString())
              .username(username)
              .password(passwordEncoder.encode(password))
              .email(email)
              .role(Role.ADMIN)
              .build();
      existingUser = UserRepository.save(existingUser);
    }
    return existingUser;
  }

  /**
   * Charge un utilisateur par son nom d'utilisateur.
   * 
   * @param username Nom à rechercher.
   * @return L'utilisateur correspondant ou null.
   */
  @Override
  public User loadUserByUsername(String username) {
    return UserRepository.findByUsername(username);
  }
}
