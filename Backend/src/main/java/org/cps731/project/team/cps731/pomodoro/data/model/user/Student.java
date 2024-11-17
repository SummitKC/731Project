package org.cps731.project.team.cps731.pomodoro.data.model.user;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;

import java.util.Set;

@Entity
public class Student {

    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    private User user;
    @ManyToMany(mappedBy = "takenBy")
    private Set<Course> courses;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<WorkAnalytics> workAnalytics;

    public Student(User user, Set<Course> courses, Set<Task> tasks, Set<WorkAnalytics> workAnalytics) {
        this.user = user;
        this.courses = courses;
        this.tasks = tasks;
        this.workAnalytics = workAnalytics;
    }

    public Student(User user) {
        this.id = user.getId();
        this.user = user;
    }

    public Student() {
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public Set<WorkAnalytics> getWorkAnalytics() {
        return this.workAnalytics;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setWorkAnalytics(Set<WorkAnalytics> workAnalytics) {
        this.workAnalytics = workAnalytics;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Student)) return false;
        final Student other = (Student) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Student;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
