
package assignment.sonpt.repositories;

import java.util.List;
import assignment.sonpt.models.User;
import assignment.sonpt.utils.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sonpt_ph19600
 */
public class LoginRepository{
    public User findByUsernameAndPassword(String usernameRequest, String passwordRequest) {
        Connection conn = JDBCUtil.getConnection();
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users "
                    + "WHERE username = ? AND [password] = ?");
            ps.setString(1, usernameRequest);
            ps.setString(2, passwordRequest);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String username = rs.getString(1);
                String password = rs.getString(2);
                Integer role = rs.getInt(3);
                user = new User(username, password, role);
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
        return user;
    }
}
