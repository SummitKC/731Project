import React, { useState } from 'react';
import '../assets/studenthome.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Course from '../components/Course/Course';
import Board from '../components/TaskBoard/Board';
import { useNavigate } from 'react-router-dom';
import { placeholderCourses, placeholderTasks, placeholderAssignments, placeholderAnnouncements } from './data';

const StudentHome = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });

  const firstName = "John";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;

  const [courses, setCourses] = useState(placeholderCourses);
  const [tasks, setTasks] = useState(placeholderTasks);
  const term = "Fall 2024";

  

  const groupedTasks = {};

  tasks.forEach(task => {
    const { taskDate, taskStatus } = task;
    if (!groupedTasks[taskDate]) {
      groupedTasks[taskDate] = { TODO: [], 'In Progress': [], Completed: [] };
    }
    groupedTasks[taskDate][taskStatus].push(task);
  });

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));

  const convertGroupedTasks = (groupedTasks) => {
    return Object.keys(groupedTasks).map(date => {
      const statuses = groupedTasks[date];
      return {
        taskDate: date,
        tasks: Object.values(statuses).flat()
      };
    });
  };

  const allTasks = convertGroupedTasks(groupedTasks);

  const navigate = useNavigate();

  const handleCourseClick = (courseCode, courseName) => {
    navigate(`/course/${courseCode}`, {
      state: {
        courseCode,
        courseName,
        assignments: placeholderAssignments,
        announcements: placeholderAnnouncements,
      },
    });
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName="John" lastName="Doe" />
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
                  <div key={index} onClick={() => handleCourseClick(course.courseCode, course.courseName)}>
                    <Course
                      courseCode={course.courseCode}
                      courseName={course.courseName}
                      courseIcon={{}}
                    />
                  </div>
                ))}
              </div>
              <button className='generic-button font' style={{ alignSelf: 'end' }}>Join Course</button>
            </div>
            <Board title="Tasks Overview" tasks={allTasks} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentHome;
