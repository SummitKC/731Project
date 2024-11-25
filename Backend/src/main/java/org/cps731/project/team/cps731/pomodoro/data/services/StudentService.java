package org.cps731.project.team.cps731.pomodoro.data.services;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return studentRepo.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepo.findById(id).orElse(null);
        if (existingStudent != null) {
            existingStudent.getUser().setEmail(student.getUser().getEmail());
            existingStudent.getUser().setPassword(student.getUser().getPassword());
            return studentRepo.save(existingStudent);
        }
        return null;
    }

    public Student addCourseToStudent(Long studentId, Course course) {
    if (studentId == null || course == null) {
        throw new IllegalArgumentException("Student ID and course cannot be null");
    }

    Student student = studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    
    Set<Course> courses = student.getCourses();
    if (courses == null) {
        courses = new HashSet<>();
    }
    
    courses.add(course);
    student.setCourses(courses);
    
    return studentRepo.save(student);
    }

    public Student addTaskToStudent(Long studentId, Task task) {
        if (studentId == null || task == null) {
            throw new IllegalArgumentException("Student ID and task cannot be null");
        }
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Set<Task> tasks = student.getTasks();
        if (tasks == null) {
            tasks = new HashSet<>();
        }

        task.setOwner(student);
        tasks.add(task);
        student.setTasks(tasks);
        
        return studentRepo.save(student);
    }
    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }
}