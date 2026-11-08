package ma.fstt.ecommerce.service;

import ma.fstt.ecommerce.model.*;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PanierService {

    public Panier charger(Long internauteId) {
        EntityManager em = JPAUtil.em();
        try {
            Panier p = trouverOuCreer(em, internauteId);
            p.getLignes().size(); // charge les lignes
            return p;
        } finally {
            em.close();
        }
    }

    public void ajouter(Long internauteId, Long produitId) {
        EntityManager em = JPAUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Panier panier = trouverOuCreer(em, internauteId);
            Produit produit = em.find(Produit.class, produitId);
            LignePanier existante = null;
            for (LignePanier l : panier.getLignes()) {
                if (l.getProduit().getId().equals(produitId)) { existante = l; break; }
            }
            if (existante != null) {
                existante.setQuantite(existante.getQuantite() + 1);
            } else {
                panier.getLignes().add(new LignePanier(panier, produit, 1));
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void retirer(Long internauteId, Long ligneId) {
        EntityManager em = JPAUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Panier panier = trouverOuCreer(em, internauteId);
            panier.getLignes().removeIf(l -> l.getId().equals(ligneId)); // orphanRemoval
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void vider(Long internauteId) {
        EntityManager em = JPAUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            trouverOuCreer(em, internauteId).getLignes().clear();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /** Doit etre appelee dans une transaction active si le panier doit etre cree. */
    private Panier trouverOuCreer(EntityManager em, Long internauteId) {
        List<Panier> r = em.createQuery(
                "SELECT p FROM Panier p WHERE p.internaute.id = :id", Panier.class)
                .setParameter("id", internauteId).getResultList();
        if (!r.isEmpty()) return r.get(0);
        boolean local = !em.getTransaction().isActive();
        if (local) em.getTransaction().begin();
        Panier p = new Panier();
        p.setInternaute(em.find(Internaute.class, internauteId));
        em.persist(p);
        if (local) em.getTransaction().commit();
        return p;
    }
}
