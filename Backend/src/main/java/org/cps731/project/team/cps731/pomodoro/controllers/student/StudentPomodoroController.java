package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.dto.PomSession;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/student/pomodoro")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentPomodoroController {

    @Autowired
    private TimeEntryService timeEntryService;

    @Autowired
    private TaskService taskService;

    private Map<Long, PomSession> activePomSession = new HashMap<>();

    @PostMapping("/start/{TaskID}")
    public ResponseEntity<Map<String, PomSession>> startPomodoro(@PathVariable Long TaskID, @RequestParam int mins) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            var task = taskService.getTaskById(TaskID);
            session = PomSession.builder()
                    .startTime(Instant.now().toEpochMilli())
                    .endTime(Instant.now().plus(mins, ChronoUnit.MINUTES).toEpochMilli())
                    .task(new TaskDTO(task))
                    .build();

            session.setStartTime(Instant.now().toEpochMilli());
            session.setTask(new TaskDTO(task));
            activePomSession.put(TaskID, session);
            
            return ResponseEntity.ok(Map.of("Start Time", session));
        }
        throw new IllegalArgumentException("Pomodoro session already exists");
    }

    @PostMapping("/pause/{TaskID}")
    public ResponseEntity<Map<String, Object>> pausePomodoro(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            throw new IllegalArgumentException("Session does not exist");
        } else if (session.isPaused()) {
            throw new IllegalArgumentException("Session is already paused");
        }
        session.setPauseTime(Instant.now().toEpochMilli());
        session.setPaused(true);
        return ResponseEntity.ok(Map.of("Pause Time", session));

    }
    
    @PostMapping("/resume/{TaskID}")
    public ResponseEntity<Map<String, Object>> resumePomodoro(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
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
        return ResponseEntity.ok(Map.of("Resume Time", session));
    }

    @PostMapping("/end/{taskID}")
    public ResponseEntity<Map<String, Object>> endPomodoro(@PathVariable Long taskID) {
        PomSession session = activePomSession.get(taskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }
        var timeLogged = session.getEndTime() - session.getStartTime() - session.getPauses()
                .stream()
                .reduce(0L, Long::sum);
        var task = taskService.getTaskById(taskID);
        TimeEntry timeEntry = TimeEntry.builder()
                .task(task)
                .startTime(new Timestamp(session.getStartTime()))
                .endTime(new Timestamp(session.getEndTime()))
                .timeLogged(timeLogged)
                .pomodoros(1)
                .build();
        timeEntryService.createTimeEntry(timeEntry);
        activePomSession.remove(taskID);
        return ResponseEntity.ok(Map.of("End Time", session));
    }


    @GetMapping("/break/{TaskID}") //Work in progress
    public ResponseEntity<Integer> pomoBreak(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }

        //if (Timestamp.from(Instant.now()).equals(session.)) {
        
            return ResponseEntity.ok(5);
    }

}






