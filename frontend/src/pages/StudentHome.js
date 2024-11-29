import React, { useState, useEffect } from 'react';
import '../assets/studenthome.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Course from '../components/Course/Course';
import Board from '../components/TaskBoard/Board';
import { useNavigate } from 'react-router-dom';
import Modal from '../components/Course/Modal';


const StudentHome = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });


  const [courses, setCourses] = useState([]);
  const [tasks, setTasks] = useState([]);
  const term = "Fall 2024";

  const navigate = useNavigate();
  
  const name = ""; 

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      const fetchProfile = async () => {
        try {
          const response = await fetch('http://localhost:8080/api/student/home/profile', {
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
            localStorage.setItem('studentID', data.studentID);
          } else {
            console.error('Error fetching profile:', response.statusText);
          }
        } catch (error) {
          console.error('Error fetching profile:', error);
        }
      };
  
      fetchProfile();
      
      // Fetch courses
      fetch('http://localhost:8080/api/student/dashboard/courses', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        }
      })
        .then(response => response.json())
        .then(data => setCourses(data))
        .catch(error => console.error('Error fetching courses:', error));
      
      // Fetch tasks
      fetch('http://localhost:8080/api/student/dashboard/tasks', {
        method: 'GET',
        headers: {
          Authorization: token,
          'Content-Type': 'application/json',
        }
      })
        .then(response => response.json())
        .then(data => setTasks(data))
        .catch(error => console.error('Error fetching tasks:', error));
    }
  }, [navigate]);

  const firstName = localStorage.getItem('name')?.split(' ')[0] || "John";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;
  const groupedTasks = {};

  tasks.forEach(task => {
    const { taskDate, taskStatus } = task;
    if (!groupedTasks[taskDate]) {
      groupedTasks[taskDate] = { TODO: [], 'IN_PROGRESS': [], Completed: [] };
    }
    groupedTasks[taskDate][taskStatus].push(task);
  });

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));

  const convertGroupedTasks = (groupedTasks) => {
    return Object.keys(groupedTasks).map(date => {
        const dateObj = new Date(date);
        
        // Extract year, month, day
        const year = dateObj.getFullYear();
        const month = (dateObj.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
        const day = dateObj.getDate().toString().padStart(2, '0');

        // Format the date as YYYY-MM-DD
        const formattedDate = `${year}-${month}-${day}`;

        const statuses = groupedTasks[date];
        return {
            taskDate: formattedDate,
            tasks: Object.values(statuses).flat()
        };
    });
  };


  const allTasks = convertGroupedTasks(groupedTasks);

  

  const handleCourseClick = (courseCode, courseName) => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      console.log(courseCode);
      fetch(`http://localhost:8080/api/student/course/${courseCode}`, {
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
        navigate(`/course/${courseCode}`, {
          state: {
            courseCode,
            courseName,
            assignments: data.assignments,
            announcements: data.announcements
          },
        });
      
      }).catch(error => {
        console.error('Error fetching course data:', error);
      });
       
    }
  };
  
  const [showModal, setShowModal] = useState(false);
  const [courseCode, setCourseCode] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  
  const handleJoinCourseClick = () => {
    setShowModal(true);
  };
  
  const handleCloseModal = () => {
    setShowModal(false);
  };
  
  const handleSubmitJoinCourse = (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      fetch('http://localhost:8080/api/student/home/courses/join', {
        method: 'POST',
        headers: {
          Authorization: token,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ courseCode }),
      })
      .then(response => {
        if (response.status === 204) {
          return {};
        } else if (!response.ok) {
          return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
      })
      .then(data => {
        window.location.reload();
      })
      .catch(error => {
        console.error('Error joining course:', error);
        if (error.message === 'Course not found') {
          setErrorMessage('Course not found. Please check the course code.');
        } else if (error.message === 'Course is archived') {
          setErrorMessage('Course is archived and cannot be joined.');
        } else if (error.message === 'Student already is enrolled in this course') {
          setErrorMessage('You are already enrolled in this course.');
        } else {
          setErrorMessage('Error joining course. Please try again.');
        }
      });
    }
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
          <h1 style={isDesktopOrLaptop ? { paddingTop: '30px', paddingLeft: '30px' } : { }}>Welcome to your Dashboard</h1>

          <div className='dashboard-wrapper'>
            <div className='courses-box'>
              <h2>Your Courses for {term}</h2>
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
              <button className='generic-button font' style={{ alignSelf: 'end' }} onClick={handleJoinCourseClick}>Join Course</button>
            </div>
            <Board title="Tasks Overview" tasks={allTasks} />
          </div>
        </div>
      </div>
      <Modal
        show={showModal}
        handleClose={handleCloseModal}
        handleSubmit={handleSubmitJoinCourse}
        courseCode={courseCode}
        setCourseCode={setCourseCode}
        errorMessage={errorMessage}
      />


    </div>
  );
};

export default StudentHome;
