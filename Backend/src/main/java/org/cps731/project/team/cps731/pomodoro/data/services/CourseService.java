package org.cps731.project.team.cps731.pomodoro.data.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
//import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.repo.course.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Course getCourseById(CourseID id) {
        return courseRepo.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    public Course updateCourse(CourseID id, Course course) {
        Course existingCourse = courseRepo.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setCourseID(course.getCourseID());
            //existingCourse.setArchived(course.getArchived());
            //existingCourse.setCreatedBy(course.getCreatedBy());
            //existingCourse.setTakenBy(course.getTakenBy());
            existingCourse.setAnnouncements(course.getAnnouncements());
            return courseRepo.save(existingCourse);
        }
        return null;
    }

    public Course archiveState(CourseID id, Boolean state) {
        Course existingCourse = courseRepo.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setArchived(state);
            return courseRepo.save(existingCourse);
        }
        return null;
    }

    public Course addAnnouncementToCourse(CourseID id, Announcement announcement) {
        Course existingCourse = courseRepo.findById(id).orElse(null);
        if (existingCourse != null) {
            Set<Announcement> announcements = existingCourse.getAnnouncements();
            announcements.add(announcement);
            existingCourse.setAnnouncements(announcements);
            return courseRepo.save(existingCourse);
        }
        return null;
    }

/*     public Course addAssignmentToCourse(CourseID id, Assignment assignment) {
        Course existingCourse = courseRepo.findById(id).orElse(null);
        if (existingCourse != null) {
            Set<Announcement> announcements = existingCourse.getAnnouncements();
            announcements.add(announcement);
            existingCourse.setAnnouncements(announcements);
            return courseRepo.save(existingCourse);
        }
        return null;
    }
 */
    public void deleteCourse(CourseID id) {
        courseRepo.deleteById(id);
    }
}