import React, { useState } from 'react';
import Announcement from './Announcement';
import { useNavigate } from 'react-router-dom';
import '../../assets/coursepage.css';
import NewAnnouncementModal from '../Course/NewAnnouncementModal';

const AnnouncementBoard = ({ announcements, type, courseCode, fetchCourseData }) => {
  const [showAnnouncementModal, setShowAnnouncementModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  
  const navigate = useNavigate();

  // Group announcements by post date
  const groupedAnnouncements = announcements.reduce((acc, announcement) => {
    const { announcementPostDate } = announcement;
    if (!acc[announcementPostDate]) {
      acc[announcementPostDate] = [];
    }
    acc[announcementPostDate].push(announcement);
    return acc;
  }, {});


  const sortedDatesDesc = Object.keys(groupedAnnouncements).sort((a, b) => new Date(a) - new Date(b));

  const handleNewAnnouncement = (data) => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      fetch(`http://localhost:8080/api/professor/course/${courseCode}/announcement`, {
        method: 'POST',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
      })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text().then(text => text ? JSON.parse(text) : {});
      })
      .then(result => {
        console.log('New announcement created:', result);
        setShowAnnouncementModal(false);
        fetchCourseData();
      })
      .catch(error => {
        console.error('Error creating announcement:', error);
        setErrorMessage('Error creating announcement. Please try again.');
      });
    }
  };

  return (
    <div className='announcement-wrapper'>
      <h2>Announcements</h2>
      
      <div className='announcement-box'>
        <div style={type === 'professor' ? {} : { display: 'none' }} className='announcement-wrapper-header'>
          <button className='generic-button' onClick={() => setShowAnnouncementModal(true)}>New Announcement</button>
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
      <NewAnnouncementModal
        show={showAnnouncementModal}
        handleClose={() => setShowAnnouncementModal(false)}
        handleSubmit={handleNewAnnouncement}
        courseCode={courseCode}
        setErrorMessage={setErrorMessage}
      />
    </div>
  );
};

export default AnnouncementBoard;
