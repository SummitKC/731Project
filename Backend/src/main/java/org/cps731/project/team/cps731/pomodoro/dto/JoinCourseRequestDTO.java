package org.cps731.project.team.cps731.pomodoro.dto;

public class JoinCourseRequestDTO {

    private String courseCode;

    public JoinCourseRequestDTO(String courseCode) {
        this.courseCode = courseCode;
    }

    public JoinCourseRequestDTO() {
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
