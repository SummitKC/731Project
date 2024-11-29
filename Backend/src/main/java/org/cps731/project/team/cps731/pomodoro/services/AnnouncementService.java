package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.repo.announcement.AnnouncementRepo;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private StudentService studentService;

    public List<Announcement> getAnnouncementsByCourse(String courseCode, int page, int size) {
        var studentID = SecurityUtil.getAuthenticatedUserID();
        var student = studentService.getStudentById(studentID);

        if (student.getCourses().stream().noneMatch(c -> c.getCourseCode().equals(courseCode))) {
            throw new AuthorizationDeniedException(
                    "Student is not enrolled in this course",
                    new AuthorizationDecision(false)
                    );
        }

        return announcementRepo.findAllByCourse_CourseCode(courseCode);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepo.findById(id).orElse(null);
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepo.save(announcement);
    }

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