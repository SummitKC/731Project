package org.cps731.project.team.cps731.pomodoro.controllers.professor;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professor/dashboard")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorDashBoardController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/profile")
    public ResponseEntity<String> getProfessorProfile() {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Professor professor = professorService.getProfessorById(userID);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        String uName =  professor.getUser().getEmail();
        return ResponseEntity.ok(uName);
    }

    @GetMapping("/courses")
    public ResponseEntity<Set<CourseDTO>> getProfessorCourses() {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Professor professor = professorService.getProfessorById(userID);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(professor.getCreatedCourses().stream().map(CourseDTO::new).collect(Collectors.toSet()));
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO course) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Professor professor = professorService.getProfessorById(userID);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }

        Course createdCourse = courseService.createCourse(course, userID);
        professorService.createCourseForProfessor(userID, createdCourse);

        try {
            return ResponseEntity
                    .created(new URI("/api/professor/course/" + course.getCourseCode()))
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PutMapping("/courses/{courseCode}/archive")
    public ResponseEntity<CourseDTO> archiveCourse(@PathVariable String courseCode, @RequestParam Boolean archived) {
        Course updatedCourse = courseService.archiveState(courseCode, archived);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CourseDTO(updatedCourse));
    }
}

/*     @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
} */