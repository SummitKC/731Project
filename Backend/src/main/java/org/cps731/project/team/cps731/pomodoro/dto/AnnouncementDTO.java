package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;

import java.util.Date;

public class AnnouncementDTO {

    private String title;
    private Date issueDate;
    private String description;

    public AnnouncementDTO(String title, Date issueDate, String description) {
        this.title = title;
        this.issueDate = issueDate;
        this.description = description;
    }

    public AnnouncementDTO(Announcement announcement) {
        title = announcement.getTitle();
        issueDate = new Date(announcement.getIssueTime().getTime());
        description = announcement.getDescription();
    }

    public AnnouncementDTO() {
    }

    public String getTitle() {
        return this.title;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
