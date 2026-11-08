package ma.fstt.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Vitrine : presentation d'un ensemble de produits mis en avant. */
@Entity
@Table(name = "vitrine")
public class Vitrine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @OneToMany(mappedBy = "vitrine", cascade = CascadeType.ALL)
    private List<Produit> produits = new ArrayList<>();

    public Vitrine() {}
    public Vitrine(String titre) { this.titre = titre; }

    public void ajouterProduit(Produit p) {
        produits.add(p);
        p.setVitrine(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public List<Produit> getProduits() { return produits; }
    public void setProduits(List<Produit> produits) { this.produits = produits; }
}
