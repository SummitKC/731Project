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
    @ManyToOne
    private Student owner;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="referenced_assignment_title", referencedColumnName = "title"),
            @JoinColumn(name="course_name", referencedColumnName = "course_name"),
            @JoinColumn(name="school_term", referencedColumnName = "school_term"),
            @JoinColumn(name="school_year", referencedColumnName = "school_year"),
            @JoinColumn(name="referenced_assignment_issue_time", referencedColumnName = "issue_time"),
    })
    private Assignment derivedFrom;

}
