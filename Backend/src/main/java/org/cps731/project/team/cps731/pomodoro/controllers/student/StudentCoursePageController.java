package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/student/course")
@Secured("STUDENT")
public class StudentCoursePageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseDetails(
            @PathVariable CourseID courseId,
            @RequestParam(defaultValue = "0") int announcementPage,
            @RequestParam(defaultValue = "0") int assignmentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Get announcements with pagination
        List<Announcement> announcements = announcementService.getAnnouncementsByCourse(
            courseId, announcementPage, pageSize);

        // Get assignments with pagination
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(
            courseId, assignmentPage, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("course", course);
        response.put("announcements", announcements);
        response.put("assignments", assignments);
        response.put("professor", course.getCreatedBy().getUser().getEmail());
        response.put("isArchived", course.getArchived());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/announcements")
    public ResponseEntity<List<Announcement>> getCourseAnnouncements(
            @PathVariable CourseID courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(
            announcementService.getAnnouncementsByCourse(courseId, page, size));
    }

    @GetMapping("/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> getCourseAssignments(
            @PathVariable CourseID courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(
            assignmentService.getAssignmentsByCourse(courseId, page, size));
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Student> leaveCourse(@PathVariable CourseID courseId) {
        try {
            var studentId = SecurityUtil.getAuthenticatedUserID();
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            Set<Course> courses = student.getCourses();
            courses.removeIf(c -> c.getCourseID().equals(courseId));
            student.setCourses(courses);
            
            Student updatedStudent = studentService.updateStudent(studentId, student);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}