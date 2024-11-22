package org.cps731.project.team.cps731.pomodoro.Controllers;
import org.cps731.project.team.cps731.pomodoro.Services.WorkAnalyticsService;
import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.repo.WorkAnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class WorkAnalyticsController  {
    
    @Autowired
    private  WorkAnalyticsService workAnalytics;

    @GetMapping
    public ResponseEntity<List<WorkAnalytics>> getAllWorkAnalytics() {
        return ResponseEntity.ok(workAnalytics.getAllWorkAnalytics());
    }

    @GetMapping("/placeholder")
    public ResponseEntity<List<WorkAnalytics>> getWorkAnalyticsByID() {
        return ResponseEntity.ok(workAnalytics.getWorkAnalyticsByID());
    }

    public ResponseEntity<List<WorkAnalytics>> getAllWorkAnalytics() {
        return ResponseEntity.ok(workAnalytics.getAllWorkAnalytics());
    }

    public ResponseEntity<List<WorkAnalytics>> getAllWorkAnalytics() {
        return ResponseEntity.ok(workAnalytics.getAllWorkAnalytics());
    }
}
