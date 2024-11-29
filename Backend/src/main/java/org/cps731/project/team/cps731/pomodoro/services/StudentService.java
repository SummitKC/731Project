package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.repo.course.CourseRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.dto.student.StudentProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    public StudentService(StudentRepo studentRepo, CourseRepo courseRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getStudentByEmail(String email) {
        return studentRepo.findAllByUser_Email(email);
    }

    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return studentRepo.save(student);
    }

    public StudentProfileDTO getStudentProfile(Long id) {
        return new StudentProfileDTO(studentRepo.findById(id).orElseThrow());
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

    public Student addCourseToStudent(Long studentId, String courseCode) {
        if (studentId == null || courseCode == null) {
            throw new IllegalArgumentException("Student ID and course cannot be null");
        }

        var course = courseRepo.findById(courseCode).orElseThrow(() -> new NoSuchElementException("Course not found"));
        if (course.getArchived()) {
            throw new IllegalArgumentException("Course is archived");
        }
        var student = studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        if (course.getTakenBy().contains(student)) {
            throw new IllegalArgumentException("Student already is enrolled in this course");
        }
        Set<Course> courses = student.getCourses();
        if (courses == null) {
            courses = new HashSet<>();
        }

        courses.add(course);
        student.setCourses(courses);
        course.getTakenBy().add(student);
        courseRepo.save(course);
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

    public boolean removeStudentFromCourse(Long studentID, String courseCode) {
        var course = courseRepo.findById(courseCode).orElse(null);
        var student = studentRepo.findById(studentID).orElseThrow();

        if (course == null) {
            return false;
        }

        if (!course.getTakenBy().contains(student)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        course.setTakenBy(course.getTakenBy().stream().filter(s -> !s.equals(student)).collect(Collectors.toSet()));
        student.setCourses(student.getCourses().stream().filter(c -> !c.equals(course)).collect(Collectors.toSet()));

        courseRepo.save(course);
        studentRepo.save(student);

        return true;
    }

    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }
}