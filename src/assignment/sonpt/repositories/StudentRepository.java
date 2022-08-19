package assignment.sonpt.repositories;

import assignment.sonpt.models.Student;
import assignment.sonpt.utils.JDBCUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sonpt_ph19600
 */
public class StudentRepository {

    public Student findByStudentCode(String studentCodeRequest) {
        Connection conn = JDBCUtil.getConnection();
        Student student = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students WHERE student_code = ?");
            ps.setString(1, studentCodeRequest);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt(1);
                String studentCode = rs.getString(2);
                String fullname = rs.getString(3);
                String email = rs.getString(4);
                String phone = rs.getString(5);
                Integer gender = rs.getInt(6);
                String address = rs.getString(7);
                String avatar = rs.getString(8);

                student = new Student(id, studentCode, fullname, email, phone, gender, address, avatar);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return student;
    }

    public boolean add(Student student) {
        String insertQuery = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = JDBCUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, student.getStudentCode());
            ps.setString(2, student.getFullName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setInt(5, student.getGender());
            ps.setString(6, student.getAddress());
            ps.setString(7, student.getAvatar());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Student updateStudent) {
        String updateQuery = "UPDATE Students SET "
                + "student_code = ?, "
                + "fullname = ?, "
                + "email = ?, "
                + "phone = ?, "
                + "gender = ?, "
                + "[address] = ?, "
                + "avatar = ? WHERE id = ?";

        Connection conn = JDBCUtil.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, updateStudent.getStudentCode());
            ps.setString(2, updateStudent.getFullName());
            ps.setString(3, updateStudent.getEmail());
            ps.setString(4, updateStudent.getPhone());
            ps.setInt(5, updateStudent.getGender());
            ps.setString(6, updateStudent.getAddress());
            ps.setString(7, updateStudent.getAvatar());
            ps.setInt(8, updateStudent.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Integer id) {
        String deleteQuery = "DELETE FROM students WHERE id = ?";
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(deleteQuery);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Student findById(Integer idRequest) {
        Connection conn = JDBCUtil.getConnection();
        Student student = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
            ps.setInt(1, idRequest);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt(1);
                String studentCode = rs.getString(2);
                String fullname = rs.getString(3);
                String email = rs.getString(4);
                String phone = rs.getString(5);
                Integer gender = rs.getInt(6);
                String address = rs.getString(7);
                String avatar = rs.getString(8);

                student = new Student(id, studentCode, fullname, email, phone, gender, address, avatar);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return student;
    }

    public List<Student> getList() {
        List<Student> lstStudent = new ArrayList<>();

        Connection conn = JDBCUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String studentCode = rs.getString(2);
                String fullname = rs.getString(3);
                String email = rs.getString(4);
                String phone = rs.getString(5);
                Integer gender = rs.getInt(6);
                String address = rs.getString(7);
                String avatar = rs.getString(8);

                Student student = new Student(id, studentCode, fullname, email, phone, gender, address, avatar);
                lstStudent.add(student);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return lstStudent;
    }
}
