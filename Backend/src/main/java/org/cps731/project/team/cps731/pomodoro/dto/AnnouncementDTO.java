package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;

import java.text.SimpleDateFormat;

public class AnnouncementDTO {

    private Long announcementID;
    private String announcementHeader;
    private String announcementPostDate;
    private String announcementDescription;

    public AnnouncementDTO(Announcement announcement) {
        announcementID = announcement.getID();
        announcementHeader = announcement.getTitle();
        announcementPostDate = new SimpleDateFormat("MM/dd/yyyy").format(announcement.getIssueTime().getTime());
        announcementDescription = announcement.getDescription();
    }

    public AnnouncementDTO() {
    }

    public String getAnnouncementHeader() {
        return this.announcementHeader;
    }

    public String getAnnouncementDescription() {
        return this.announcementDescription;
    }

    public void setAnnouncementHeader(String announcementHeader) {
        this.announcementHeader = announcementHeader;
    }

    public void setAnnouncementDescription(String announcementDescription) {
        this.announcementDescription = announcementDescription;
    }

    public Long getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(Long announcementID) {
        this.announcementID = announcementID;
    }

    public String getAnnouncementPostDate() {
        return announcementPostDate;
    }

    public void setAnnouncementPostDate(String announcementPostDate) {
        this.announcementPostDate = announcementPostDate;
    }
}
