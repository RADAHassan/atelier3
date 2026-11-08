package ma.fstt.ecommerce.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Libere l'EntityManagerFactory a l'arret de l'application. */
@WebListener
public class JPAContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) { JPAUtil.close(); }
}
