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

    public StudentProfileDTO() {
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StudentProfileDTO)) return false;
        final StudentProfileDTO other = (StudentProfileDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$studentID = this.getStudentID();
        final Object other$studentID = other.getStudentID();
        if (this$studentID == null ? other$studentID != null : !this$studentID.equals(other$studentID)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof StudentProfileDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $studentID = this.getStudentID();
        result = result * PRIME + ($studentID == null ? 43 : $studentID.hashCode());
        return result;
    }

    public String toString() {
        return "StudentProfileDTO(name=" + this.getName() + ", email=" + this.getEmail() + ", studentID=" + this.getStudentID() + ")";
    }
}
