import React from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/professorhome.css';
import '../assets/coursepage.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import AssignmentBoard from '../components/Course/AssignmentBoard';
import AnnouncementBoard from '../components/Course/AnnouncementBoard';

const CoursePageProfessor = () => {
  const location = useLocation();
  const { courseName, courseCode, professor, assignments = [], announcements = [] } = location.state || {};

  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });

  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <ProfessorSidebar firstName={firstName} lastName={lastName} />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <h1 style={isDesktopOrLaptop ? { paddingTop: '10px', paddingLeft: '30px' } : {}}>{courseCode} - {courseName} Course Page</h1>
          <div className='course-page-content-wrapper'>
            <AssignmentBoard assignments={assignments} title="Assignments" type='professor' />
            <AnnouncementBoard announcements={announcements} type='professor' />
          </div>
        </div>
      </div>
    </div>
  );
};

export default CoursePageProfessor;
