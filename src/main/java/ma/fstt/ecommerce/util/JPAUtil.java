package ma.fstt.ecommerce.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/** Fournit l'EntityManagerFactory unique de l'application (unite "ecommercePU"). */
public final class JPAUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("ecommercePU");

    private JPAUtil() {}

    public static EntityManager em() { return EMF.createEntityManager(); }

    public static void close() { if (EMF.isOpen()) EMF.close(); }
}
