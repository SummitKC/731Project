package org.cps731.project.team.cps731.pomodoro.data.repo.user;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Student findAllByUser_Email(String email);
    boolean existsByStudentID(Long studentID);

}
