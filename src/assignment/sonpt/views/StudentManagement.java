package assignment.sonpt.views;

import assignment.sonpt.models.Student;
import assignment.sonpt.services.StudentService;
import assignment.sonpt.thread.ClockThread;
import assignment.sonpt.thread.TitleMotion;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sonpt_ph19600
 */
public class StudentManagement extends javax.swing.JFrame {

    private StudentService studentService;
    private ClockThread clock;
    private TitleMotion titleMotion;
    private String inputAvatarPath = "No Avatar";
    private File folder;
    private static String avatarFolder = "";
    private WebcamPanel webcamPanel;
    private Webcam webcam;
    private Thread capture;

    public StudentManagement() {
        initComponents();
        setLocationRelativeTo(null);
        studentService = new StudentService();
        folder = new File("avatarFolder");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        avatarFolder = folder.getAbsolutePath();
        ClockThread.jLabel = lbClock;
        clock = new ClockThread();
        clock.start();
        TitleMotion.jLabel = lbTitle;
        titleMotion = new TitleMotion();
        titleMotion.start();
        loadTable(studentService.getList());

    }

    private void initWebcam() {
        Dimension size = WebcamResolution.VGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setPreferredSize(size);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setVisible(true);
        webcamPanel.setDisplayDebugInfo(true);
        webcamPanel.setImageSizeDisplayed(true);
        webcamPanel.setMirrored(true);

        wcPanel.add(webcamPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 300));
        wcPanel.getParent().revalidate(); 
    }

    public void captureThread() {
        capture = new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    Result result = null;
                    BufferedImage image = null;

                    if (webcam.isOpen()) {
                        if ((image = webcam.getImage()) == null) {
                            continue;
                        }
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException ex) {
                        ex.printStackTrace();
                    }
                    if (result != null) {
                        txtResult.setText(result.getText());
                    }

                } while (true);
            }
        };
        capture.setDaemon(true);
        capture.start();
    }
    
    private void loadTable(List<Student> lstStudent) {
        if (lstStudent == null) {
            JOptionPane.showMessageDialog(this, "Lỗi chương trình", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (lstStudent.isEmpty()) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tbStudent.getModel();
        model.setRowCount(0);
        for (Student x : lstStudent) {
            Object[] objects = new Object[]{
                x.getId(),
                x.getStudentCode(),
                x.getFullName(),
                x.getEmail(),
                x.getPhone(),
                x.getGender() == 1 ? "Nam" : "Nữ",
                x.getAddress(),
                x.getAvatar()
            };
            model.addRow(objects);
        }
    }

    private Student getInput() {
        String studentCode = txtStudentCode.getText().trim();
        String fullname = txtFullname.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        Integer gender = rdMale.isSelected() ? 1 : rdFemale.isSelected() ? 0 : -1;
        String address = txtAddress.getText().trim();

        Pattern studentCodePattern = Pattern.compile("^PH[0-9]{5}");
        Matcher studentCodeMatcher = studentCodePattern.matcher(studentCode);
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$");
        Matcher emailMatcher = emailPattern.matcher(email);
        Pattern phonePattern = Pattern.compile("\\d{10,11}");
        Matcher phoneMatcher = phonePattern.matcher(phone);

        if (fullname.isBlank() || email.isBlank()
                || phone.isBlank() || address.isBlank()
                || gender == -1 || studentCode.isBlank()) {
            JOptionPane.showMessageDialog(this, "Bạn cần điền đầy đủ thông tin!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            if (address.isBlank()) {
                txtAddress.requestFocus();
            }
            if (phone.isBlank()) {
                txtPhone.requestFocus();
            }
            if (email.isBlank()) {
                txtEmail.requestFocus();
            }
            if (fullname.isBlank()) {
                txtFullname.requestFocus();
            }
            if (studentCode.isBlank()) {
                txtStudentCode.requestFocus();
            }
            return null;
        }

        if (!studentCodeMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không đúng định dạng!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!emailMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!phoneMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Student inputStudent = new Student(0, studentCode, fullname, email, phone, gender, address, inputAvatarPath);
        return inputStudent;
    }

    private void clear() {
        lbId.setText("-");
        txtStudentCode.setText("");
        txtFullname.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        btnGrGender.clearSelection();
        lbAvatar.setIcon(new ImageIcon(getClass().getResource("/assignment/sonpt/icons/noImage.png")));
        tbStudent.clearSelection();
        inputAvatarPath = "No Avatar";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrGender = new javax.swing.ButtonGroup();
        popupAvatar = new javax.swing.JPopupMenu();
        openIterm = new javax.swing.JMenuItem();
        showIterm = new javax.swing.JMenuItem();
        clearIterm = new javax.swing.JMenuItem();
        failFrame = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbFail = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        webcamFrame = new javax.swing.JFrame();
        jPanel13 = new javax.swing.JPanel();
        wcPanel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtResult = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        btnOkWebcam = new javax.swing.JButton();
        studentManagementPanel = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbStudent = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnNew = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSave = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnDelete1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnFind = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnShow = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        rdFemale = new javax.swing.JRadioButton();
        rdMale = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFullname = new javax.swing.JTextField();
        txtStudentCode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbId = new javax.swing.JLabel();
        lbAvatar = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnImport = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnExport = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        btnScan = new javax.swing.JLabel();

        popupAvatar.setBackground(new java.awt.Color(255, 255, 255));
        popupAvatar.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        popupAvatar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 204)));

        openIterm.setBackground(new java.awt.Color(255, 255, 255));
        openIterm.setFont(new java.awt.Font("Nunito", 0, 14)); // NOI18N
        openIterm.setText("Open File");
        openIterm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openItermActionPerformed(evt);
            }
        });
        popupAvatar.add(openIterm);

        showIterm.setFont(new java.awt.Font("Nunito", 0, 14)); // NOI18N
        showIterm.setText("Show Avatar");
        showIterm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showItermActionPerformed(evt);
            }
        });
        popupAvatar.add(showIterm);

        clearIterm.setFont(new java.awt.Font("Nunito", 0, 14)); // NOI18N
        clearIterm.setText("Clear Avatar");
        clearIterm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearItermActionPerformed(evt);
            }
        });
        popupAvatar.add(clearIterm);

        failFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        failFrame.setBackground(new java.awt.Color(255, 255, 255));
        failFrame.setSize(new java.awt.Dimension(789, 430));
        failFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(790, 430));

        tbFail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã SV", "Họ tên", "Email", "SĐT", "Giới tính", "Địa chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbFail);
        if (tbFail.getColumnModel().getColumnCount() > 0) {
            tbFail.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbFail.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbFail.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbFail.getColumnModel().getColumn(5).setPreferredWidth(20);
            tbFail.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        btnOk.setBackground(new java.awt.Color(1, 181, 204));
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Nunito", 0, 24)); // NOI18N
        jLabel9.setText("Danh sách sinh viên bị trùng mã sinh viên");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jLabel9)
                .addContainerGap(187, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        failFrame.getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 430));

        webcamFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        webcamFrame.setSize(new java.awt.Dimension(840, 450));
        webcamFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                webcamFrameWindowClosing(evt);
            }
        });
        webcamFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        wcPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        wcPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(1, 181, 204));
        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Nunito", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Thông tin sinh viên");

        txtResult.setColumns(15);
        txtResult.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtResult.setRows(5);
        txtResult.setTabSize(5);
        jScrollPane4.setViewportView(txtResult);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Nunito", 0, 24)); // NOI18N
        jLabel8.setText("Scan Here");

        btnOkWebcam.setBackground(new java.awt.Color(1, 181, 204));
        btnOkWebcam.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnOkWebcam.setForeground(new java.awt.Color(255, 255, 255));
        btnOkWebcam.setText("OK");
        btnOkWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkWebcamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOkWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(wcPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wcPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        webcamFrame.getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 420));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        studentManagementPanel.setBackground(new java.awt.Color(255, 255, 255));
        studentManagementPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        studentManagementPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitle.setFont(new java.awt.Font("Nunito", 0, 36)); // NOI18N
        lbTitle.setText("Quản Lý Sinh Viên");
        studentManagementPanel.add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 37, -1, 46));

        lbClock.setFont(new java.awt.Font("Nunito", 0, 20)); // NOI18N
        lbClock.setForeground(new java.awt.Color(255, 0, 51));
        lbClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbClock.setText("Clock");
        studentManagementPanel.add(lbClock, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 15, 108, -1));

        tbStudent.setFont(new java.awt.Font("Nunito", 0, 14)); // NOI18N
        tbStudent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mã SV", "Họ tên", "Email", "Số ĐT", "Giới tính", "Địa chỉ", "Hình"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbStudent.setRowHeight(25);
        tbStudent.getTableHeader().setReorderingAllowed(false);
        tbStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStudentMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbStudent);
        if (tbStudent.getColumnModel().getColumnCount() > 0) {
            tbStudent.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbStudent.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbStudent.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbStudent.getColumnModel().getColumn(4).setPreferredWidth(60);
            tbStudent.getColumnModel().getColumn(5).setPreferredWidth(30);
            tbStudent.getColumnModel().getColumn(7).setPreferredWidth(40);
        }

        studentManagementPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 720, 250));

        jPanel2.setBackground(new java.awt.Color(1, 181, 204));

        btnNew.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/new.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, 100, 35));

        jPanel3.setBackground(new java.awt.Color(1, 181, 204));

        btnUpdate.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/edit.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 150, -1, -1));

        jPanel4.setBackground(new java.awt.Color(1, 181, 204));

        btnSave.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, -1, -1));

        jPanel5.setBackground(new java.awt.Color(1, 181, 204));

        btnDelete1.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnDelete1.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/delete.png"))); // NOI18N
        btnDelete1.setText("Delete");
        btnDelete1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDelete1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 250, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 750, -1, -1));

        jPanel7.setBackground(new java.awt.Color(1, 181, 204));

        btnFind.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnFind.setForeground(new java.awt.Color(255, 255, 255));
        btnFind.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/find.png"))); // NOI18N
        btnFind.setText("Find");
        btnFind.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFind.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFindMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, -1, -1));

        jPanel8.setBackground(new java.awt.Color(1, 181, 204));

        btnShow.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnShow.setForeground(new java.awt.Color(255, 255, 255));
        btnShow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnShow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/show.png"))); // NOI18N
        btnShow.setText("Show");
        btnShow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 350, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));

        txtAddress.setColumns(10);
        txtAddress.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtAddress.setLineWrap(true);
        txtAddress.setRows(3);
        txtAddress.setTabSize(2);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jScrollPane1.setViewportView(txtAddress);

        jLabel6.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel6.setText("Địa chỉ:");

        rdFemale.setBackground(new java.awt.Color(255, 255, 255));
        btnGrGender.add(rdFemale);
        rdFemale.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        rdFemale.setText("Nữ");

        rdMale.setBackground(new java.awt.Color(255, 255, 255));
        btnGrGender.add(rdMale);
        rdMale.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        rdMale.setText("Nam");

        jLabel5.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel5.setText("Giới tính:");

        jLabel4.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel4.setText("Số ĐT:");

        txtPhone.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtPhone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txtEmail.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel3.setText("Email:");

        jLabel2.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel2.setText("Họ tên:");

        txtFullname.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtFullname.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txtStudentCode.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        txtStudentCode.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel1.setText("Mã SV:");

        jLabel7.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        jLabel7.setText("ID:");

        lbId.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        lbId.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(17, 17, 17)
                        .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdMale)
                        .addGap(6, 6, 6)
                        .addComponent(rdFemale))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbId, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStudentCode, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStudentCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdMale)
                            .addComponent(rdFemale)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        studentManagementPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 340, 380));

        lbAvatar.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        lbAvatar.setForeground(new java.awt.Color(204, 204, 204));
        lbAvatar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/noImage.png"))); // NOI18N
        lbAvatar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(1, 181, 204)));
        lbAvatar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbAvatar.setFocusTraversalPolicyProvider(true);
        lbAvatar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAvatarMouseClicked(evt);
            }
        });
        studentManagementPanel.add(lbAvatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 260, 360));

        jPanel9.setBackground(new java.awt.Color(1, 181, 204));

        btnImport.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnImport.setForeground(new java.awt.Color(255, 255, 255));
        btnImport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/import.png"))); // NOI18N
        btnImport.setText("Import");
        btnImport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 400, -1, -1));

        jPanel11.setBackground(new java.awt.Color(1, 181, 204));

        btnExport.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/export.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 450, -1, -1));

        jPanel14.setBackground(new java.awt.Color(1, 181, 204));

        btnScan.setFont(new java.awt.Font("Nunito", 0, 18)); // NOI18N
        btnScan.setForeground(new java.awt.Color(255, 255, 255));
        btnScan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnScan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assignment/sonpt/icons/scan.png"))); // NOI18N
        btnScan.setText("Scan");
        btnScan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnScan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnScanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnScan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnScan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        studentManagementPanel.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        getContentPane().add(studentManagementPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // CANCEL BTN
        if (JOptionPane.showConfirmDialog(this, "Thoát chương trình?", "Exit", JOptionPane.YES_OPTION) == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnCancelMouseClicked

    private void tbStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMouseClicked
        // TABLE CLICK
        int row = tbStudent.getSelectedRow();
        lbId.setText(tbStudent.getValueAt(row, 0) + "");
        txtStudentCode.setText(tbStudent.getValueAt(row, 1) + "");
        txtFullname.setText(tbStudent.getValueAt(row, 2) + "");
        txtEmail.setText(tbStudent.getValueAt(row, 3) + "");
        txtPhone.setText(tbStudent.getValueAt(row, 4) + "");
        String genderStr = tbStudent.getValueAt(row, 5) + "";
        if (genderStr.equals("Nam")) {
            rdMale.setSelected(true);
        } else {
            rdFemale.setSelected(true);
        }
        txtAddress.setText(tbStudent.getValueAt(row, 6) + "");
        String avatar = tbStudent.getValueAt(row, 7) + "";
        if (avatar.equals("No Avatar")) {
            lbAvatar.setIcon(new ImageIcon(getClass().getResource("/assignment/sonpt/icons/noImage.png")));
            inputAvatarPath = "No Avatar";
        } else {
            Image avatarImg = null;
            try {
                File file = new File(avatarFolder + "\\" + avatar);
                inputAvatarPath = file.getAbsolutePath();
                avatarImg = ImageIO.read(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi File ảnh. Kiểm tra lại folder ảnh", "Fail", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImageIcon icon = new ImageIcon(avatarImg.getScaledInstance(260, 360, Image.SCALE_SMOOTH));
            lbAvatar.setIcon(icon);
        }
    }//GEN-LAST:event_tbStudentMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // NEW BTN
        clear();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // UPDATE BTN
        if (tbStudent.getSelectedRow() == -1 || lbId.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Mời chọn một sinh viên sửa cập nhật", "Update", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Student updateStudent = getInput();
        if (updateStudent == null) {
            return;
        }
        updateStudent.setId(Integer.parseInt(lbId.getText()));
        String mess = "Bạn muốn cập nhật sinh viên: " + tbStudent.getValueAt(tbStudent.getSelectedRow(), 1);
        if (!(JOptionPane.showConfirmDialog(this, mess, "Update confirm", JOptionPane.YES_OPTION) == 0)) {
            return;
        }
        Integer status = studentService.update(updateStudent);
        if (status == 0) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Update", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadTable(studentService.getList());
        } else if (status == 1) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại", "Update", JOptionPane.ERROR_MESSAGE);
        } else if (status == -2) {
            JOptionPane.showMessageDialog(this, "Sửa thất bại. Lỗi file ảnh", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi chương trình. Mời khởi động lại chương trình", "Update", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void openItermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openItermActionPerformed
        //Open Avatar click
        JFileChooser avatarChooser = new JFileChooser("D:\\");
        FileNameExtensionFilter avatarFilter = new FileNameExtensionFilter("Image File", "png", "jpg", "jpeg");
        avatarChooser.setFileFilter(avatarFilter);
        avatarChooser.setAcceptAllFileFilterUsed(false);
        int selectFileCheck = avatarChooser.showOpenDialog(this);
        File selected = avatarChooser.getSelectedFile();

        if (selectFileCheck == JFileChooser.APPROVE_OPTION) {
            inputAvatarPath = avatarChooser.getSelectedFile().getPath();
            Image avatar = null;
            try {
                avatar = ImageIO.read(avatarChooser.getSelectedFile());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Import hình đại diện không thành công", "Fail", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImageIcon icon = new ImageIcon(avatar.getScaledInstance(260, 360, Image.SCALE_SMOOTH));
            lbAvatar.setIcon(icon);
        }
    }//GEN-LAST:event_openItermActionPerformed

    private void showItermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showItermActionPerformed
        // SHOW AVATAR
        if (!inputAvatarPath.equals("No Avatar")) {
            try {
                Image img = ImageIO.read(new File(inputAvatarPath));

                ImageIcon icon = new ImageIcon(img.getScaledInstance(390, 540, Image.SCALE_SMOOTH));
                Object[] option = {"OK"};
                JOptionPane.showOptionDialog(this, "", "Avatar", JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE, icon, option, option[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có avatar", "Notifications", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_showItermActionPerformed

    private void clearItermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearItermActionPerformed
        // CLEAR AVATAR
        lbAvatar.setIcon(new ImageIcon(getClass().getResource("/assignment/sonpt/icons/noImage.png")));
        inputAvatarPath = "No Avatar";
    }//GEN-LAST:event_clearItermActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // SAVE BTN
        if (!lbId.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Sinh viên đã có trong bảng sinh viên", "Thêm sinh viên", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Student student = getInput();
        if (student == null) {
            return;
        }
        int status = studentService.add(student);
        if (status == 0) {
            JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm sinh viên", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadTable(studentService.getList());
        } else if (status == 1) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại", "Thêm sinh viên", JOptionPane.WARNING_MESSAGE);
        } else if (status == -2) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Lỗi file ảnh", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Lỗi chương trình", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnDelete1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete1MouseClicked
        // DELETE BTN
        if (tbStudent.getSelectedRow() == -1 || lbId.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Mời chọn một sinh viên để xóa", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = tbStudent.getSelectedRow();
        String studentCode = tbStudent.getValueAt(row, 1) + "";
        if (!txtStudentCode.getText().equals(studentCode)) {
            JOptionPane.showMessageDialog(this, "Vui lòng không thay đổi mã "
                    + "sinh viên trước khi xóa", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer id = Integer.parseInt(tbStudent.getValueAt(row, 0) + "");
        String mess = "Bạn muốn xóa sinh viên: " + tbStudent.getValueAt(row, 1);
        if (!(JOptionPane.showConfirmDialog(this, mess,
                "Delete confirm", JOptionPane.YES_OPTION) == 0)) {
            return;
        }
        boolean status = studentService.delete(id);
        if (status) {
            JOptionPane.showMessageDialog(this, "Xóa thành công", "Delete", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadTable(studentService.getList());
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại. Lỗi chương trình", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDelete1MouseClicked

    private void btnFindMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFindMouseClicked
        // FIND BTN
        List<Student> lstFind = new ArrayList<>();
        String studentCode = (String) JOptionPane.showInputDialog(this, "Nhập Mã sinh viên:");
        Student student = studentService.findByStudentCode(studentCode);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên", "Find fail", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lstFind.add(student);
        loadTable(lstFind);
    }//GEN-LAST:event_btnFindMouseClicked

    private void btnShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowMouseClicked
        //SHOW BTN
        loadTable(studentService.getList());
    }//GEN-LAST:event_btnShowMouseClicked

    private void lbAvatarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbAvatarMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            popupAvatar.show(lbAvatar, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_lbAvatarMouseClicked

    private void btnImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportMouseClicked
        int select = -1;
        ImageIcon icon = new ImageIcon(getClass().getResource("/assignment/sonpt/icons/sample.png"));
        Object[] option = {"OK", "Cancel"};
        select = JOptionPane.showOptionDialog(this, "", "Mẫu import",
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE, icon, option, option[0]);

        if (!(select == 0)) {
            return;
        }
        JFileChooser avatarChooser = new JFileChooser("D:\\");
        FileNameExtensionFilter avatarFilter = new FileNameExtensionFilter("Exel File", "xlsx");
        avatarChooser.setFileFilter(avatarFilter);
        avatarChooser.setAcceptAllFileFilterUsed(false);
        int selectFileCheck = avatarChooser.showOpenDialog(this);
        File selectedFile = avatarChooser.getSelectedFile();
        if (!(selectFileCheck == JFileChooser.APPROVE_OPTION)) {
            return;
        }
        List<Student> lstFail = studentService.importFile(selectedFile);
        if (!lstFail.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) tbFail.getModel();
            model.setRowCount(0);
            int i = 0;
            for (Student x : lstFail) {
                model.addRow(new Object[]{
                    i += 1,
                    x.getStudentCode(),
                    x.getFullName(),
                    x.getEmail(),
                    x.getPhone(),
                    x.getGender() == 1 ? "Nam" : "Nữ",
                    x.getAddress()
                });
            }
            failFrame.setVisible(true);
            failFrame.setLocationRelativeTo(null);
            return;
        }
        if (lstFail == null) {
            JOptionPane.showMessageDialog(this, "Thêm tự động thành công", "Import", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Thêm thất bại", "Import", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnImportMouseClicked

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        //BTN OK
        failFrame.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportMouseClicked
        //BTN EXPORT
        JFileChooser fileChooser = new JFileChooser("D:\\");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File Exel (.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);
        int x = fileChooser.showSaveDialog(this);
        FileOutputStream fos = null;
        File file = fileChooser.getSelectedFile();
        if (!(x == JFileChooser.APPROVE_OPTION)) {
            return;
        }
        if (studentService.export(file)) {
            JOptionPane.showMessageDialog(this, "Xuất danh sách thành công", "Export", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thất bại", "Export", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExportMouseClicked

    private void btnOkWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkWebcamActionPerformed
        capture.stop();
        webcamFrame.dispose();
        webcam.close();
        String result = txtResult.getText();
        if (result.isBlank()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn thêm sinh viên này vào danh sách", "Thêm tự động", JOptionPane.YES_OPTION);
        if (!(confirm == 0)) {
            return;
        }
        String[] att = result.split("\n");
        String studentCode = att[0].trim();
        String fullname = att[1].trim();
        String email = att[2].trim();
        String phone = att[3].trim();
        String genderStr = att[4].trim();
        int gender = genderStr.equals("Nam") ? 1 : 0;
        String address = att[5].trim();
        Student newStudent = new Student(0, studentCode, fullname, email, phone, gender, address, "No Avatar");
        int status = studentService.add(newStudent);
        if (status == 0) {
            JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm sinh viên", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadTable(studentService.getList());
        } else if (status == 1) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại", "Thêm sinh viên", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Lỗi chương trình", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_btnOkWebcamActionPerformed

    private void btnScanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnScanMouseClicked
        //btn Scan
        txtResult.setText("");
        initWebcam();
        captureThread();
        webcamFrame.setVisible(true);
        webcamFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnScanMouseClicked

    private void webcamFrameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_webcamFrameWindowClosing
        webcam.close();
        capture.stop();
        webcamFrame.dispose();
    }//GEN-LAST:event_webcamFrameWindowClosing

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StudentManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new StudentManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnCancel;
    private javax.swing.JLabel btnDelete1;
    private javax.swing.JLabel btnExport;
    private javax.swing.JLabel btnFind;
    private javax.swing.ButtonGroup btnGrGender;
    private javax.swing.JLabel btnImport;
    private javax.swing.JLabel btnNew;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnOkWebcam;
    private javax.swing.JLabel btnSave;
    private javax.swing.JLabel btnScan;
    private javax.swing.JLabel btnShow;
    private javax.swing.JLabel btnUpdate;
    private javax.swing.JMenuItem clearIterm;
    private javax.swing.JFrame failFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbAvatar;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbId;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JMenuItem openIterm;
    private javax.swing.JPopupMenu popupAvatar;
    private javax.swing.JRadioButton rdFemale;
    private javax.swing.JRadioButton rdMale;
    private javax.swing.JMenuItem showIterm;
    private javax.swing.JPanel studentManagementPanel;
    private javax.swing.JTable tbFail;
    private javax.swing.JTable tbStudent;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextArea txtResult;
    private javax.swing.JTextField txtStudentCode;
    private javax.swing.JPanel wcPanel;
    private javax.swing.JFrame webcamFrame;
    // End of variables declaration//GEN-END:variables
}
