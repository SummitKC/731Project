package org.cps731.project.team.cps731.pomodoro.dto.student;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

public class StudentProfileDTO {
    private String name;
    private String email;
    private Long studentID;

    public StudentProfileDTO(String name, String email, Long studentID) {
        this.name = name;
        this.email = email;
        this.studentID = studentID;
    }

    public StudentProfileDTO(Student student) {
        name = student.getUser().getName();
        email = student.getUser().getEmail();
        studentID = student.getUser().getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }
}
