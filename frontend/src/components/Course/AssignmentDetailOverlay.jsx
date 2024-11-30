import React, { useState, useEffect } from 'react';
import '../../assets/task.css';
import '../../assets/global.css';


const AssignmentDetailOverlay = ({ selectedAssignment, handleClose }) => {
  const [taskName, setTaskName] = useState('');
  const [taskStatus, setTaskStatus] = useState('TODO');
  const [taskPriority, setTaskPriority] = useState('NORMAL');
  const [taskDate, setTaskDate] = useState('');

  useEffect(() => {
   
  }, [selectedAssignment]);

  const handleCreateTask = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('Expired or bad token, login again!');
      return;
    }

    const taskData = {
      taskName,
      taskStatus,
      taskPriority,
      taskDate: new Date(taskDate).toISOString()
    };

    const assignmentID = selectedAssignment.assignmentID;

    try {
      const response = await fetch(`http://localhost:8080/api/student/taskboard/tasks?assignment=${assignmentID}`, {
        method: 'POST',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });

      if (response.ok) {
        console.log('Task created successfully.');
        handleClose();
        window.location.reload();
      } else {
        console.error('Error creating task:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  return (
    <>
      <div className="task-detail-overlay" onClick={handleClose}></div>
      <div className="task-detail-container">
        <button className="close-button" onClick={handleClose}>&times;</button>
        <h2>Assignment Details</h2>
        <p><strong>Name:</strong> {selectedAssignment.assignmentName}</p>
        <p><strong>Due Date:</strong> {selectedAssignment.assignmentDueDate}</p>
        <p><strong>Due Time:</strong> {selectedAssignment.assignmentDueTime}</p>
        
        <div style={selectedAssignment.type == 'professor' ? {display:'none'} : {}}>
          <h3>Create Task from Assignment</h3>
          <form className='font' onSubmit={(e) => { e.preventDefault(); handleCreateTask(); }}>
            <label>Task Name: </label>
            <input className='font' type="text" value={taskName} onChange={(e) => setTaskName(e.target.value)} required />
            <br></br>
            <br></br>
            <label>Status: </label>
            <select className='font' value={taskStatus} onChange={(e) => setTaskStatus(e.target.value)} required>
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">IN PROGRESS</option>
              <option value="COMPLETE">COMPLETED</option>
            </select>
            <br></br>
            <br></br>
  
            <label>Priority: </label>
            <select className={`${taskPriority.toLowerCase()} font`} value={taskPriority} onChange={(e) => setTaskPriority(e.target.value)} required>
              <option className='low' value="LOW">LOW</option>
              <option className='normal' value="NORMAL">NORMAL</option>
              <option  className='high' value="HIGH">HIGH</option>
              <option className='urgent' value="URGENT">URGENT</option>
            </select>
            <br></br>
            <br></br>
  
            <label>Date and Time: </label>
            <input className='font' type="datetime-local" value={taskDate} onChange={(e) => setTaskDate(e.target.value)} required />
            <br></br>
            <br></br>
  
            <button className='generic-button' type="submit">Create Task</button>
          </form>
        </div>
      </div>
    </>
  );
};

export default AssignmentDetailOverlay;
