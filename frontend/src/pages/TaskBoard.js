import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Board from '../components/TaskBoard/Board';
import { useNavigate } from 'react-router-dom';

const TaskBoard = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });

  const firstName = "John";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;

  const [tasks, setTasks] = useState([]);
  const term = "Fall 2024";
  const navigate = useNavigate();
  useEffect(() => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      // Fetch tasks
      fetch('http://localhost:8080/api/student/taskboard/tasks', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(data => {
        const fetchedTasks = [
          ...data.todoTasks,
          ...data.inProgressTasks,
          ...data.reviewingTasks,
          ...data.completedTasks
        ];
        setTasks(fetchedTasks);
      })
      .catch(error => console.error('Error fetching tasks:', error));
    }
  }, []);

  const groupedTasks = {};

  tasks.forEach(task => {
    const { taskDate, taskStatus } = task;
    const dateObj = new Date(taskDate);
    const year = dateObj.getFullYear();
    const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
    const day = dateObj.getDate().toString().padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
  
    if (!groupedTasks[formattedDate]) {
      groupedTasks[formattedDate] = { TODO: [], IN_PROGRESS: [], COMPLETED: [] };
    }
    const status = taskStatus === 'REVIEWING' ? 'IN_PROGRESS' : taskStatus;
    groupedTasks[formattedDate][status].push(task);
  });
  

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));

  const getTasksByStatus = (status) => {
    return sortedDates
      .filter(date => groupedTasks[date][status].length > 0)
      .map(date => ({
        taskDate: date,
        tasks: groupedTasks[date][status]
      }));
  };
  
  const todoTasks = getTasksByStatus('TODO');
  const inProgressTasks = getTasksByStatus('IN_PROGRESS');
  const completedTasks = getTasksByStatus('COMPLETED');

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName="John" lastName="Doe" />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <div className='header-container'>
            <h1>Your Taskboard</h1>      
            <button className='generic-button'>Create Task</button>
          </div>
          <div className='dashboard-wrapper'>  
            <Board title="TODO" tasks={todoTasks} />
            <Board title="IN PROGRESS" tasks={inProgressTasks} />
            <Board title="COMPLETED" tasks={completedTasks} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaskBoard;
