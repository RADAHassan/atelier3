package ma.fstt.ecommerce.service;

import ma.fstt.ecommerce.model.Categorie;
import ma.fstt.ecommerce.model.Produit;
import ma.fstt.ecommerce.model.Vitrine;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProduitService {

    /** Insere des donnees de demonstration si la base est vide. */
    public void initialiserDonnees() {
        EntityManager em = JPAUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            long n = em.createQuery("SELECT COUNT(v) FROM Vitrine v", Long.class).getSingleResult();
            if (n > 0) return;
            tx.begin();
            Categorie info = new Categorie("Informatique");
            Categorie tel = new Categorie("Telephonie");
            Categorie acc = new Categorie("Accessoires");
            em.persist(info);
            em.persist(tel);
            em.persist(acc);

            Vitrine v = new Vitrine("Nos meilleures offres");
            v.ajouterProduit(new Produit("Laptop Pro 15", "Ordinateur portable 16 Go RAM, SSD 512 Go", 8999.0, 10, info));
            v.ajouterProduit(new Produit("Smartphone X", "Ecran OLED 6.5 pouces, 128 Go", 4500.0, 25, tel));
            v.ajouterProduit(new Produit("Casque Bluetooth", "Reduction de bruit active", 799.0, 40, acc));
            v.ajouterProduit(new Produit("Souris sans fil", "Ergonomique, 2400 DPI", 149.0, 100, acc));
            v.ajouterProduit(new Produit("Clavier mecanique", "Switchs rouges, retroeclaire", 459.0, 30, info));
            em.persist(v);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Produit> lister() {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery("SELECT p FROM Produit p ORDER BY p.nom", Produit.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vitrine> vitrines() {
        EntityManager em = JPAUtil.em();
        try {
            List<Vitrine> vs = em.createQuery("SELECT v FROM Vitrine v", Vitrine.class).getResultList();
            for (Vitrine v : vs) v.getProduits().size(); // charge la collection LAZY
            return vs;
        } finally {
            em.close();
        }
    }
}
