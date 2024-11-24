import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Task from '../components/TaskBoard/Task';
import '../assets/task.css';
import TaskDetailOverlay from '../components/TaskBoard/TaskOverlayDetail';

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


  const groupedTasks = tasks.reduce((acc, task) => {
    const { taskDate, taskStatus } = task;
    if (!acc[taskDate]) {
      acc[taskDate] = { TODO: [], 'In Progress': [], Completed: [] };
    }
    acc[taskDate][taskStatus].push(task);
    return acc;
  }, {});

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));


  const [selectedTask, setSelectedTask] = useState(null);
  const handleTaskClick = (task) => { 
    setSelectedTask(task);
  };
  const handleClose = () => { 
    setSelectedTask(null);
  }
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
            
            <div className='main-taskboard-box'>
              <h2>TODO</h2>
                {sortedDates.filter(date => groupedTasks[date].TODO.length > 0).map(date => (
                  <div className='tasks-container' key={date}>
                    <h3 className='task-header'>{date}</h3>
                    {groupedTasks[date].TODO.map((task, index) => (
                      <Task
                        key={index}
                        taskName={task.taskName}
                        taskPriority={task.taskPriority}
                        taskStatus={task.taskStatus}
                        taskDate={task.taskDate}
                        onClick={() => handleTaskClick(task)}
                      />
                    ))}
                  </div>
                ))}
            </div>
            
            <div className='main-taskboard-box'>
              <h2>In Progress</h2>
              {sortedDates.filter(date => groupedTasks[date]['In Progress'].length > 0).map(date => (
                <div className='tasks-container' key={date}>
                  <h3 className='task-header'>{date}</h3>
                  {groupedTasks[date]['In Progress'].map((task, index) => (
                    <Task
                      key={index}
                      taskName={task.taskName}
                      taskPriority={task.taskPriority}
                      taskStatus={task.taskStatus}
                      taskDate={task.taskDate}
                      onClick={() => handleTaskClick(task)}
                    />
                  ))}
                </div>
              ))}
            </div>

            <div className='main-taskboard-box'>
              <h2>Completed</h2>
              {sortedDates.filter(date => groupedTasks[date].Completed.length > 0).map(date => (
                <div className='tasks-container' key={date}>
                  <h3 className='task-header'>{date}</h3>
                  {groupedTasks[date].Completed.map((task, index) => (
                    <Task
                      key={index}
                      taskName={task.taskName}
                      taskPriority={task.taskPriority}
                      taskStatus={task.taskStatus}
                      taskDate={task.taskDate}
                      onClick={() => handleTaskClick(task)}
                    />
                  ))}
                </div>
              ))}
            </div>
          
          </div>
          
          <TaskDetailOverlay selectedTask={selectedTask} handleClose={handleClose} />
        </div>
      </div>
    </div>
  );
}

export default TaskBoard;
