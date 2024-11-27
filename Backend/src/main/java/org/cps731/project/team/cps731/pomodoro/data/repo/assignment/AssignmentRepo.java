package org.cps731.project.team.cps731.pomodoro.data.repo.assignment;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {

    List<Assignment> findAllByAnnouncement_Course_CourseCode(String courseCode, PageRequest pageRequest);

}
