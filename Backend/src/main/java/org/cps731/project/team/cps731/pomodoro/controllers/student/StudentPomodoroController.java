package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.dto.PomSession;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.PomodoroService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/student/pomodoro")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentPomodoroController {

    private final TimeEntryService timeEntryService;
    private final TaskService taskService;
    private final PomodoroService pomodoroService;
    private final StudentService studentService;

    @Autowired
    public StudentPomodoroController(TimeEntryService timeEntryService, TaskService taskService, PomodoroService pomodoroService, StudentService studentService) {
        this.timeEntryService = timeEntryService;
        this.taskService = taskService;
        this.pomodoroService = pomodoroService;
        this.studentService = studentService;
    }

    @PostMapping("/start/{taskID}")
    public ResponseEntity<PomSession> startPomodoro(@PathVariable Long taskID, @RequestParam int mins) {
        var task = taskService.getTaskById(taskID);
        if (userIsNotTaskOwner(task)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        PomSession session = pomodoroService.getSession(taskID);
        if (session == null) {
            session = PomSession.builder()
                    .startTime(Instant.now().toEpochMilli())
                    .endTime(Instant.now().plus(mins, ChronoUnit.MINUTES).toEpochMilli())
                    .task(new TaskDTO(task))
                    .build();

            session.setStartTime(Instant.now().toEpochMilli());
            session.setTask(new TaskDTO(task));
            pomodoroService.addSession(taskID, session);
            
            return ResponseEntity.ok(session);
        }
        throw new IllegalArgumentException("Pomodoro session already exists");
    }

    @PostMapping("/pause/{taskID}")
    public ResponseEntity<PomSession> pausePomodoro(@PathVariable Long taskID) {
        var task = taskService.getTaskById(taskID);
        if (userIsNotTaskOwner(task)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        PomSession session = pomodoroService.getSession(taskID);
        if (session == null) {
            throw new IllegalArgumentException("Session does not exist");
        } else if (session.isPaused()) {
            throw new IllegalArgumentException("Session is already paused");
        }
        session.setPauseTime(Instant.now().toEpochMilli());
        session.setPaused(true);
        return ResponseEntity.ok(session);

    }
    
    @PostMapping("/resume/{taskID}")
    public ResponseEntity<PomSession> resumePomodoro(@PathVariable Long taskID) {
        var task = taskService.getTaskById(taskID);
        if (userIsNotTaskOwner(task)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        PomSession session = pomodoroService.getSession(taskID);
        if (session == null) {
            throw new IllegalArgumentException("Session does not exist");
        } else if (!session.isPaused()) {
            throw new IllegalArgumentException("Session is not paused");
        }
        session.setResumeTime(Instant.now().toEpochMilli());
        session.setPaused(false);
        long pauseDuration = session.getResumeTime() - session.getPauseTime();
        session.setEndTime(session.getEndTime() + pauseDuration);
        session.addPause(pauseDuration);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/end/{taskID}")
    public ResponseEntity<PomSession> endPomodoro(@PathVariable Long taskID) {
        var task = taskService.getTaskById(taskID);
        if (userIsNotTaskOwner(task)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        PomSession session = pomodoroService.getSession(taskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }
        var timeLogged = session.getEndTime() - session.getStartTime() - session.getPauses()
                .stream()
                .reduce(0L, Long::sum);
        TimeEntry timeEntry = TimeEntry.builder()
                .task(task)
                .startTime(new Timestamp(session.getStartTime()))
                .endTime(new Timestamp(session.getEndTime()))
                .timeLogged(timeLogged)
                .pomodoros(1)
                .build();
        timeEntryService.createTimeEntry(timeEntry);
        pomodoroService.removeSession(taskID);
        return ResponseEntity.ok(session);
    }


    @GetMapping("/break/{taskID}") //Work in progress
    public ResponseEntity<Integer> pomoBreak(@PathVariable Long taskID) {
        PomSession session = pomodoroService.getSession(taskID);
        var task = taskService.getTaskById(taskID);
        if (userIsNotTaskOwner(task)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }

        //if (Timestamp.from(Instant.now()).equals(session.)) {
        
            return ResponseEntity.ok(5);
    }

    private boolean userIsNotTaskOwner(Task task) {
        var user = studentService.getStudentById(SecurityUtil.getAuthenticatedUserID());
        return !task.getOwner().getID().equals(user.getID());
    }

}






