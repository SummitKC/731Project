package org.cps731.project.team.cps731.pomodoro.data.repo.task;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    Set<Task> findAllByOwnerId(Long id);
    Set<Task> findAllByOwnerIdAndStateIsIn(Long id, Collection<TaskState> states);
    Set<Task> findAllByOwnerIdAndStateIsInAndDerivedFrom_Announcement_IssueTimeAfter(Long ownerId, Collection<TaskState> state, Timestamp derivedFromAnnouncementIssueTime);

}
