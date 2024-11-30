package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.dto.PomSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PomodoroService {

    private final Map<Long, PomSession> activePomSessions = new HashMap<>();

    public void addSession(Long taskID, PomSession session) {
        activePomSessions.put(taskID, session);
    }

    public PomSession getSession(Long taskID) {
        return activePomSessions.get(taskID);
    }

    public void removeSession(Long taskID) {
        activePomSessions.remove(taskID);
    }

}
