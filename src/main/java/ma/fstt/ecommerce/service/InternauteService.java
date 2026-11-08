package ma.fstt.ecommerce.service;

import ma.fstt.ecommerce.model.Internaute;
import ma.fstt.ecommerce.model.Panier;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class InternauteService {

    public Internaute inscrire(String nom, String email, String motDePasse) {
        EntityManager em = JPAUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Long n = em.createQuery("SELECT COUNT(i) FROM Internaute i WHERE i.email = :e", Long.class)
                    .setParameter("e", email).getSingleResult();
            if (n > 0) {
                throw new IllegalStateException("Cet email est deja utilise.");
            }
            Internaute i = new Internaute(nom, email, hash(motDePasse));
            Panier p = new Panier();
            p.setInternaute(i);
            i.setPanier(p);
            em.persist(i); // le panier est persiste en cascade
            tx.commit();
            return i;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Internaute authentifier(String email, String motDePasse) {
        EntityManager em = JPAUtil.em();
        try {
            List<Internaute> r = em.createQuery(
                    "SELECT i FROM Internaute i WHERE i.email = :e AND i.motDePasse = :m", Internaute.class)
                    .setParameter("e", email).setParameter("m", hash(motDePasse)).getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    static String hash(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
