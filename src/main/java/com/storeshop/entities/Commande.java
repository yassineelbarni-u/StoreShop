package com.storeshop.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une commande effectuée par un utilisateur.
 * Regroupe plusieurs articles commandés et suit l'état global de la transaction.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commande {

  /**
   * Identifiant unique de la commande.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * L'utilisateur ayant passé la commande.
   */
  @ManyToOne(optional = false)
  private User user;

  /**
   * Date et heure de création de la commande.
   */
  private LocalDateTime createdAt;

  /**
   * Statut actuel de la commande (ex: VALIDEE, ANNULEE, EN_COURS).
   */
  private String status;

  /**
   * Montant total de la commande toutes taxes comprises.
   */
  private double total;

  /**
   * Liste des articles inclus dans cette commande.
   */
  @OneToMany(
      mappedBy = "commande",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<CommandeItem> items = new ArrayList<>();

  /**
   * Ajoute un article à la commande et établit la relation bidirectionnelle.
   * 
   * @param item L'article de commande à ajouter.
   */
  public void addItem(CommandeItem item) {
    items.add(item);
    item.setCommande(this);
  }
}
