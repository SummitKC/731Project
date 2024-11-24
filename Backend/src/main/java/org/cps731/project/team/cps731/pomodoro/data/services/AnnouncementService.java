package org.cps731.project.team.cps731.pomodoro.data.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.repo.announcement.AnnouncementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;

    public List<Announcement> getAnnouncementsByCourse(CourseID courseID, int page, int size) {
        if (courseID == null) {
            throw new IllegalArgumentException("CourseID cannot be null");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        return announcementRepo.findAllByCourse_CourseID(courseID, pageable);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepo.findById(id).orElse(null);
    }

/*     public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepo.save(announcement);
    } */

    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        Announcement existingAnnouncement = announcementRepo.findById(id).orElse(null);
        if (existingAnnouncement != null) {
            existingAnnouncement.setTitle(announcement.getTitle());
            existingAnnouncement.setDescription(announcement.getDescription());
            existingAnnouncement.setIssueTime(announcement.getIssueTime());
            existingAnnouncement.setCourse(announcement.getCourse());
            return announcementRepo.save(existingAnnouncement);
        }
        return null;
    }

    public void deleteAnnouncement(Long id) {
        announcementRepo.deleteById(id);
    }
}