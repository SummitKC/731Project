import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Timer from '../components/Pomodoro/Timer';
import '../assets/task.css';
import '../assets/pomodoro.css';
import { useNavigate } from 'react-router-dom';

const PomodoroPage = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  const [tasks, setTasks] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      // get all tasks
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
          ...data.reviewingTasks
        ];
        setTasks(fetchedTasks);
      })
      .catch(error => console.error('Error fetching tasks:', error));
    }
  }, [navigate]);

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName={firstName} lastName={lastName} />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content main-container">
          <h1 className='header-container'>Pomodoro</h1>
          <Timer tasks={tasks} className="timer" />
        </div>
      </div>
    </div>
  );
}

export default PomodoroPage;
