package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
//import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.course.CourseRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ProfessorRepo professorRepo;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserRepo userRepo;

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Course getCourseById(String courseCode) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        var userType = userRepo.findById(userID).orElseThrow().getUserType();
        if (userType.equals(UserType.STUDENT)) {
            var student = studentService.getStudentById(userID);
            if (student.getCourses().stream().noneMatch(c -> c.getCourseCode().equals(courseCode))) {
                throw new AuthorizationDeniedException(
                        "Student is not enrolled in this course",
                        new AuthorizationDecision(false)
                );
            }
        } else if (userType.equals(UserType.PROFESSOR)) {
            var professor = professorRepo.findById(userID).orElseThrow();
            if (professor.getCreatedCourses().stream().noneMatch(c -> c.getCourseCode().equals(courseCode))) {
                throw new AuthorizationDeniedException(
                        "Professor does not own in this course",
                        new AuthorizationDecision(false)
                );
            }
        }

        return courseRepo.findById(courseCode).orElse(null);
    }

    public Course createCourse(CourseDTO course, Long professorID) {
        var professor = professorRepo.findByUser_Id(professorID);
        var newCourse = Course.builder()
                .courseCode(course.getCourseCode())
                .name(course.getName())
                .createdBy(professor)
                .archived(false)
                .term(course.getTerm())
                .year(course.getYear())
                .build();
        return courseRepo.save(newCourse);
    }

    public Course updateCourse(String courseCode, Course course) {
        Course existingCourse = courseRepo.findById(courseCode).orElse(null);
        if (existingCourse != null) {
            existingCourse.setCourseCode(course.getCourseCode());
            existingCourse.setArchived(course.getArchived());
            existingCourse.setCreatedBy(course.getCreatedBy());
            existingCourse.setTakenBy(course.getTakenBy());
            existingCourse.setAnnouncements(course.getAnnouncements());
            return courseRepo.save(existingCourse);
        }
        return null;
    }

    public Course archiveState(String courseCode, Boolean state) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Course existingCourse = courseRepo.findById(courseCode).orElseThrow();
        if (!existingCourse.getCreatedBy().getId().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Cannot archive a course you do not own",
                    new AuthorizationDecision(false)
                    );
        }
        existingCourse.setArchived(state);
        return courseRepo.save(existingCourse);
    }

    public Course addAnnouncementToCourse(String courseCode, Announcement announcement) {
        Course existingCourse = courseRepo.findById(courseCode).orElse(null);
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
    public void deleteCourse(String courseCode) {
        courseRepo.deleteById(courseCode);
    }
}