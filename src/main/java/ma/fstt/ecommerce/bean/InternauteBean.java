package ma.fstt.ecommerce.bean;

import ma.fstt.ecommerce.model.Internaute;
import ma.fstt.ecommerce.service.InternauteService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/** Gestion des internautes : inscription, connexion, deconnexion. */
@ManagedBean(name = "internauteBean")
@SessionScoped
public class InternauteBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private final InternauteService service = new InternauteService();

    private String nom;
    private String email;
    private String motDePasse;
    private Internaute connecte;

    public String inscrire() {
        try {
            connecte = service.inscrire(nom, email, motDePasse);
            motDePasse = null;
            return "vitrine?faces-redirect=true";
        } catch (RuntimeException e) {
            message(e.getMessage() != null ? e.getMessage() : "Inscription impossible.");
            return null;
        }
    }

    public String connecter() {
        Internaute i = service.authentifier(email, motDePasse);
        if (i == null) {
            message("Email ou mot de passe incorrect.");
            return null;
        }
        connecte = i;
        motDePasse = null;
        return "vitrine?faces-redirect=true";
    }

    public String deconnecter() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "vitrine?faces-redirect=true";
    }

    public boolean isConnecte() { return connecte != null; }

    private void message(String m) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, m, null));
    }

    public Internaute getConnecte() { return connecte; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
