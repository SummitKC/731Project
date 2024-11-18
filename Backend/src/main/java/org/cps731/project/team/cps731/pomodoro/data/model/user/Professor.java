package org.cps731.project.team.cps731.pomodoro.data.model.user;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;

import java.util.Set;

@Entity
public class Professor {

    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    private User user;
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Course> createdCourses;

    public Professor(Long id, User user, Set<Course> createdCourses) {
        this.id = id;
        this.user = user;
        this.createdCourses = createdCourses;
    }

    public Professor(User user) {
        this.id = user.getId();
        this.user = user;
    }

    public Professor() {
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Set<Course> getCreatedCourses() {
        return this.createdCourses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedCourses(Set<Course> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Professor)) return false;
        final Professor other = (Professor) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Professor;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
