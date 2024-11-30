package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.FullCourseInfoDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student/course")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentCoursePageController {

    private final CourseService courseService;
    private final AnnouncementService announcementService;
    private final AssignmentService assignmentService;
    private final StudentService studentService;

    @Autowired
    public StudentCoursePageController(CourseService courseService, AnnouncementService announcementService, StudentService studentService, AssignmentService assignmentService) {
        this.courseService = courseService;
        this.announcementService = announcementService;
        this.studentService = studentService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<FullCourseInfoDTO> getCourseDetails(
            @PathVariable String courseCode,
            @RequestParam(defaultValue = "0") int announcementPage,
            @RequestParam(defaultValue = "0") int assignmentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Get assignments with pagination
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(
                courseCode, assignmentPage, pageSize);

        return ResponseEntity.ok(new FullCourseInfoDTO(course, assignments));
    }

    @GetMapping("/{courseCode}/announcements")
    public ResponseEntity<List<AnnouncementDTO>> getCourseAnnouncements(
            @PathVariable String courseCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(
                announcementService.getAnnouncementsByCourse(courseCode, page, size)
                        .stream()
                        .map(AnnouncementDTO::new)
                        .toList()
        );
    }

    @GetMapping("/{courseCode}/assignments")
    public ResponseEntity<List<AssignmentDTO>> getCourseAssignments(
            @PathVariable String courseCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(
            assignmentService.getAssignmentsByCourse(courseCode, page, size)
                    .stream()
                    .map(AssignmentDTO::new)
                    .toList()
        );
    }

    @DeleteMapping("/courses/{courseCode}")
    public ResponseEntity<Void> leaveCourse(@PathVariable String courseCode) {
        var studentId = SecurityUtil.getAuthenticatedUserID();

        if (studentService.removeStudentFromCourse(studentId, courseCode)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}