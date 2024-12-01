import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'
import '../assets/studenthome.css';
import '../assets/coursepage.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import AssignmentBoard from '../components/Course/AssignmentBoard';
import AnnouncementBoard from '../components/Course/AnnouncementBoard';

const CoursePageStudent = () => {
  const location = useLocation();
  const { courseName, courseCode, assignments = [], announcements = [] } = location.state || {};

  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });

  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  const navigate = useNavigate();

  const [showConfirm, setShowConfirm] = useState(false);


  const handleLeaveCourse = () => {
    setShowConfirm(true);
  };

  const confirmLeaveCourse = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      fetch(`http://localhost:8080/api/student/course/${courseCode}`, { 
        method: 'DELETE', 
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        }
      }).then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        navigate('/student/home');
      }).then(data => {
        setShowConfirm(false);
        navigate('/student/home');
      }).catch(error => {
        console.error('Error leaving course:', error);
      });
    }
  };

  const cancelLeaveCourse = () => {
    setShowConfirm(false);
  };


  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName={firstName} lastName={lastName} />

      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <div className='course-page-header'>
            <h1 style={isDesktopOrLaptop ? { paddingTop: '10px', paddingLeft: '30px' } : {}}>{courseName} Course Page</h1>
            <button style={{marginTop:'30px'}} className='generic-button' onClick={handleLeaveCourse}>Leave Course</button>
          </div>
          <div className='course-page-content-wrapper'>
            <AssignmentBoard assignments={assignments} title="Assignments" type='student' />
            <AnnouncementBoard announcements={announcements} type='student' />
          </div>
        </div>
      </div>
    
      {showConfirm && (
        <div className="confirmation-modal">
          <div className="confirmation-content">
            <p>Are you sure you want to leave the course?</p>
            <button className='generic-button' onClick={confirmLeaveCourse}>Yes</button>
            <button className='generic-button' onClick={cancelLeaveCourse}>No</button>
          </div>
        </div>
      )}
  </div>

  );
};

export default CoursePageStudent;
