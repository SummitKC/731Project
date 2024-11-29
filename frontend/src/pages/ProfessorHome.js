import React, { useState, useEffect } from 'react';
import '../assets/professorhome.css';
import { Link } from 'react-router-dom';
import { useMediaQuery } from 'react-responsive';
import ProfessorSidebar from '../components/Common/ProfessorSidebar';
import Course from '../components/Course/Course';
import '../assets/global.css';
import { useNavigate } from 'react-router-dom';
import CreateCourseModal from '../components/Course/CreateCourseModal';

const ProfessorHome = () => {
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' });
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' });

  const currentTerm = "FALL"

  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const [year, setYear] = useState('');
  const [term, setTerm] = useState(currentTerm);

  
  const [showModal, setShowModal] = useState(false);
  const [courseCode, setCourseCode] = useState('');
  const [courseName, setCourseName] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  
  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;
  
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
      .then(data => {
        const filteredCourses = data.filter(course => course.archived === false); 
        setCourses(filteredCourses);
      })
      .catch(error => console.error('Error fetching courses:', error));
    }
  }, [navigate]);

  const handleCreateCourseClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleSubmitCreateCourse = (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');

    if (!token) {
      navigate('/login');
    } else {
      fetch('http://localhost:8080/api/professor/dashboard/courses', {
        method: 'POST',
        headers: {
          Authorization: `${token}`, // Ensure Bearer prefix
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          courseCode: courseCode,
          name: courseName,
          term: String(term),
          year: Number(year),
          archived: false
        }),
      })
      .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
          return response.text().then(text => { throw new Error(text); });
        }
        
      })
      .then(data => {
        setShowModal(false);
        window.location.reload();
      })
      .catch(error => {
        console.error(error)
        setErrorMessage('Error creating course. Please Ensure course code is unique.');
      });
    }
  };
  


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
  

  const groupedCourses = courses.reduce((acc, course) => {
    const key = `${course.year}-${course.term}`;
    if (!acc[key]) {
      acc[key] = [];
    }
    acc[key].push(course);
    return acc;
  }, {});
  

  const renderGroupedCourses = () => {
    return Object.keys(groupedCourses).map(key => {
      const [year, term] = key.split('-');
      return (
        <div key={key} className="course-group">
          <h2 className='lpad-50'>{`${term} ${year}`}</h2>
          <div className='courses-container'>
            {groupedCourses[key].map((course, index) => (
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
      );
    });
  };
  

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
            <button className="generic-button" onClick={handleCreateCourseClick}>Create Course</button>   
          </div>
          {renderGroupedCourses()}
        </div>
      </div>
  
      <CreateCourseModal
        show={showModal}
        handleClose={handleCloseModal}
        handleSubmitCreateCourse={handleSubmitCreateCourse} // Ensure this prop name matches
        courseCode={courseCode}
        setCourseCode={setCourseCode}
        courseName={courseName}
        setCourseName={setCourseName}
        year={year}
        setYear={setYear}
        term={term}
        setTerm={setTerm}
        errorMessage={errorMessage}
        setErrorMessage={setErrorMessage} // Pass setErrorMessage to modal
        />

    </div>
  );
  
  
}

export default ProfessorHome;
