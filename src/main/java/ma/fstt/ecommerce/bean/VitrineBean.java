package ma.fstt.ecommerce.bean;

import ma.fstt.ecommerce.model.Vitrine;
import ma.fstt.ecommerce.service.ProduitService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/** Gestion de la vitrine : affichage des produits. */
@ManagedBean(name = "vitrineBean")
@ViewScoped
public class VitrineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ProduitService service = new ProduitService();
    private List<Vitrine> vitrines;

    @PostConstruct
    public void init() {
        service.initialiserDonnees();
        vitrines = service.vitrines();
    }

    public List<Vitrine> getVitrines() { return vitrines; }
}
