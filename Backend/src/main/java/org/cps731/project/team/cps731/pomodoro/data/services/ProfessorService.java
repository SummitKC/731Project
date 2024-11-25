package org.cps731.project.team.cps731.pomodoro.data.services;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepo professorRepo;

    public List<Professor> getAllProfessors() {
        return professorRepo.findAll();
    }

    public Professor getProfessorById(Long id) {
        return professorRepo.findById(id).orElse(null);
    }

    public Professor createProfessor(Professor professor) {
        return professorRepo.save(professor);
    }

    public Professor updateProfessor(Long id, Professor professor) {
        Professor existingProfessor = professorRepo.findById(id).orElse(null);
        if (existingProfessor != null) {
            existingProfessor.getUser().setEmail(professor.getUser().getEmail());
            existingProfessor.getUser().setPassword(professor.getUser().getPassword());
            return professorRepo.save(existingProfessor);
        }
        return null;
    }

    public Professor createCourseForProfessor(Long professorId, Course course) {
    if (professorId == null || course == null) {
        throw new IllegalArgumentException("Professor ID and course cannot be null");
    }
    Professor professor = professorRepo.findById(professorId).orElseThrow(() -> new RuntimeException("Professor not found with id: " + professorId));
    Set<Course> courses = professor.getCreatedCourses();
    if (courses == null) {
        courses = professor.getCreatedCourses();
    }

    courses.add(course);
    professor.setCreatedCourses(courses);

    return professorRepo.save(professor);
    } 

    

    public void deleteProfessor(Long id) {
        professorRepo.deleteById(id);
    }
}