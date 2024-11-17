package org.cps731.project.team.cps731.pomodoro.data.task;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.converter.DurationConverter;
import org.cps731.project.team.cps731.pomodoro.data.user.Student;

import java.sql.Timestamp;
import java.time.Duration;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String name;
    private Timestamp plannedDueDate;
    @Enumerated(EnumType.ORDINAL)
    private TaskState state;
    @Convert(converter = DurationConverter.class)
    private Duration timeLogged;
    private Integer pomodorosCompleted;
    @ManyToOne
    private Student owner;
    @ManyToOne
    @JoinColumn(name = "deriving_assignment", referencedColumnName = "assignment_details_announcement")
    private Assignment derivedFrom;

}