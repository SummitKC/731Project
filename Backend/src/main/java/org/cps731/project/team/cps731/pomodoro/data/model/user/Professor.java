package org.cps731.project.team.cps731.pomodoro.data.model.user;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Entity
public class Professor {

    @Id
    private Long userID;
    @NaturalId
    private Long employeeID;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "userID")
    private User user;
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Course> createdCourses;

    public Professor(Long userID, Long employeeID, User user, Set<Course> createdCourses) {
        this.userID = userID;
        this.employeeID = employeeID;
        this.user = user;
        this.createdCourses = createdCourses;
    }

    public Professor(User user, Long employeeID) {
        this.employeeID = employeeID;
        this.userID = user.getId();
        this.user = user;
    }

    public Professor() {
    }

    public Long getUserID() {
        return this.userID;
    }

    public User getUser() {
        return this.user;
    }

    public Set<Course> getCreatedCourses() {
        return this.createdCourses;
    }

    public void setUserID(Long id) {
        this.userID = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedCourses(Set<Course> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public Long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Professor)) return false;
        final Professor other = (Professor) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getUserID();
        final Object other$id = other.getUserID();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Professor;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getUserID();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
