package org.cps731.project.team.cps731.pomodoro.dto.auth;

public class RegisterStudentRequestDTO {
    private Long studentID;
    private String name;
    private String email;
    private String password;

    public RegisterStudentRequestDTO(Long studentID, String name, String email, String password) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public RegisterStudentRequestDTO() {
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RegisterStudentRequestDTO)) return false;
        final RegisterStudentRequestDTO other = (RegisterStudentRequestDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userID = this.getStudentID();
        final Object other$userID = other.getStudentID();
        if (this$userID == null ? other$userID != null : !this$userID.equals(other$userID)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RegisterStudentRequestDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userID = this.getStudentID();
        result = result * PRIME + ($userID == null ? 43 : $userID.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        return result;
    }
}
