package org.cps731.project.team.cps731.pomodoro.dto.auth;

public class RegisterProfessorRequestDTO {
    private Long professorID;
    private String name;
    private String email;
    private String password;

    public RegisterProfessorRequestDTO(Long professorID, String name, String password, String email) {
        this.professorID = professorID;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public RegisterProfessorRequestDTO() {
    }

    public Long getProfessorID() {
        return professorID;
    }

    public void setProfessorID(Long professorID) {
        this.professorID = professorID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RegisterProfessorRequestDTO)) return false;
        final RegisterProfessorRequestDTO other = (RegisterProfessorRequestDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$professorID = this.getProfessorID();
        final Object other$professorID = other.getProfessorID();
        if (this$professorID == null ? other$professorID != null : !this$professorID.equals(other$professorID))
            return false;
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
        return other instanceof RegisterProfessorRequestDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $professorID = this.getProfessorID();
        result = result * PRIME + ($professorID == null ? 43 : $professorID.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        return result;
    }
}
