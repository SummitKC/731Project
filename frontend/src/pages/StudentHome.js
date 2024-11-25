import React, { useState, useEffect } from 'react';
import '../assets/studenthome.css';
import '../assets/global.css'
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Course from '../components/Course/Course';
import Task from '../components/TaskBoard/Task';

const StudentHome = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });

  const firstName = "John";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;

  const placeholderCourses = [
    { courseCode: 'CPS 510', courseName: 'Database Systems I' },
    { courseCode: 'CPS 310', courseName: 'Computer Architecture II' },
  ];

  const placeholderTasks = [
    { taskName: 'This is a really long named task', taskStatus: 'TODO', taskPriority: 'HIGH', taskDate: '2024-11-20'},
    { taskName: 'TASK 2', taskStatus: 'In Progress', taskPriority: 'LOW', taskDate: '2024-11-22'},
    { taskName: 'TASK 3', taskStatus: 'In Progress', taskPriority: 'NORMAL', taskDate: '2024-11-22' },
    { taskName: 'TASK 4', taskStatus: 'TODO', taskPriority: 'URGENT', taskDate: '2024-11-23' },
    { taskName: 'TASK 5', taskStatus: 'TODO', taskPriority: 'HIGH', taskDate: '2024-11-24' },
  ];

  const [courses, setCourses] = useState(placeholderCourses);
  const [tasks, setTasks] = useState(placeholderTasks);
  const term = "Fall 2024";

  // Group and sort tasks by date
  const groupedTasks = tasks.reduce((acc, task) => {
    const date = task.taskDate;
    if (!acc[date]) {
      acc[date] = [];
    }
    acc[date].push(task);
    return acc;
  }, {});

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));

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
                  <Course
                    key={index}
                    courseCode={course.courseCode}
                    courseName={course.courseName}
                    courseIcon={{}}
                  />
                ))}
              </div>
              <button className='generic-button font' style={{ alignSelf: 'end' }}>Join Course</button>
            </div>
            <div className='taskboard-box'>
              <h2>Tasks Overview</h2>
              
                {sortedDates.map(date => (
                  <div className='tasks-container' key={date}>
                    <h3 className='task-header'>{date}</h3>
                    {groupedTasks[date].map((task, index) => (
                      <Task
                        key={index}
                        taskName={task.taskName}
                        taskPriority={task.taskPriority}
                        taskStatus={task.taskStatus}
                        taskDate={task.taskDate}
                        board={'true'}
                      />
                    ))}
                  </div>
                ))}
              
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default StudentHome;
