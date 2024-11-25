package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.repo.task.TaskRepo;
import org.cps731.project.team.cps731.pomodoro.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private TaskRepo taskRepo;

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepo.findById(id).orElse(null);
    }

    public Set<Task> getTaskByState(Long ownerId, TaskState state) {
        return taskRepo.findAllByOwnerIdAndStateIsIn(ownerId, Set.of(state));
    }

    public Set<Task> getTaskByStateAndIssueTime(Long ownerId, TaskState state, Timestamp issueTime) {
        return taskRepo.findAllByOwnerIdAndStateIsInAndDerivedFrom_Announcement_IssueTimeAfter(ownerId, Set.of(state), issueTime);
    }
    public Task createTask(Task task) {
        return taskRepo.save(task);
    }

    public Task updateTask(Long id, TaskDTO task) {
        Task existingTask = taskRepo.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.updateFromTaskDTO(task);
            return taskRepo.save(existingTask);
        }
        return null;
    }

    public Task changeTaskState(Long id, TaskState state) {
        Task existingTask = taskRepo.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setState(state);
            return taskRepo.save(existingTask);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }
}