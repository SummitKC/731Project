package org.cps731.project.team.cps731.pomodoro.data.controllers;

import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;

import java.time.Instant;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.services.TimeEntryService;
import org.cps731.project.team.cps731.pomodoro.data.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/pomodoro")
public class StudentPomodoroController {

    @Autowired
    private TimeEntryService timeEntryService;

    @Autowired
    private TaskService taskService;

    private Map<Long, PomSession> activePomSession = new HashMap<>();

    private static class PomSession {
        private Timestamp startTime;
        private Timestamp endTime;
        private Timestamp pauseTime;
        private Timestamp resumeTime;
        private boolean isPaused;
        private Task task;
    }
    
    @PostMapping("/start{TaskID}")
    public ResponseEntity<Map<String, PomSession>> startPomodoro(@PathVariable Long TaskID, int mins) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }
            Task task = taskService.getTaskById(TaskID);

            session.startTime = Timestamp.from(Instant.now());
            Instant endTimeInstant = session.startTime.toInstant().plusSeconds(mins * 60);
            session.endTime = Timestamp.from(endTimeInstant);
            session.task = task;
            activePomSession.put(TaskID, session);
            
            return ResponseEntity.ok(Map.of("Start Time", session));
        
    }

    @PostMapping("/pause{TaskID}")
    public ResponseEntity<Map<String, Object>> pausePomodoro(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null || !session.isPaused) {
            return ResponseEntity.badRequest().build();
        }
            session.pauseTime = Timestamp.from(Instant.now());
            session.isPaused = true;
            return ResponseEntity.ok(Map.of("Pause Time", session));

    }
    
    @PostMapping("/resume{TaskID}")
    public ResponseEntity<Map<String, Object>> resumePomodoro(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null || session.isPaused) {
            return ResponseEntity.badRequest().build();
        }
            session.resumeTime = Timestamp.from(Instant.now());
            session.isPaused = false;
            long pauseDuration = session.resumeTime.toInstant().toEpochMilli() - session.pauseTime.toInstant().toEpochMilli();
            session.endTime = Timestamp.from(session.endTime.toInstant().plusMillis(pauseDuration));
            return ResponseEntity.ok(Map.of("Resume Time", session));
    
    }

    @PostMapping("/end{TaskID}")
    public ResponseEntity<Map<String, Object>> endPomodoro(@PathVariable Long TaskID) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }
            TimeEntry timeEntry = new TimeEntry(session.task, session.startTime, session.endTime, 1);
            timeEntryService.createTimeEntry(timeEntry);
            activePomSession.remove(TaskID);
            return ResponseEntity.ok(Map.of("End Time", session));
    }


    @GetMapping("/break{TaskID}") //Work in progress
    public ResponseEntity<Integer> pomoBreak(@PathVariable Long TaskID, int brakeNumber) {
        PomSession session = activePomSession.get(TaskID);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }

        //if (Timestamp.from(Instant.now()).equals(session.)) {
            return ResponseEntity.ok(brakeNumber);
    }

}






