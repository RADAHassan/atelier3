package ma.fstt.ecommerce.bean;

import ma.fstt.ecommerce.model.LignePanier;
import ma.fstt.ecommerce.service.PanierService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/** Gestion du panier de l'internaute connecte. */
@ManagedBean(name = "panierBean")
@SessionScoped
public class PanierBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PanierService service = new PanierService();

    @ManagedProperty("#{internauteBean}")
    private InternauteBean internauteBean;

    public String ajouter(Long produitId) {
        if (!internauteBean.isConnecte()) return "connexion?faces-redirect=true";
        service.ajouter(internauteBean.getConnecte().getId(), produitId);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Produit ajoute au panier.", null));
        return null;
    }

    public String retirer(Long ligneId) {
        service.retirer(internauteBean.getConnecte().getId(), ligneId);
        return null;
    }

    public String vider() {
        service.vider(internauteBean.getConnecte().getId());
        return null;
    }

    public List<LignePanier> getLignes() {
        if (!internauteBean.isConnecte()) return Collections.emptyList();
        return service.charger(internauteBean.getConnecte().getId()).getLignes();
    }

    public double getTotal() {
        double t = 0;
        for (LignePanier l : getLignes()) t += l.getSousTotal();
        return t;
    }

    public int getNombreArticles() {
        int n = 0;
        for (LignePanier l : getLignes()) n += l.getQuantite();
        return n;
    }

    public void setInternauteBean(InternauteBean b) { this.internauteBean = b; }
}
