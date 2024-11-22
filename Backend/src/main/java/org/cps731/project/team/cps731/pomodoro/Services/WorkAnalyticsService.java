package org.cps731.project.team.cps731.pomodoro.Services;

import java.util.List;

import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.repo.WorkAnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkAnalyticsService {

    @Autowired
    private WorkAnalyticsRepo workAnalyticsRepo;

     public List<WorkAnalytics> getAllWorkAnalytics() {
        return workAnalyticsRepo.findAll();
    }
    
}
