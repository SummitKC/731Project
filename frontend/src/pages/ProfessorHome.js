import React, { useState, useEffect } from 'react';
import '../assets/professorhome.css';
import { Link } from 'react-router-dom';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import Course from '../components/Course/Course';
import '../assets/global.css';
import { useNavigate } from 'react-router-dom';


const ProfessorHome = () => {
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' });
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' });


  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const term = "Fall 2024";
  
  const navigate = useNavigate();

  const [courses, setCourses] = useState([]);
  
  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      const fetchProfile = async () => {
        try {
          const response = await fetch('http://localhost:8080/api/professor/dashboard/profile', {
            method: 'GET',
            headers: {
              Authorization: `${token}`,
              'Content-Type': 'application/json',
            }
          });
          if (response.ok) {
            const data = await response.json();
            console.log(data);
            localStorage.setItem('name', data.name);
            localStorage.setItem('email', data.email);
            localStorage.setItem('professorID', data.professorID);
          } else {
            console.error('Error fetching profile:', response.statusText);
          }
        } catch (error) {
          console.error('Error fetching profile:', error);
        }
      };
  
      fetchProfile();
      
      
      // Fetch courses
      fetch('http://localhost:8080/api/professor/dashboard/courses', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(data => setCourses(data))
      .catch(error => console.error('Error fetching courses:', error));
    }
  }, [navigate]);

  const handleCourseClick = (courseCode, courseName) => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      console.log(courseCode);
      console.log(courseName);
      
      fetch(`http://localhost:8080/api/professor/course/${courseCode}`, { 
        method: 'GET', 
        headers: {
          Authorization: token,
          'Content-Type': 'application/json',
        }
      }).then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      }).then(data => {
        navigate(`/professor/course/${courseCode}`, {
          state: {
            courseCode,
            courseName,
            professor: data.professor,
            assignments: data.assignments,
            announcements: data.announcements
          },
        });
      
      }).catch(error => {
        console.error('Error fetching course data:', error);
      });
       
    }
  };
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
        
        <div className="main-container">
          <div className='header-container'>
            <h1 style={isMobile ? {} : { paddingTop: '10px', paddingLeft: '30px' }}>Welcome to your Dashboard</h1>    
            <Link className="generic-button" to="/professor/home">Create Course</Link>   
          </div>
          <h2 className='lpad-50'>Here are your courses for <b>{term}</b></h2>
          <div className='courses-container'>
            {courses.map((course, index) => (
              <div key={index} onClick={() => handleCourseClick(course.courseCode, course.name)}>
                <Course
                  courseCode={course.courseCode}
                  courseName={course.name}
                  courseIcon={{}}
                />
              </div>
            ))}

          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfessorHome;
