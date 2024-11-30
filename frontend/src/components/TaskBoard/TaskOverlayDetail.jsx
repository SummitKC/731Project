import React, { useState } from 'react';
import '../../assets/task.css';
import { parseISO, formatISO, format } from 'date-fns';

const TaskDetailOverlay = ({ selectedTask, handleClose }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [taskName, setTaskName] = useState(selectedTask?.taskName || '');
  const [taskStatus, setTaskStatus] = useState(selectedTask?.taskStatus || '');
  const [taskPriority, setTaskPriority] = useState(selectedTask?.taskPriority || '');

  const prioclass = taskPriority.toLowerCase();
  const token = localStorage.getItem('token');

  const [taskDate, setTaskDate] = useState((new Date(selectedTask.taskDate)).toISOString().split('T')[0]);
  const [taskTime, setTaskTime] = 
    useState((new Date(selectedTask.taskDate)).
    toISOString().split('T')[1].split(':')[0] + ':' + (new Date(selectedTask.taskDate)).
    toISOString().split('T')[1].split(':')[1]);
  
  const [taskDateTime, setTaskDateTime] = useState(new Date(selectedTask.taskDate));
  
  console.log(taskTime)

  const [taskFormattedTime, setTaskFormattedTime] = useState(() => {
    const [hours, minutes] = taskTime.split(':');
    const isPM = parseInt(hours) >= 12;
    const formattedHours = isPM ? parseInt(hours) - 12 : parseInt(hours);
    return `${formattedHours}:${minutes} ${isPM ? 'PM'
   : 'AM'}`;
  });
  
  const handleDelete = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/student/taskboard/tasks/${selectedTask.id}`, {
        method: 'DELETE',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        console.log('Task deleted successfully.');
        handleClose();
        window.location.reload();
      } else {
        console.error('Error deleting task:', response.statusText);
      }
    } catch (error) {
      console.error('Error deleting task:', error);
    }
  };

  const now = (new Date()).toISOString();
  

  const onCancel = async () => {
    setIsEditing(false);
  } 

  const handleComplete = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/student/taskboard/tasks/${selectedTask.id}`, {
        method: 'PUT',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          taskName: selectedTask.taskName,
          taskStatus: "COMPLETE",
          taskPriority: selectedTask.taskPriority,
          taskDate: String(now)
        })
      });

      if (response.ok) {
        console.log('Task marked as complete.');
        handleClose();
        window.location.reload();
      } else {
        console.error('Error marking task as complete:', response.statusText);
      }
    } catch (error) {
      console.error('Error marking task as complete:', error);
    }
  };

  const handleEdit = (event) => {
    event.preventDefault();
    setIsEditing(true);
  };

  const handleSave = async () => {
    try {
      const combinedDateTime = `${taskDate}T${taskTime}:00Z`;
      
      const response = await fetch(`http://localhost:8080/api/student/taskboard/tasks/${selectedTask.id}`, {
        method: 'PUT',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          taskName,
          taskStatus,
          taskPriority,
          taskDate: combinedDateTime
        })
      });

      if (response.ok) {
        console.log('Task updated successfully.');
        setIsEditing(false);
        handleClose();
        window.location.reload();
      } else {
        console.error('Error updating task:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating task:', error);
    }
  };

  if (!selectedTask) return null;

  return (
    <>
      <div className="task-detail-overlay" onClick={handleClose}></div>
      <div className="task-detail-container">
        <button className="close-button" onClick={handleClose}>&times;</button>
        <h2>Task Details</h2>
        <br></br>
        
        <form onSubmit={handleSave}>
          <label>Name:</label>
          {isEditing ? (
            <input type="text" value={taskName} onChange={(e) => setTaskName(e.target.value)} required />
          ) : (
            <span> {taskName}</span>
          )}
          <br></br>
          <br></br>
          
          <label>Status:</label>
          {(isEditing && selectedTask.taskStatus !== "COMPLETE") ? (
            <select value={taskStatus} onChange={(e) => setTaskStatus(e.target.value)} required>
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="COMPLETE">COMPLETE</option>
            </select>
          ) : (
            <span> {taskStatus}</span>
          )}
          <br></br>
          <br></br>
          
          <label>Priority:</label>
          {isEditing ? (
            <select value={taskPriority} onChange={(e) => setTaskPriority(e.target.value)} required>
              <option value="LOW">LOW</option>
              <option value="NORMAL">NORMAL</option>
              <option value="HIGH">HIGH</option>
              <option value="URGENT">URGENT</option>
            </select>
          ) : (
            <span className={`${prioclass}`}> {taskPriority}</span>
          )}
          <br></br>
          <br></br>
          
          <label>Date:</label>
          {(isEditing && selectedTask.taskStatus !== "COMPLETE") ? (
            <input type="date" value={taskDate} onChange={(e) => setTaskDate(e.target.value)} required />
          ) : ( <span> {taskDate} </span> ) }<br></br> <br></br>
          <label>Time:</label>
          
          {(isEditing && selectedTask.taskStatus !== "COMPLETE") ? (
            <input type="time" value={taskTime} onChange={(e) => setTaskTime(e.target.value)} required />
          ) : ( <span> {taskFormattedTime} </span> ) }<br></br>
          
          {isEditing ?
            (<button className='generic-button' onClick={onCancel}>Cancel</button>) : (<></>)
          }
          {isEditing ? (
            <button type="submit" className='generic-button'>Save</button>
          ) : (
            <button style={(selectedTask.taskStatus === "COMPLETE") ? {display:'none'} : {}} type="button" className='generic-button' onClick={handleEdit}>Edit Task</button>
          )}
          
          {!isEditing ? (
            <button className='generic-button' onClick={handleDelete}>Delete Task</button>
          ) : (<></>)
          
          }
            
          { !isEditing ? (<button style={(selectedTask.taskStatus === "COMPLETE") ? {display:'none'} : {}} className='generic-button' onClick={handleComplete}>Complete Task</button>) 
          : (<></>)}
          
        </form>
      </div>
    </>
  );
};

export default TaskDetailOverlay;
