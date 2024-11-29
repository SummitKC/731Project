package org.cps731.project.team.cps731.pomodoro.dto.professor;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;

public class ProfessorProfileDTO {
    private Long employeeID;
    private String email;
    private String name;

    public ProfessorProfileDTO() {
      
    }

    public ProfessorProfileDTO(Long employeeID, String email, String name) {
        this.employeeID = employeeID;
        this.email = email;
        this.name = name;
    }

    public ProfessorProfileDTO(Professor professor) {
        this.employeeID = professor.getEmployeeID();
        this.email = professor.getUser().getEmail();
        this.name = professor.getUser().getName();
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

    public Long getEmployeeID() {
      return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
      this.employeeID = employeeID;
    }
}
