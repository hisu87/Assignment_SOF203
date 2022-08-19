

package assignment.sonpt.services;

import assignment.sonpt.models.Grade;
import assignment.sonpt.models.GradeRequest;
import assignment.sonpt.models.GradeResponse;
import assignment.sonpt.models.Student;
import assignment.sonpt.repositories.GradeRepository;
import assignment.sonpt.repositories.StudentRepository;
import java.util.List;

/**
 *
 * @author sonpt_ph19600
 */
public class GradeService{
    private GradeRepository gradeRepo;
    private StudentRepository studentRepo;
    
    public GradeService() {
        gradeRepo = new GradeRepository();
        studentRepo = new StudentRepository();
    }

    public Integer add(GradeRequest gradeRequest){
        Student student = studentRepo.findByStudentCode(gradeRequest.getStudentCode());
        if (student == null) {
            return 1; //Sinh viên không tồn tại
        }
        GradeResponse grade = gradeRepo.findByStudentId(student.getId());
        if (!(grade == null)) {
            return 2; //Sinh viên đã có trong bảng điểm;
        }        
        
        Grade newGrade = new Grade();
        newGrade.setStudentId(student.getId());
        newGrade.setEnglish(gradeRequest.getEnglish());
        newGrade.setInformatics(gradeRequest.getInformatics());
        newGrade.setGymnastics(gradeRequest.getGymnastics());
        return gradeRepo.add(newGrade) ? 0 : -1;
    }

    public Integer update(GradeRequest gradeRequest){        
        Student student = studentRepo.findByStudentCode(gradeRequest.getStudentCode());
        if (student == null) {
            return 1; //Sinh viên không tồn tại
        }
        Grade grade = gradeRepo.findById(gradeRequest.getId());
        if (grade.getStudentId() != student.getId()) {
            if (!(gradeRepo.findByStudentId(student.getId()) == null)) {
                return 2; //Sinh viên này đã có điểm
            }
        }
        Grade updateGrade = new Grade();
        updateGrade.setId(grade.getId());
        updateGrade.setStudentId(student.getId());
        updateGrade.setEnglish(gradeRequest.getEnglish());
        updateGrade.setInformatics(gradeRequest.getInformatics());
        updateGrade.setGymnastics(gradeRequest.getGymnastics());    
        return gradeRepo.update(updateGrade) ? 0 : -1;
    }

    public Integer delete(Integer id){
        return gradeRepo.delete(id) ? 0 : -1;
    }

    public List<GradeResponse> getList(){
        return gradeRepo.getList();
    }

    public GradeResponse findByStudentCode(String studentCode){
        Student student = studentRepo.findByStudentCode(studentCode);
        if (student == null) {
            return null;
        }
        GradeResponse gradeResponse = gradeRepo.findByStudentId(student.getId());
         
        return gradeResponse;
    }

    public List<GradeResponse> getTop(){
        return gradeRepo.getTop();
    }
}
