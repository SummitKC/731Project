import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../assets/professorhome.css';
import '../assets/coursepage.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import AssignmentBoard from '../components/Course/AssignmentBoard';
import AnnouncementBoard from '../components/Course/AnnouncementBoard';


const CoursePageProfessor = () => {
  const location = useLocation();
  const { courseName, courseCode, professor } = location.state || {};

  const [assignments, setAssignments] = useState([]);
  const [announcements, setAnnouncements] = useState([]);

  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });

  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  const navigate = useNavigate();

  const [showConfirm, setShowConfirm] = useState(false);


  const confirmCourseArchive = () => {
    setShowConfirm(true);
  };
  
  const cancelCourseArchive = () => {
    setShowConfirm(false);
  };

  const fetchCourseData = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`http://localhost:8080/api/professor/course/${courseCode}`, {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        const data = await response.json();
        setAssignments(data.assignments);
        setAnnouncements(data.announcements);
      } else {
        console.error('Error fetching course data:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching course data:', error);
    }
  };

  useEffect(() => {
    fetchCourseData();
  }, []);
  
  const handleArchiveCourse = async () =>{
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`http://localhost:8080/api/professor/dashboard/courses/${courseCode}/archive?archived=true`, {
        method: 'PUT',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },

      });
      if (response.ok) {
        navigate("/professor/home");
      } else {
        console.error('Error fetching course data:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching course data:', error);
    }
    
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <ProfessorSidebar firstName={firstName} lastName={lastName} />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <div className='course-page-header'>
              <h1 style={isDesktopOrLaptop ? { paddingTop: '10px', paddingLeft: '30px' } : {}}>{courseName} Course Page</h1>
              
              <button style={{marginTop:'30px'}} className='generic-button' onClick={confirmCourseArchive}>Archive Course</button>
          
          </div>
          
             
          <div className='course-page-content-wrapper'>
            <AssignmentBoard 
              assignments={assignments} 
              title="Assignments" 
              type='professor' 
              courseCode={courseCode} 
              fetchCourseData={fetchCourseData}
            />
            <AnnouncementBoard 
              announcements={announcements} 
              type='professor' 
              courseCode={courseCode} 
              fetchCourseData={fetchCourseData}
            />
          </div>
          
        </div>
      </div>
      
      {showConfirm && (
        <div className="confirmation-modal">
          <div className="confirmation-content">
            <p>Are you sure you want to archive the course?</p>
            <button className='generic-button' onClick={handleArchiveCourse}>Yes</button>
            <button className='generic-button' onClick={cancelCourseArchive}>No</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CoursePageProfessor;
