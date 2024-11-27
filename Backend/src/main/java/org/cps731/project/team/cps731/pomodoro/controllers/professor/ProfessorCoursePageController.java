package org.cps731.project.team.cps731.pomodoro.controllers.professor;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professor/course")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorCoursePageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AssignmentService assignmentService;

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

        List<Announcement> announcements = announcementService.getAnnouncementsByCourse(
                courseCode, announcementPage, pageSize);
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(
                courseCode, assignmentPage, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("course", course);
        response.put("announcements", announcements);
        response.put("assignments", assignments);
        response.put("isArchived", course.getArchived());
        response.put("professor", course.getCreatedBy().getUser().getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseCode}/announcement") //Would it be better to get coursed by the CreatedCourses method?
    public ResponseEntity<Course> createAnnouncement(@PathVariable String courseCode, @RequestBody Announcement announcement) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        announcement.setCourse(course);
        Course updatedCourse = courseService.addAnnouncementToCourse(courseCode, announcement);
        
        return ResponseEntity.ok(updatedCourse);
    }

    @PostMapping("/{courseCode}/assignment")
    public ResponseEntity<Assignment> createAssignment(@PathVariable String courseCode, @RequestBody Assignment assignment) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        assignment.getAnnouncement().setCourse(course);
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        
        return ResponseEntity.ok(createdAssignment);
    }

    @PutMapping("/{courseCode}/archive")
    public ResponseEntity<Course> archiveCourse(@PathVariable String courseCode, @RequestParam Boolean archived) {
        
        Course updatedCourse = courseService.archiveState(courseCode, archived);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(updatedCourse);
    }
}