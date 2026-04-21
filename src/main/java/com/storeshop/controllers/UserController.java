package com.storeshop.controllers;

import com.storeshop.entities.Role;
import com.storeshop.entities.User;
import com.storeshop.repositories.UserRepository;
import com.storeshop.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour la gestion des utilisateurs par l'administrateur.
 * Permet de lister, ajouter, modifier et supprimer des utilisateurs.
 * Accès restreint aux utilisateurs ayant le rôle 'ADMIN'.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class UserController {

  private final AccountService accountService;
  private final UserRepository UserRepository;

  /**
   * Affiche le tableau de bord de l'administration.
   * 
   * @return La vue dashboard.
   */
  @GetMapping("/dashboard")
  public String dashboard() {
    return "admin/dashboard";
  }

  /**
   * Affiche la liste de tous les utilisateurs inscrits.
   * 
   * @param model Le modèle pour la vue.
   * @return La vue de la liste des utilisateurs.
   */
  @GetMapping("/users")
  public String listUsers(Model model) {
    model.addAttribute("users", UserRepository.findAll());
    return "admin/listeUsers";
  }

  /**
   * Affiche le formulaire d'ajout d'un nouvel utilisateur.
   * 
   * @param model Le modèle pour la vue.
   * @return La vue du formulaire d'ajout.
   */
  @GetMapping("/users/add")
  public String showAddUserForm(Model model) {
    model.addAttribute("roles", Role.values());
    return "admin/ajouterUser";
  }

  /**
   * Traite l'ajout d'un utilisateur.
   * 
   * @param username        Nom d'utilisateur.
   * @param password        Mot de passe.
   * @param confirmPassword Confirmation.
   * @param email           Email.
   * @param role            Rôle attribué.
   * @return Redirection vers la liste des utilisateurs ou le formulaire en cas d'erreur.
   */
  @PostMapping("/users/add")
  public String addUser(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      @RequestParam String email,
      @RequestParam(required = false) String role) {
    try {
      User user = accountService.AddUser(username, password, email, confirmPassword);

      if (role != null && !role.isEmpty()) {
        user.setRole(Role.valueOf(role));
        UserRepository.save(user);
      }

      return "redirect:/admin/users?success=add";
    } catch (Exception e) {
      return "redirect:/admin/users/add?error=" + e.getMessage();
    }
  }

  /**
   * Affiche le formulaire d'édition d'un utilisateur existant.
   * 
   * @param userId ID de l'utilisateur à modifier.
   * @param model  Le modèle pour la vue.
   * @return La vue du formulaire d'édition.
   */
  @GetMapping("/users/edit")
  public String showEditUserForm(@RequestParam String userId, Model model) {
    User user =
        UserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    model.addAttribute("user", user);
    model.addAttribute("allRoles", Role.values());
    return "admin/editUser";
  }

  /**
   * Traite la modification des informations d'un utilisateur.
   * 
   * @param userId   ID de l'utilisateur.
   * @param username Nouveau nom d'utilisateur.
   * @param email    Nouvel email.
   * @param password Nouveau mot de passe (optionnel).
   * @param role     Nouveau rôle.
   * @return Redirection vers la liste des utilisateurs.
   */
  @PostMapping("/users/edit")
  public String editUser(
      @RequestParam String userId,
      @RequestParam String username,
      @RequestParam String email,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role) {
    try {
      User user =
          UserRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

      user.setUsername(username);
      user.setEmail(email);

      // Update the password if provided
      if (password != null && !password.isEmpty()) {
        user.setPassword(password); 
      }

      // Update the role
      if (role != null && !role.isEmpty()) {
        user.setRole(Role.valueOf(role));
      }

      UserRepository.save(user);

      return "redirect:/admin/users?success=edit";
    } catch (Exception e) {
      return "redirect:/admin/users?error=" + e.getMessage();
    }
  }

  /**
   * Supprime un utilisateur.
   * 
   * @param userId ID de l'utilisateur à supprimer.
   * @return Redirection vers la liste des utilisateurs.
   */
  @GetMapping("/users/delete")
  public String deleteUser(@RequestParam String userId) {
    try {
      UserRepository.deleteById(userId);
      return "redirect:/admin/users?success=delete";
    } catch (Exception e) {
      return "redirect:/admin/users?error=" + e.getMessage();
    }
  }
}
