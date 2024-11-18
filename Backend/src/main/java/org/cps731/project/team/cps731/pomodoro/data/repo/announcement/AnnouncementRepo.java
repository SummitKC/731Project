package org.cps731.project.team.cps731.pomodoro.data.repo.announcement;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepo extends PagingAndSortingRepository<Announcement, Long> {

    List<Announcement> findAllByCourse_CourseID(CourseID courseID, Pageable pageable);

}
