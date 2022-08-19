package assignment.sonpt.utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sonpt_ph19600
 * DBContext
 */
public class JDBCUtil {

    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            String server = "PTS\\SQLEXPRESS";
            String user = "sa";
            String password = "123456";
            String db = "FPL_DaoTao";
            int port = 1433;
            SQLServerDataSource dataSource = new SQLServerDataSource();
            
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setDatabaseName(db);
            dataSource.setServerName(server);
            dataSource.setPortNumber(port);
            dataSource.setEncrypt(false);
            try {
                conn = dataSource.getConnection();
//                conn.setAutoCommit(false);
//                conn.commit();
            } catch (SQLServerException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return JDBCUtil.conn;
    }
    
}
