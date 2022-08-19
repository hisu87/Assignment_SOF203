/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.sonpt.models;



/**
 *
 * @author sonpt_ph19600
 */
public class Grade {
    private Integer id;
    private Integer studentId;
    private Integer english;
    private Integer informatics;
    private Integer gymnastics;

    
    public Grade() {
    }

    public Grade(Integer id, Integer studentId, Integer english, Integer informatics, Integer gymnastics) {
        this.id = id;
        this.studentId = studentId;
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

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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
