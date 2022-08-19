/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.sonpt.models;

/**
 *
 * @author sonpt_ph19600
 */
public class GradeResponse {
    private Integer id;
    private String studentCode;
    private String fullname;
    private Integer english;
    private Integer informatics;
    private Integer gymnastics;
    private float average;

    public GradeResponse() {
    }

    public GradeResponse(Integer id, String studentCode, String fullname, Integer english, Integer informatics, Integer gymnastics, float average) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullname = fullname;
        this.english = english;
        this.informatics = informatics;
        this.gymnastics = gymnastics;
        this.average = average;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    
}
