package org.cps731.project.team.cps731.pomodoro.controllers.professor;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/professor/dashboard")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorDashBoardController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<String> getProfessorProfile(@PathVariable Long id) {
        Professor professor = professorService.getProfessorById(id);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        String uName =  professor.getUser().getEmail();
        return ResponseEntity.ok(uName);
    }

    @GetMapping("/courses/{professorId}")
    public ResponseEntity<Set<Course>> getProfessorCourses(@PathVariable Long professorId) {
        Professor professor = professorService.getProfessorById(professorId);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(professor.getCreatedCourses());
    }

    @PostMapping("/courses/{professorId}")
    public ResponseEntity<Course> createCourse(@PathVariable Long professorId, @RequestBody Course course) {
        try {
            Professor professor = professorService.getProfessorById(professorId);
            if (professor == null) {
                return ResponseEntity.notFound().build();
            }
            
            course.setCreatedBy(professor);
            Course createdCourse = courseService.createCourse(course);
            professorService.createCourseForProfessor(professorId, createdCourse);
            
            return ResponseEntity.ok(createdCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/courses/{courseId}/archive")
    public ResponseEntity<Course> archiveCourse(@PathVariable CourseID courseId, @RequestParam Boolean archived) {
        Course updatedCourse = courseService.archiveState(courseId, archived);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }
}

/*     @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
} */