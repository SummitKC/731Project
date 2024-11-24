import React, { useState } from 'react';
import '../assets/professorhome.css';
import { Link } from 'react-router-dom';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import Course from '../components/Course/Course';
import '../assets/global.css';

const ProfessorHome = () => {
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' });
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' });

  const firstName = "Jane";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const term = "Fall 2024";

  const placeholderCourses = [
    { courseCode: 'CPS 510', courseName: 'Database Systems I' },
    { courseCode: 'CPS 310', courseName: 'Computer Architecture II' },
    { courseCode: 'CPS 420', courseName: 'Discrete Structures' },
    { courseCode: 'CPS 530', courseName: 'Web Application Development' },
    { courseCode: 'Code', courseName: 'Name' },
  ];

  const [courses, setCourses] = useState(placeholderCourses);

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}> 
      <ProfessorSidebar firstName="Jane" lastName="Doe" />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-container">
          <div className='header-container'>
            <h1 style={isMobile ? {} : { paddingTop: '10px', paddingLeft: '30px' }}>Welcome to your Dashboard</h1>    
            <Link className="generic-button" to="/professor/home">Create Course</Link>   
          </div>
          <h2 className='lpad-50'>Here are your courses for <b>{term}</b></h2>
          <div className='courses-container'>
            {courses.map((course, index) => (
              <Course
                key={index}
                courseCode={course.courseCode}
                courseName={course.courseName}
                courseIcon={{}}
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfessorHome;
