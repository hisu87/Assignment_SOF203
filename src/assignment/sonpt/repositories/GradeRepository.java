/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.sonpt.repositories;

import assignment.sonpt.models.Grade;
import assignment.sonpt.models.GradeResponse;
import assignment.sonpt.utils.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sonpt_ph19600
 */
public class GradeRepository {

    public boolean add(Grade newGrade) {
        Connection conn = JDBCUtil.getConnection();
        String insertQuery = "INSERT INTO grade VALUES (?, ?, ?, ?)";

        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(insertQuery);
            ps.setInt(1, newGrade.getStudentId());
            ps.setInt(2, newGrade.getEnglish());
            ps.setInt(3, newGrade.getInformatics());
            ps.setInt(4, newGrade.getGymnastics());

            ps.executeUpdate();
            return true; //Thêm thành công
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Grade updateGrade) {
        Connection conn = JDBCUtil.getConnection();
        String updateQuery = "UPDATE grade SET student_id = ?, english = ?, "
                + "informatics = ?, gymnastics = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setInt(1, updateGrade.getStudentId());
            ps.setInt(2, updateGrade.getEnglish());
            ps.setInt(3, updateGrade.getInformatics());
            ps.setInt(4, updateGrade.getGymnastics());
            ps.setInt(5, updateGrade.getId());
            System.out.println(ps.executeUpdate());
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        Connection conn = JDBCUtil.getConnection();
        String deleteQuery = "DELETE FROM grade WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(deleteQuery);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public List<GradeResponse> getList() {
        List<GradeResponse> lstGrade = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        String selectQuery = "SELECT G.id, S.student_code, S.fullname, G.english, "
                + "G.informatics, G.gymnastics, "
                + "ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) "
                + "AS average FROM grade AS G JOIN students AS S ON G.student_id = S.id";
        try {
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String studentid = rs.getString(2);
                String fullname = rs.getString(3);
                Integer english = rs.getInt(4);
                Integer informatics = rs.getInt(5);
                Integer gymnastics = rs.getInt(6);
                Float average = rs.getFloat(7);
                GradeResponse gradeResponse = new GradeResponse(id, studentid, fullname, english, informatics, gymnastics, average);
                lstGrade.add(gradeResponse);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return lstGrade;
    }

    public Grade findById(Integer idRequest) {
        String selectQuery = "SELECT * FROM grade WHERE id = ?";
        Connection conn = JDBCUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setInt(1, idRequest);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt(1);
                Integer studenstId = rs.getInt(2);
                Integer english = rs.getInt(3);
                Integer informatics = rs.getInt(4);
                Integer gymnastics = rs.getInt(5);
                Grade grade = new Grade(id, studenstId, english, informatics, gymnastics);
                return grade;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public GradeResponse findByStudentId(Integer studentIdRequest) {
        GradeResponse gradeResponse = null;
        String selectQuery = "SELECT G.id, S.student_code, S.fullname, G.english, "
                + "G.informatics, G.gymnastics, "
                + "ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) "
                + "AS average FROM grade AS G JOIN students AS S ON G.student_id = S.id"
                + " WHERE G.student_id = ?";
        Connection conn = JDBCUtil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setInt(1, studentIdRequest);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt(1);
                String studentid = rs.getString(2);
                String fullname = rs.getString(3);
                Integer english = rs.getInt(4);
                Integer informatics = rs.getInt(5);
                Integer gymnastics = rs.getInt(6);
                Float average = rs.getFloat(7);
                gradeResponse = new GradeResponse(id, studentid, fullname, english, informatics, gymnastics, average);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return gradeResponse;
    }

    public List<GradeResponse> getTop() {
        List<GradeResponse> topGrade = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        String topQuery = "SELECT TOP(3) G.id, S.student_code, S.fullname, "
                + "G.english, G.informatics, G.gymnastics, "
                + "ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) "
                + "AS average FROM Grade AS G JOIN students AS S "
                + "ON G.student_id = S.id ORDER BY average DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(topQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String studentid = rs.getString(2);
                String fullname = rs.getString(3);
                Integer english = rs.getInt(4);
                Integer informatics = rs.getInt(5);
                Integer gymnastics = rs.getInt(6);
                Float average = rs.getFloat(7);
                GradeResponse gradeResponse = new GradeResponse(id, studentid, fullname, english, informatics, gymnastics, average);
                topGrade.add(gradeResponse);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return topGrade;
    }
}
