package org.cps731.project.team.cps731.pomodoro.controllers.professor;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.CreateAssignmentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDetailsDTO;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professor/course")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorCoursePageController {

    private final CourseService courseService;
    private final AnnouncementService announcementService;
    private final AssignmentService assignmentService;

    @Autowired
    public ProfessorCoursePageController(CourseService courseService, AssignmentService assignmentService, AnnouncementService announcementService) {
        this.courseService = courseService;
        this.assignmentService = assignmentService;
        this.announcementService = announcementService;
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseDetailsDTO> getCourseDetails(
            @PathVariable String courseCode,
            @RequestParam(defaultValue = "0") int announcementPage,
            @RequestParam(defaultValue = "0") int assignmentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new CourseDetailsDTO(course,
                assignmentService.getAssignmentsByCourse(courseCode, assignmentPage, pageSize)
                        .stream()
                        .map(AssignmentDTO::new)
                        .collect(Collectors.toSet())
        ));
    }

    @PostMapping("/{courseCode}/announcement") //Would it be better to get coursed by the CreatedCourses method?
    public ResponseEntity<Void> createAnnouncement(@PathVariable String courseCode, @RequestBody AnnouncementDTO announcement) {
        
        var course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            var newAnnouncement = new Announcement(announcement, course);
            courseService.addAnnouncementToCourse(courseCode, newAnnouncement);
            return ResponseEntity.created(new URI("/api/professor/course/" + courseCode)).build();
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{courseCode}/assignment")
    public ResponseEntity<AssignmentDTO> createAssignment(@PathVariable String courseCode, @RequestBody CreateAssignmentRequestDTO requestBody) {
        
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        Assignment createdAssignment = assignmentService.createAssignment(requestBody, course);
        
        return ResponseEntity.ok(new AssignmentDTO(createdAssignment));
    }

    @PutMapping("/assignment/{assignmentID}")
    public ResponseEntity<AssignmentDTO> updateAssignment(@PathVariable Long assignmentID, CreateAssignmentRequestDTO requestDTO) {
        return ResponseEntity.ok(
                new AssignmentDTO(assignmentService.updateAssignment(assignmentID, requestDTO))
        );
    }

    @PutMapping("/{courseCode}/archive")
    public ResponseEntity<CourseDTO> archiveCourse(@PathVariable String courseCode, @RequestParam Boolean archived) {
        
        Course updatedCourse = courseService.archiveState(courseCode, archived);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new CourseDTO(updatedCourse));
    }
}