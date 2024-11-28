package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.data.repo.timeentry.TimeEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class TimeEntryService {

    private final TimeEntryRepo timeEntryRepo;

    @Autowired
    public TimeEntryService(TimeEntryRepo timeEntryRepo) {
        this.timeEntryRepo = timeEntryRepo;
    }

    public List<TimeEntry> getAllTimeEntries() {
        return timeEntryRepo.findAll();
    }

    public TimeEntry getTimeEntryById(Long id) {
        return timeEntryRepo.findById(id).orElse(null);
    }

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        return timeEntryRepo.save(timeEntry);
    }

    public TimeEntry updateTimeEntry(Long id, TimeEntry timeEntry) {
        TimeEntry existingTimeEntry = timeEntryRepo.findById(id).orElse(null);
        if (existingTimeEntry != null) {
            existingTimeEntry.setTask(timeEntry.getTask());
            existingTimeEntry.setStartTime(timeEntry.getStartTime());
            existingTimeEntry.setEndTime(timeEntry.getEndTime());
            existingTimeEntry.setPomodoros(timeEntry.getPomodoros());
            return timeEntryRepo.save(existingTimeEntry);
        }
        return null;
    }

    public void deleteTimeEntry(Long id) {
        timeEntryRepo.deleteById(id);
    }

    public Set<TimeEntry> findAllByTaskOwnerIdAndStartTimeAfter(Long studentID, Timestamp after) {
        return timeEntryRepo.findAllByTask_OwnerIdAndStartTimeAfter(studentID, after);
    }
}