package org.cps731.project.team.cps731.pomodoro.data.model.assignment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Assignment {

    @Id
    private Long ID;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assignment_details_announcement", referencedColumnName = "ID")
    private Announcement announcement;
    private Timestamp dueDate;
    @OneToMany(mappedBy = "derivedFrom")
    private Set<Task> derivingTasks;

}
