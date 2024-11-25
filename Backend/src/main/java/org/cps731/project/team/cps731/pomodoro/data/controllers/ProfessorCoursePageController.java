package org.cps731.project.team.cps731.pomodoro.data.controllers;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.data.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.data.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professor/course")
public class ProfessorCoursePageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AssignmentService assignmentService;

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

        List<Announcement> announcements = announcementService.getAnnouncementsByCourse(
            courseId, announcementPage, pageSize);
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(
            courseId, assignmentPage, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("course", course);
        response.put("announcements", announcements);
        response.put("assignments", assignments);
        response.put("isArchived", course.getArchived());
        response.put("professor", course.getCreatedBy().getUser().getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/announcement") //Would it be better to get coursed by the CreatedCourses method? 
    public ResponseEntity<Course> createAnnouncement(@PathVariable CourseID courseId, @RequestBody Announcement announcement) {
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        announcement.setCourse(course);
        Course updatedCourse = courseService.addAnnouncementToCourse(courseId, announcement);
        
        return ResponseEntity.ok(updatedCourse);
    }

    @PostMapping("/{courseId}/assignment")
    public ResponseEntity<Assignment> createAssignment(@PathVariable CourseID courseId, @RequestBody Assignment assignment) {
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        assignment.getAnnouncement().setCourse(course);
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        
        return ResponseEntity.ok(createdAssignment);
    }

    @PutMapping("/{courseId}/archive")
    public ResponseEntity<Course> archiveCourse(@PathVariable CourseID courseId, @RequestParam Boolean archived) {
        
        Course updatedCourse = courseService.archiveState(courseId, archived);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(updatedCourse);
    }
}