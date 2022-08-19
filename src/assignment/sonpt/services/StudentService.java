package assignment.sonpt.services;

import assignment.sonpt.models.Student;
import assignment.sonpt.repositories.StudentRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author sonpt_ph19600
 */
public class StudentService {

    private StudentRepository studentRepository;
//    private static int countManipulation = 0;
    private static List<Student> lstAvatarAdd = new ArrayList<>();

    public StudentService() {
        studentRepository = new StudentRepository();
    }

    private String saveAvatar(String sourcePath, String studentCode) {
        File avatarFolder = new File("avatarFolder");
        String avatarFolderPath = avatarFolderPath = avatarFolder.getAbsolutePath();

        //Tạo file lưu ảnh của SV
        File soureAvatar = new File(sourcePath);
        //Lấy đường dẫn ảnh SV sau khi lưu thành công
        String sourceAvatarType = sourcePath.substring(sourcePath.lastIndexOf("."));

        String studentAvatarPath = avatarFolderPath + "\\" + studentCode + sourceAvatarType;

        //Copy avatar đến thư mục project
        File studentAvatar = new File(studentAvatarPath);

        try {
            Files.copy(soureAvatar.toPath(), studentAvatar.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return studentCode + sourceAvatarType;
    }

    public Integer add(Student student) {
        if (!(studentRepository.findByStudentCode(student.getStudentCode()) == null)) {
            return 1; //Trùng mã sinh viên
        }
        if (!student.getAvatar().equals("No Avatar")) {
            String avatarPath = saveAvatar(student.getAvatar(), student.getStudentCode());
            if (avatarPath.isBlank()) {
                return -2;
            }
            student.setAvatar(avatarPath);
        }
        boolean status = studentRepository.add(student);
        if (status) {
            return 0;
        }
        return -1;
    }

    public Integer update(Student updateStudent) {
        Student findStudentById = studentRepository.findById(updateStudent.getId());
        if (!findStudentById.getStudentCode().equals(updateStudent.getStudentCode())) { //Kiểm tra xem có thay đổi Mã sv không
            if (!(studentRepository.findByStudentCode(updateStudent.getStudentCode()) == null)) {
                return 1; //Trùng mã sv                
            }
        }
        if (!updateStudent.getAvatar().equals("No Avatar")) {
            String avatarPath = saveAvatar(updateStudent.getAvatar(), updateStudent.getStudentCode());
            if (avatarPath.isBlank()) {
                return -2;
            }
            updateStudent.setAvatar(avatarPath);
        }
        if (studentRepository.update(updateStudent)) {
            return 0;
        }
        return -1;
    }

    public boolean delete(Integer studentId) {
        return studentRepository.delete(studentId);
    }

    public List<Student> getList() {
        return studentRepository.getList();
    }

    public Student findById(Integer idRequest) {
        return studentRepository.findById(idRequest);
    }

    public Student findByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    public List<Student> importFile(File file) {
        List<Student> lstStudent = new ArrayList<>();
        List<Student> lstFail = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dft = new DataFormatter();
            Iterator<Row> itegator = sheet.iterator();
            while (itegator.hasNext()) {
                Row row = itegator.next();
                if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                    continue;
                }
                String studentCode = row.getCell(0).getStringCellValue();
                String fullname = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                String phone = row.getCell(3).getStringCellValue();
                String genderStr = row.getCell(4).getStringCellValue();
                String address = row.getCell(5).getStringCellValue();
                Integer gender = -1;
                if (genderStr.equals("Nam")) {
                    gender = 1;
                } else {
                    gender = 0;
                }

                Student student = new Student(0, studentCode, fullname, email, phone, gender, address, "No Image");
                lstStudent.add(student);
            }
            workbook.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        if (lstStudent.isEmpty()) {
            return null;
        }
        for (Student student : lstStudent) {
            if (!(studentRepository.findByStudentCode(student.getStudentCode()) == null)) {
                lstFail.add(student);
            } else {
                studentRepository.add(student);
            }
        }

        return lstFail;
    }
    
    public boolean export(File file){
        List<Student> lst = studentRepository.getList();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Danh sách sinh viên");
            int rowNum = 0;
            Row firstRow = sheet.createRow(rowNum++);
            Cell idTitle = firstRow.createCell(0);
            idTitle.setCellValue("ID");
            Cell studentCodeTitle = firstRow.createCell(1);
            studentCodeTitle.setCellValue("Mã sinh viên");
            Cell fullnameTitle = firstRow.createCell(2);
            fullnameTitle.setCellValue("Họ tên");
            Cell emailTitle = firstRow.createCell(3);
            emailTitle.setCellValue("Email");
            Cell phoneTitle = firstRow.createCell(4);
            phoneTitle.setCellValue("SĐT");
            Cell genderTitle = firstRow.createCell(5);
            genderTitle.setCellValue("Giới tính");
            Cell addressTitle = firstRow.createCell(6);
            addressTitle.setCellValue("Địa chỉ");
            for (Student x : lst) {
                Row row = sheet.createRow(rowNum++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(x.getId());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(x.getStudentCode());
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(x.getFullName());
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(x.getEmail());
                Cell cell5 = row.createCell(4);
                cell5.setCellValue(x.getPhone());
                Cell cell6 = row.createCell(5);
                cell6.setCellValue(x.getGender() == 1 ? "Nam" : "Nữ");
                Cell cell7 = row.createCell(6);
                cell7.setCellValue(x.getAddress());
            }
            workbook.write(fos);
            workbook.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
