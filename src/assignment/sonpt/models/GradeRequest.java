package assignment.sonpt.models;

/**
 *
 * @author sonpt_ph19600
 */
public class GradeRequest {
    private Integer id;
    private String studentCode;
    private Integer english;
    private Integer informatics;
    private Integer gymnastics;

    public GradeRequest(Integer id, String studentCode, Integer english, Integer informatics, Integer gymnastics) {
        this.id = id;
        this.studentCode = studentCode;
        this.english = english;
        this.informatics = informatics;
        this.gymnastics = gymnastics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Integer getEnglish() {
        return english;
    }

    public void setEnglish(Integer english) {
        this.english = english;
    }

    public Integer getInformatics() {
        return informatics;
    }

    public void setInformatics(Integer informatics) {
        this.informatics = informatics;
    }

    public Integer getGymnastics() {
        return gymnastics;
    }

    public void setGymnastics(Integer gymnastics) {
        this.gymnastics = gymnastics;
    }

    
}
