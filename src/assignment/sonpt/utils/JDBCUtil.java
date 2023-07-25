package assignment.sonpt.utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hisu
 * DBContext
 */
public class JDBCUtil {

    private static Connection conn;

    public static Connection getConnection() {
    if (conn == null) {
        // existing code...
         String server = "HISU";
            String user = "sa";
            String password = "0807";
            String db = "fpl_daotao";
            
            SQLServerDataSource dataSource = new SQLServerDataSource();
            
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setDatabaseName(db);
            dataSource.setServerName(server);
            dataSource.setEncrypt(false); // Disable SSL

        try {
            conn = dataSource.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to create connection");
            }
        } catch (SQLServerException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return JDBCUtil.conn;
}
    
}
