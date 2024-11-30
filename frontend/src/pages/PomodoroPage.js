import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Timer from '../components/Pomodoro/Timer';
import Task from '../components/TaskBoard/Task';
import '../assets/task.css';
import '../assets/pomodoro.css';

const PomodoroPage = () => {
    const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
    const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
    const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
    const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });
  
    const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
    const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
    const initials = `${firstName[0]}${lastName[0]}`;


  
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
    
            <Timer className="timer"/>
            </div>
            
          </div>
        </div>
    );
  }
  
  export default PomodoroPage;
  