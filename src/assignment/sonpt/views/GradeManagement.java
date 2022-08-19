package assignment.sonpt.views;

import assignment.sonpt.models.Grade;
import assignment.sonpt.models.GradeRequest;
import assignment.sonpt.models.GradeResponse;
import assignment.sonpt.models.Student;
import assignment.sonpt.services.GradeService;
import assignment.sonpt.services.StudentService;
import assignment.sonpt.thread.ClockThread;
import assignment.sonpt.thread.TitleMotion;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author sonpt_ph19600
 */
public class GradeManagement extends javax.swing.JFrame {

    private GradeService service;
    private ClockThread clock;
    private TitleMotion titleMotion;
    private StudentService studentService;

    public GradeManagement() {
        initComponents();
        setLocationRelativeTo(null);
        service = new GradeService();
        studentService = new StudentService();
        ClockThread.jLabel = lbClock;
        clock = new ClockThread();
        clock.start();
        TitleMotion.jLabel = lbTitle;
        titleMotion = new TitleMotion();
        titleMotion.start();
        loadTable(service.getList());

    }

    private void loadTable(List<GradeResponse> lstGrade) {
        DefaultTableModel model = (DefaultTableModel) tbGrade.getModel();
        model.setRowCount(0);
        for (GradeResponse x : lstGrade) {

            Object[] row = new Object[]{
                x.getId(),
                x.getStudentCode(),
                x.getFullname(),
                x.getEnglish(),
                x.getInformatics(),
                x.getGymnastics(),
                x.getAverage()
            };
            model.addRow(row);
        }
    }

    private boolean checkInputPoint(String gradeStr) {
        try {
            int grade = Integer.parseInt(gradeStr);
            if (grade < 0 || grade > 10) {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private GradeRequest getInput() {
        String inputStudentId = txtStudentId.getText().trim();
        String inputEnglish = txtEnglish.getText().trim();
        String inputInformatics = txtInformatics.getText().trim();
        String inputGymnastics = txtGymnastics.getText().trim();

        if (inputStudentId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (inputEnglish.isBlank()) {
            JOptionPane.showMessageDialog(this, "Điểm Tiếng anh không được phép để trống", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        if (!checkInputPoint(inputEnglish)) {
            JOptionPane.showMessageDialog(this, "Điểm Tiếng anh là số nguyên từ 0 đến 10", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        if (inputInformatics.isBlank()) {
            JOptionPane.showMessageDialog(this, "Điểm Tin học không được phép để trống", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        if (!checkInputPoint(inputInformatics)) {
            JOptionPane.showMessageDialog(this, "Điểm Tin học là số nguyên từ 0 đến 10", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        if (inputGymnastics.isBlank()) {
            JOptionPane.showMessageDialog(this, "Điểm GDTC không được phép để trống", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        if (!checkInputPoint(inputGymnastics)) {
            JOptionPane.showMessageDialog(this, "Điểm GDTC là số nguyên từ 0 đến 10", "Warning", JOptionPane.WARNING_MESSAGE);
            txtEnglish.requestFocus();
            return null;
        }
        GradeRequest gradeRequest = new GradeRequest(0, inputStudentId, Integer.parseInt(inputEnglish),
                Integer.parseInt(inputInformatics), Integer.parseInt(inputGymnastics));
        return gradeRequest;
    }

    private void clear() {
        lbId.setText("-");
        txtFullname.setText("");
        txtStudentId.setText("");
        txtEnglish.setText("");
        txtInformatics.setText("");
        txtGymnastics.setText("");
        lbAvg.setText("0.0");
        txtSearch.setText("");
        tbGrade.clearSelection();
    }

    private void showSelectedRow(int selectedIndex) {
        lbId.setText(tbGrade.getValueAt(selectedIndex, 0).toString());
        txtStudentId.setText(tbGrade.getValueAt(selectedIndex, 1).toString());
        txtFullname.setText(tbGrade.getValueAt(selectedIndex, 2).toString());
        txtEnglish.setText(tbGrade.getValueAt(selectedIndex, 3).toString());
        txtInformatics.setText(tbGrade.getValueAt(selectedIndex, 4).toString());
        txtGymnastics.setText(tbGrade.getValueAt(selectedIndex, 5).toString());
        lbAvg.setText(tbGrade.getValueAt(selectedIndex, 6).toString());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GradeManagementPanel = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFullname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtEnglish = new javax.swing.JTextField();
        txtStudentId = new javax.swing.JTextField();
        txtInformatics = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGymnastics = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lbAvg = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbId = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnNew = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnSave = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbGrade = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnPrev = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        btnNext = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnLast = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        btnShow = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        btnTop = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        GradeManagementPanel.setBackground(new java.awt.Color(255, 255, 255));
        GradeManagementPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GradeManagementPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitle.setFont(new java.awt.Font("Nunito", 0, 36)); // NOI18N
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Quản Lý Điểm Sinh Viên");
        GradeManagementPanel.add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 450, 43));

        lbClock.setFont(new java.awt.Font("Nunito", 0, 20)); // NOI18N
        lbClock.setForeground(new java.awt.Color(255, 0, 51));
        lbClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbClock.setText("Clock");
        GradeManagementPanel.add(lbClock, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 110, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(1, 181, 204), new java.awt.Color(1, 181, 204), new java.awt.Color(1, 181, 204), new java.awt.Color(1, 181, 204)), "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito", 0, 18))); // NOI18N
        jPanel1.setToolTipText("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSearch.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtSearch.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 280, 35));

        jPanel2.setBackground(new java.awt.Color(1, 181, 204));

        btnSearch.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSearch.setText("Search");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Mã SV:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, -1));

        GradeManagementPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 570, 80));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel1.setText("Họ tên SV:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        txtFullname.setEditable(false);
        txtFullname.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtFullname.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel3.add(txtFullname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 210, -1));

        jLabel2.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel2.setText("Tiếng anh:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        txtEnglish.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtEnglish.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel3.add(txtEnglish, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 210, -1));

        txtStudentId.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtStudentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtStudentId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStudentIdKeyReleased(evt);
            }
        });
        jPanel3.add(txtStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 210, -1));

        txtInformatics.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtInformatics.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel3.add(txtInformatics, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 210, -1));

        jLabel4.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel4.setText("Tin học:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        txtGymnastics.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtGymnastics.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel3.add(txtGymnastics, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 210, -1));

        jLabel5.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel5.setText("Giáo dục TC:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Nunito", 0, 20)); // NOI18N
        jLabel7.setText("Điểm TB");
        jPanel9.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, -1));

        lbAvg.setFont(new java.awt.Font("Nunito", 1, 30)); // NOI18N
        lbAvg.setForeground(new java.awt.Color(1, 181, 204));
        lbAvg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAvg.setText("0.0");
        jPanel9.add(lbAvg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 70, 50));

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 120, 120));

        jLabel6.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Mã SV:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 80, -1));

        jLabel8.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel8.setText("ID:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        lbId.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        lbId.setText("-");
        jPanel3.add(lbId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 50, -1));

        GradeManagementPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 530, 310));

        jPanel4.setBackground(new java.awt.Color(1, 181, 204));

        btnNew.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnNew.setText("New");
        btnNew.setToolTipText("Thêm mới điểm cho sinh viên");
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        GradeManagementPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 210, 100, 35));

        jPanel5.setBackground(new java.awt.Color(1, 181, 204));

        btnSave.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSave.setText("Save");
        btnSave.setToolTipText("Lưu các thay đổi");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        GradeManagementPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 260, -1, -1));

        jPanel6.setBackground(new java.awt.Color(1, 181, 204));

        btnDelete.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Xóa sinh viên khỏi bảng điểm");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        GradeManagementPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, -1, -1));

        jPanel7.setBackground(new java.awt.Color(1, 181, 204));

        btnUpdate.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnUpdate.setText("Update");
        btnUpdate.setToolTipText("Cập nhật điểm cho sinh viên");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 360, -1, -1));

        jPanel8.setBackground(new java.awt.Color(255, 51, 51));

        btnCancel.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCancel.setText("Cancel");
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 770, -1, -1));

        tbGrade.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        tbGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mã SV", "Họ Tên", "Tiếng Anh", "Tin học", "GDTC", "Điểm TB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbGrade.setGridColor(new java.awt.Color(204, 204, 204));
        tbGrade.setRowHeight(30);
        tbGrade.setSelectionBackground(new java.awt.Color(1, 181, 204));
        tbGrade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbGrade.getTableHeader().setReorderingAllowed(false);
        tbGrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbGradeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbGrade);
        if (tbGrade.getColumnModel().getColumnCount() > 0) {
            tbGrade.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbGrade.getColumnModel().getColumn(1).setPreferredWidth(60);
            tbGrade.getColumnModel().getColumn(2).setPreferredWidth(130);
            tbGrade.getColumnModel().getColumn(3).setPreferredWidth(40);
            tbGrade.getColumnModel().getColumn(4).setPreferredWidth(40);
            tbGrade.getColumnModel().getColumn(5).setPreferredWidth(40);
            tbGrade.getColumnModel().getColumn(6).setPreferredWidth(40);
        }

        GradeManagementPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 650, 190));

        jPanel10.setBackground(new java.awt.Color(1, 181, 204));

        btnFirst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/first.png"))); // NOI18N
        btnFirst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFirst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFirstMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 520, 35, 35));

        jPanel11.setBackground(new java.awt.Color(1, 181, 204));

        btnPrev.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/prev.png"))); // NOI18N
        btnPrev.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrevMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 520, -1, -1));

        jPanel12.setBackground(new java.awt.Color(1, 181, 204));

        btnNext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/next.png"))); // NOI18N
        btnNext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, -1, -1));

        jPanel13.setBackground(new java.awt.Color(1, 181, 204));

        btnLast.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/last.png"))); // NOI18N
        btnLast.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLastMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 520, -1, -1));

        jPanel14.setBackground(new java.awt.Color(1, 181, 204));

        btnShow.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnShow.setForeground(new java.awt.Color(255, 255, 255));
        btnShow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnShow.setText("Show");
        btnShow.setToolTipText("Hiển thị toàn bộ bảng điểm");
        btnShow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 410, -1, -1));

        jPanel15.setBackground(new java.awt.Color(1, 181, 204));

        btnTop.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnTop.setForeground(new java.awt.Color(255, 255, 255));
        btnTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnTop.setText("Top");
        btnTop.setToolTipText("Hiển thị Top 3 sinh viên có điểm cao nhất");
        btnTop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTopMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnTop, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnTop, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        GradeManagementPanel.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 460, -1, -1));

        getContentPane().add(GradeManagementPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 820));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        //CANCEL BTN
        if (JOptionPane.showConfirmDialog(this, "Thoát chương trình?", "Exit", JOptionPane.YES_OPTION) == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnCancelMouseClicked

    private void tbGradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbGradeMouseClicked
        // TABLE CLICK
        int index = tbGrade.getSelectedRow();
        showSelectedRow(index);
    }//GEN-LAST:event_tbGradeMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // NEW BTN
        clear();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        //UPDATE BTN
        if (tbGrade.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Mời chọn một sinh viên trong bảng để cập nhật",
                    "Update", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GradeRequest updateGrade = getInput();
        if (updateGrade == null) {
            return;
        }
        if (!(JOptionPane.showConfirmDialog(this, "Bạn muốn cập nhật sinh viên này?", "Confirm Update", JOptionPane.YES_OPTION) == 0)) {
            return;
        }
        String idStr = tbGrade.getValueAt(tbGrade.getSelectedRow(), 0) + "";
        updateGrade.setId(Integer.parseInt(idStr));
        Integer status = service.update(updateGrade);
        if (status == 0) {
//            loadTable(service.getList());
            loadTable(service.getTop());
            clear();
            JOptionPane.showMessageDialog(this, "Sửa thành công", "Update", JOptionPane.INFORMATION_MESSAGE);
        } else if (status == 1) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không tồn tại", "Sửa điểm", JOptionPane.WARNING_MESSAGE);
        } else if (status == 2) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên bạn sửa đã có điểm", "Sửa điểm", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "lỗi chương trình",
                    "Update fail", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // DELETE BTN        
        if (tbGrade.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Mời chọn một sinh viên trong bảng để xóa",
                    "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!(JOptionPane.showConfirmDialog(this, "Bạn muốn xóa điểm sinh viên này?", "Xóa điểm", JOptionPane.YES_OPTION) == 0)) {
            return;
        }
        String studentIdStr = tbGrade.getValueAt(tbGrade.getSelectedRow(), 1).toString();
        if (!txtStudentId.getText().equals(studentIdStr)) {
            JOptionPane.showMessageDialog(this, "Vui lòng không thay đổi mã sinh viên trước khi xóa", "Xóa điểm", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idStr = tbGrade.getValueAt(tbGrade.getSelectedRow(), 0).toString();
        Integer status = service.delete(Integer.parseInt(idStr));
        if (status == 0) {
//            loadTable(service.getList());
            loadTable(service.getTop());
            clear();
            JOptionPane.showMessageDialog(this, "Xóa thành công", "Xóa điểm", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi chương trình", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        //SEARCH BTN
        String findStudentId = txtSearch.getText().trim();
        if (findStudentId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Mời nhập MSV để tìm kiếm", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        GradeResponse gradeResponse = service.findByStudentCode(findStudentId);
        if (gradeResponse == null) {
            JOptionPane.showMessageDialog(this, "Sinh viên này chưa có trong bảng điểm",
                    "Fail", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lbId.setText(String.valueOf(gradeResponse.getId()));
        txtFullname.setText(gradeResponse.getFullname());
        txtStudentId.setText(gradeResponse.getStudentCode());
        txtEnglish.setText(String.valueOf(gradeResponse.getEnglish()));
        txtInformatics.setText(String.valueOf(gradeResponse.getInformatics()));
        txtGymnastics.setText(String.valueOf(gradeResponse.getGymnastics()));
        lbAvg.setText(String.valueOf(gradeResponse.getAverage()));
    }//GEN-LAST:event_btnSearchMouseClicked

    private void btnLastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLastMouseClicked
        //LAST BTN
        tbGrade.changeSelection(tbGrade.getRowCount() - 1, 1, false, false);
        showSelectedRow(tbGrade.getRowCount() - 1);
    }//GEN-LAST:event_btnLastMouseClicked

    private void btnFirstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirstMouseClicked
        //FIRST BTN
        tbGrade.changeSelection(0, 1, false, false);
        showSelectedRow(0);
    }//GEN-LAST:event_btnFirstMouseClicked

    private void btnNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseClicked
        // NEXT BTN
        int selectedIndex = 0;
        if (tbGrade.getSelectedRow() == -1) {
            selectedIndex = -1;
        } else {
            selectedIndex = tbGrade.getSelectedRow();
        }
        selectedIndex += 1;
        if (selectedIndex >= tbGrade.getRowCount()) {
            selectedIndex = 0;
        }
        tbGrade.changeSelection(selectedIndex, 1, false, false);
        showSelectedRow(selectedIndex);
    }//GEN-LAST:event_btnNextMouseClicked

    private void btnPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevMouseClicked
        // PREV BTN
        int selectedIndex = -1;
        if (tbGrade.getSelectedRow() == -1) {
            selectedIndex = tbGrade.getRowCount();
        } else {
            selectedIndex = tbGrade.getSelectedRow();
        }
        selectedIndex -= 1;
        if (selectedIndex < 0) {
            selectedIndex = tbGrade.getRowCount() - 1;
        }
        tbGrade.changeSelection(selectedIndex, 1, false, false);
        showSelectedRow(selectedIndex);
    }//GEN-LAST:event_btnPrevMouseClicked

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // SAVE BTN    
        if (!lbId.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Sinh viên đã điểm", "Thêm mới", JOptionPane.WARNING_MESSAGE);
            return;
        }
        GradeRequest gradeRequest = getInput();
        if (gradeRequest == null) {
            return;
        }
        Integer status = service.add(gradeRequest);
        if (status == 0) {
            loadTable(service.getTop());
//            loadTable(service.getList());
            clear();
            JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm điểm", JOptionPane.INFORMATION_MESSAGE);
        } else if (status == 1) {
            JOptionPane.showMessageDialog(this, "Sinh viên không tồn tại", "Thêm điểm", JOptionPane.WARNING_MESSAGE);
        } else if (status == 2) {
            JOptionPane.showMessageDialog(this, "Sinh viên đã có điểm", "Thêm điểm", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowMouseClicked
        // Show BTN
        loadTable(service.getList());
    }//GEN-LAST:event_btnShowMouseClicked

    private void btnTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTopMouseClicked
        // TOP BTN
        loadTable(service.getTop());
    }//GEN-LAST:event_btnTopMouseClicked

    private void txtStudentIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStudentIdKeyReleased
        //StudentId textfeild key Released
        String studentId = txtStudentId.getText().trim();
        if (!studentId.isBlank()) {
            
            Student student = studentService.findByStudentCode(studentId);
            if (!(student == null)) {
                
                txtFullname.setText(student.getFullName());
            }          
        } else {
            txtFullname.setText("");            
        }
    }//GEN-LAST:event_txtStudentIdKeyReleased

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GradeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GradeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GradeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GradeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GradeManagement().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GradeManagementPanel;
    private javax.swing.JLabel btnCancel;
    private javax.swing.JLabel btnDelete;
    private javax.swing.JLabel btnFirst;
    private javax.swing.JLabel btnLast;
    private javax.swing.JLabel btnNew;
    private javax.swing.JLabel btnNext;
    private javax.swing.JLabel btnPrev;
    private javax.swing.JLabel btnSave;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnShow;
    private javax.swing.JLabel btnTop;
    private javax.swing.JLabel btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbAvg;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbId;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JTable tbGrade;
    private javax.swing.JTextField txtEnglish;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JTextField txtGymnastics;
    private javax.swing.JTextField txtInformatics;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStudentId;
    // End of variables declaration//GEN-END:variables
}
