package org.cps731.project.team.cps731.pomodoro.dto.announcement;

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AnnouncementDTO)) return false;
        final AnnouncementDTO other = (AnnouncementDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$announcementID = this.getAnnouncementID();
        final Object other$announcementID = other.getAnnouncementID();
        if (this$announcementID == null ? other$announcementID != null : !this$announcementID.equals(other$announcementID))
            return false;
        final Object this$announcementHeader = this.getAnnouncementHeader();
        final Object other$announcementHeader = other.getAnnouncementHeader();
        if (this$announcementHeader == null ? other$announcementHeader != null : !this$announcementHeader.equals(other$announcementHeader))
            return false;
        final Object this$announcementPostDate = this.getAnnouncementPostDate();
        final Object other$announcementPostDate = other.getAnnouncementPostDate();
        if (this$announcementPostDate == null ? other$announcementPostDate != null : !this$announcementPostDate.equals(other$announcementPostDate))
            return false;
        final Object this$announcementDescription = this.getAnnouncementDescription();
        final Object other$announcementDescription = other.getAnnouncementDescription();
        if (this$announcementDescription == null ? other$announcementDescription != null : !this$announcementDescription.equals(other$announcementDescription))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AnnouncementDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $announcementID = this.getAnnouncementID();
        result = result * PRIME + ($announcementID == null ? 43 : $announcementID.hashCode());
        final Object $announcementHeader = this.getAnnouncementHeader();
        result = result * PRIME + ($announcementHeader == null ? 43 : $announcementHeader.hashCode());
        final Object $announcementPostDate = this.getAnnouncementPostDate();
        result = result * PRIME + ($announcementPostDate == null ? 43 : $announcementPostDate.hashCode());
        final Object $announcementDescription = this.getAnnouncementDescription();
        result = result * PRIME + ($announcementDescription == null ? 43 : $announcementDescription.hashCode());
        return result;
    }

    public String toString() {
        return "AnnouncementDTO(announcementID=" + this.getAnnouncementID() + ", announcementHeader=" + this.getAnnouncementHeader() + ", announcementPostDate=" + this.getAnnouncementPostDate() + ", announcementDescription=" + this.getAnnouncementDescription() + ")";
    }
}
