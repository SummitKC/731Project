package org.cps731.project.team.cps731.pomodoro.data.repo.assignment;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepo extends PagingAndSortingRepository<Assignment, Long> {

    List<Assignment> findAllByAnnouncement_Course_CourseID(CourseID courseID, PageRequest pageRequest);

}
