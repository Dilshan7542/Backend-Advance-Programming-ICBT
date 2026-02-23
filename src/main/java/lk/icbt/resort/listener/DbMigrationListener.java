package lk.icbt.resort.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lk.icbt.resort.util.CrudUtil;

/**
 * Small DB migration to keep the assignment project easy to run.
 * Creates bills table if it does not exist (so payment status works even if you used the old SQL).
 */
@WebListener
public class DbMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Backward compatible: add room category (AC/NON_AC) if missing
            try {
                CrudUtil.executeUpdate("ALTER TABLE rooms ADD COLUMN ac_type VARCHAR(10) NOT NULL DEFAULT 'NON_AC'");
            } catch (Exception e) {
                // If column already exists, ignore. (MySQL will throw an error)
                e.printStackTrace();
            }

            String sql = """
                    CREATE TABLE IF NOT EXISTS bills (
                      bill_id INT AUTO_INCREMENT PRIMARY KEY,
                      reservation_id INT NOT NULL UNIQUE,
                      sub_total DECIMAL(10,2) NOT NULL,
                      service_charge DECIMAL(10,2) NOT NULL,
                      tax DECIMAL(10,2) NOT NULL,
                      total DECIMAL(10,2) NOT NULL,
                      status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
                      paid_at TIMESTAMP NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
                    )
                    """;
            CrudUtil.executeUpdate(sql);

            String auditSql = """
                    CREATE TABLE IF NOT EXISTS reservation_actions (
                      action_id INT AUTO_INCREMENT PRIMARY KEY,
                      reservation_id INT NOT NULL,
                      user_id INT NOT NULL,
                      username VARCHAR(50) NOT NULL,
                      role VARCHAR(20) NOT NULL,
                      action VARCHAR(40) NOT NULL,
                      reason VARCHAR(500),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      INDEX idx_ra_res (reservation_id),
                      CONSTRAINT fk_ra_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
                      CONSTRAINT fk_ra_user FOREIGN KEY (user_id) REFERENCES users(user_id)
                    )
                    """;
            CrudUtil.executeUpdate(auditSql);
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
