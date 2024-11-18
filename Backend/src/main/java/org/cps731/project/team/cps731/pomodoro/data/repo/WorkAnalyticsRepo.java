package org.cps731.project.team.cps731.pomodoro.data.repo;

import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalyticsID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkAnalyticsRepo extends JpaRepository<WorkAnalytics, WorkAnalyticsID> {
}
