import React, { useState, useEffect } from 'react';
import '../assets/professorhome.css';
import { Link, useNavigate } from 'react-router-dom';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import Course from '../components/Course/Course';
import '../assets/global.css';

const CourseArchivePage = () => {
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' });
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' });

  const firstName = "Jane";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const navigate = useNavigate();

  const [groupedCourses, setGroupedCourses] = useState({});

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      // Fetch courses
      fetch('http://localhost:8080/api/professor/dashboard/courses', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(data => {
        // Filter only the archived courses
        const archivedCourses = data.filter(course => course.archived === true);
        
        // Group courses by term and year
        const grouped = archivedCourses.reduce((acc, course) => {
          const key = `${course.term} ${course.year}`;
          if (!acc[key]) acc[key] = [];
          acc[key].push(course);
          return acc;
        }, {});

        setGroupedCourses(grouped);
      })
      .catch(error => console.error('Error fetching courses:', error));
    }
  }, [navigate]);


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
          {Object.keys(groupedCourses).map((termYear, index) => (
            <div key={index} className='term-group'>
              <h2 className='lpad-50'>{termYear}</h2>
              <div className='courses-container'>
                {groupedCourses[termYear].map((course, idx) => (
                  <div key={idx}>
                    <Course
                      courseCode={course.courseCode}
                      courseName={course.name}
                      courseIcon={{}}
                    />
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default CourseArchivePage;
