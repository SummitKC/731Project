import React from 'react';
import Announcement from './Announcement'; // Adjust the path if necessary
import '../../assets/coursepage.css';

const AnnouncementBoard = ({ announcements, type }) => {
  // Group announcements by post date
  const groupedAnnouncements = announcements.reduce((acc, announcement) => {
    const { announcementPostDate } = announcement;
    if (!acc[announcementPostDate]) {
      acc[announcementPostDate] = [];
    }
    acc[announcementPostDate].push(announcement);
    return acc;
  }, {});

  // Sort dates in descending order
  const sortedDatesDesc = Object.keys(groupedAnnouncements).sort((a, b) => new Date(b) - new Date(a));

  return (
    <div className='announcement-wrapper'>
      <h2>Announcements</h2>
      
      <div className='announcement-box'>
        <div style={type === 'professor' ? {} : { display: 'none' }} className='announcement-wrapper-header'>
          <button className='generic-button'>New Announcement</button>
        </div>
        <br style={type === 'professor' ? { display: 'none' } : {}} />
        <br style={type === 'professor' ? { display: 'none' } : {}} />

        {sortedDatesDesc.map((date, dateIdx) => (
          <div key={date} className=''>
            {groupedAnnouncements[date].map((announcement, idx) => (
              <div key={idx}>
                <Announcement
                  announcementHeader={announcement.announcementHeader}
                  announcementPostDate={announcement.announcementPostDate}
                  announcementDescription={announcement.announcementDescription}
                />
                {!(dateIdx === sortedDatesDesc.length - 1 && idx === groupedAnnouncements[date].length - 1) && (
                  <hr />
                )}
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default AnnouncementBoard;
