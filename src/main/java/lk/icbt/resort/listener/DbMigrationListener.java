package lk.icbt.resort.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lk.icbt.resort.util.CrudUtil;

@WebListener
public class DbMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("App Initialized...!!!");
        } catch (Exception e) {
            e.printStackTrace();
            // If DB user doesn't have permission, the app can still run without payment status persistence.
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}
