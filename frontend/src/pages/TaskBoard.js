import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Task from '../components/TaskBoard/Task';
import '../assets/task.css';
import TaskDetailOverlay from '../components/TaskBoard/TaskOverlayDetail';
import Board from '../components/TaskBoard/Board';

const TaskBoard = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });

  const firstName = "John";
  const lastName = "Doe";
  const initials = `${firstName[0]}${lastName[0]}`;

  const placeholderTasks = [
    { taskName: 'This is a really long named task', taskStatus: 'TODO', taskPriority: 'HIGH', taskDate: '2024-11-20' },
    { taskName: 'TASK 2', taskStatus: 'In Progress', taskPriority: 'LOW', taskDate: '2024-11-22' },
    { taskName: 'TASK 3', taskStatus: 'In Progress', taskPriority: 'NORMAL', taskDate: '2024-11-22' },
    { taskName: 'TASK 4', taskStatus: 'TODO', taskPriority: 'URGENT', taskDate: '2024-11-23' },
    { taskName: 'TASK 7', taskStatus: 'TODO', taskPriority: 'URGENT', taskDate: '2024-11-23' },
    
    { taskName: 'TASK 5', taskStatus: 'Completed', taskPriority: 'HIGH', taskDate: '2024-11-24' },
    
  ];

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
  
  const getTasksByStatus = (status) => {
    return sortedDates
      .filter(date => groupedTasks[date][status].length > 0)
      .map(date => ({
        taskDate: date,
        tasks: groupedTasks[date][status]
      }));
  };
  
  const todoTasks = getTasksByStatus('TODO');
  const inProgressTasks = getTasksByStatus('In Progress');
  const completedTasks = getTasksByStatus('Completed');


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
            <Board title="In Progress" tasks={inProgressTasks} />
            <Board title="Completed" tasks={completedTasks} />
          </div>
          
        </div>
      </div>
    </div>
  );
}

export default TaskBoard;
