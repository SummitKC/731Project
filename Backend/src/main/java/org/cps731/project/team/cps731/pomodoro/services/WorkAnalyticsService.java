package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalyticsID;
import org.cps731.project.team.cps731.pomodoro.data.repo.analytics.WorkAnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WorkAnalyticsService {

    @Autowired
    private WorkAnalyticsRepo workAnalyticsRepo;

    public List<WorkAnalytics> getAllWorkAnalytics() {
        return workAnalyticsRepo.findAll();
    }

    public WorkAnalytics getWorkAnalyticsById(WorkAnalyticsID id) {
        validateWorkAnalyticsId(id);
        return workAnalyticsRepo.findById(id).orElseThrow(() -> new RuntimeException("WorkAnalytics not found"));
    }

    public WorkAnalytics createWorkAnalytics(WorkAnalytics workAnalytics) {
        validateWorkAnalytics(workAnalytics);
        return workAnalyticsRepo.save(workAnalytics);
    }

    public WorkAnalytics updateWorkAnalytics(WorkAnalyticsID id, WorkAnalytics workAnalytics) {
        validateWorkAnalyticsId(id);
        validateWorkAnalytics(workAnalytics);

        WorkAnalytics existingAnalytics = getWorkAnalyticsById(id);
        
        existingAnalytics.setPomodorosCompleted(workAnalytics.getPomodorosCompleted());
        existingAnalytics.setTimeLogged(workAnalytics.getTimeLogged());
        existingAnalytics.setStudent(workAnalytics.getStudent());
        
        return workAnalyticsRepo.save(existingAnalytics);
    }

    public WorkAnalytics incrementPomodoros(WorkAnalyticsID id) {
        validateWorkAnalyticsId(id);
        
        WorkAnalytics analytics = getWorkAnalyticsById(id);
        analytics.setPomodorosCompleted(analytics.getPomodorosCompleted() + 1);
        return workAnalyticsRepo.save(analytics);
    }

    public WorkAnalytics addTimeLogged(WorkAnalyticsID id, Integer additionalMinutes) {
        validateWorkAnalyticsId(id);
        if (additionalMinutes == null || additionalMinutes < 0) {
            throw new IllegalArgumentException("Invalid time logged value");
        }
        
        WorkAnalytics analytics = getWorkAnalyticsById(id);
        analytics.setTimeLogged(analytics.getTimeLogged() + additionalMinutes);
        return workAnalyticsRepo.save(analytics);
    }

    public void deleteWorkAnalytics(WorkAnalyticsID id) {
        validateWorkAnalyticsId(id);
        if (!workAnalyticsRepo.existsById(id)) {
            throw new RuntimeException("WorkAnalytics not found");
        }
        workAnalyticsRepo.deleteById(id);
    }

    private void validateWorkAnalytics(WorkAnalytics workAnalytics) {
        if (workAnalytics == null) {
            throw new IllegalArgumentException("WorkAnalytics cannot be null");
        }
        if (workAnalytics.getId() == null || workAnalytics.getStudent() == null || 
            workAnalytics.getPomodorosCompleted() == null || workAnalytics.getTimeLogged() == null) {
            throw new IllegalArgumentException("Invalid WorkAnalytics attributes");
        }
    }

    private void validateWorkAnalyticsId(WorkAnalyticsID id) {
        if (id == null || id.getStudentID() == null || 
            id.getStartDate() == null || id.getEndDate() == null) {
            throw new IllegalArgumentException("Invalid WorkAnalyticsID");
        }
    }
}