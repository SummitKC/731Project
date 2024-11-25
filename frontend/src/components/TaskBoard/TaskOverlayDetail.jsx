import React from 'react';
import '../../assets/task.css';

const TaskDetailOverlay = ({ selectedTask, handleClose }) => {
  if (!selectedTask) return null;

  const prioclass = selectedTask.taskPriority.toLowerCase();
  
  return (
    <>
      <div className="task-detail-overlay" onClick={handleClose}></div>
      <div className="task-detail-container">
        <button className="close-button" onClick={handleClose}>&times;</button>
        <h2>Task Details</h2>
        <p><strong>Name:</strong> {selectedTask.taskName}</p>
        <p><strong>Status:</strong> {selectedTask.taskStatus}</p>
        <p><strong>Priority:</strong><span className={`${ prioclass }`}> {selectedTask.taskPriority}</span></p>
        <p><strong>Date:</strong> {selectedTask.taskDate}</p>
        <div className='overlay-button-container'>
          <button>Edit Task</button>
          <button>Delete Task</button>
          <button>Complete Task</button>
        </div>
      </div>
    </>
  );
};

export default TaskDetailOverlay;
