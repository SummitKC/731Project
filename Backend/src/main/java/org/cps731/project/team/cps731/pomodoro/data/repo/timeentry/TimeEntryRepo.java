package org.cps731.project.team.cps731.pomodoro.data.repo.timeentry;

import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Set;

@Repository
public interface TimeEntryRepo extends JpaRepository<TimeEntry, Long> {

    /**
     * Finds all time entries of a student that started after a specific time stamp.
     * @param studentID The ID of the student.
     * @param after The time stamp the time entries must be after.
     * @return A set of time entries matching this criteria.
     */
    Set<TimeEntry> findAllByTask_OwnerIDAndStartTimeAfter(Long studentID, Timestamp after);

}
