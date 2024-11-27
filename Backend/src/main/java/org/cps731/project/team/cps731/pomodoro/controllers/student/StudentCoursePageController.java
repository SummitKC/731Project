package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student/course")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentCoursePageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/{courseCode}")
    public ResponseEntity<Map<String, Object>> getCourseDetails(
            @PathVariable String courseCode,
            @RequestParam(defaultValue = "0") int announcementPage,
            @RequestParam(defaultValue = "0") int assignmentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Get announcements with pagination
        List<Announcement> announcements = announcementService.getAnnouncementsByCourse(
                courseCode, announcementPage, pageSize);

        // Get assignments with pagination
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(
                courseCode, assignmentPage, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("course", new CourseDTO(course));
        response.put("announcements", announcements.stream().map(AnnouncementDTO::new).collect(Collectors.toSet()));
        response.put("assignments", assignments.stream().map(AssignmentDTO::new).collect(Collectors.toSet()));
        response.put("professor", course.getCreatedBy().getUser().getEmail());
        response.put("isArchived", course.getArchived());

        return ResponseEntity.ok(response);
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