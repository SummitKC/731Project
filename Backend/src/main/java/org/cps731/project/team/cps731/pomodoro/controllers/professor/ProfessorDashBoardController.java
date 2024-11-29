package org.cps731.project.team.cps731.pomodoro.controllers.professor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.professor.ProfessorProfileDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professor/dashboard")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorDashBoardController {

    private ProfessorService professorService;
    private CourseService courseService;

    @Autowired
    public ProfessorDashBoardController(ProfessorService professorService, CourseService courseService) {
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfessorProfileDTO> getProfessorProfile() 
    {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Professor professor = professorService.getProfessorById(userID);
        if (professor == null) {
          return ResponseEntity.notFound().build();
        }
        ProfessorProfileDTO response = new ProfessorProfileDTO(professor);
        return ResponseEntity.ok(response);
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