import React, { useState } from 'react';
import '../../assets/modal.css';
import '../../assets/global.css';
import '../../assets/task.css';

const CreateTaskModal = ({ show, handleClose, handleSubmit, assignmentsByCourse }) => {
  const [taskName, setTaskName] = useState('');
  const [taskStatus, setTaskStatus] = useState('TODO');
  const [taskPriority, setTaskPriority] = useState('NORMAL');
  const [taskDate, setTaskDate] = useState('');
  const [selectedAssignment, setSelectedAssignment] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleTaskSubmit = (e) => {
    e.preventDefault();
    if (!taskName || !taskStatus || !taskPriority || !taskDate || !selectedAssignment) {
      handleSubmit('All fields are required.');
      return;
    }
    
    handleSubmit({
      taskName,
      taskStatus,
      taskPriority,
      taskDate: new Date(taskDate).toISOString(),
      
    }, Number(selectedAssignment));
  };

  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2 className='bmar-20'>Create Task</h2>
        <form onSubmit={handleTaskSubmit}>
          <label>Task Name:</label>
          <input className='bmar-30' type="text" value={taskName} onChange={(e) => setTaskName(e.target.value)} required />
          
          <label>Status:</label>
          <select className='bmar-20' style={{ height:'30px' }} value={taskStatus} onChange={(e) => setTaskStatus(e.target.value)} required>
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN PROGRESS</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>

          <label>Priority:</label>
          <select className={`${taskPriority.toLowerCase()} bmar-20`} style={{ height:'30px' }} value={taskPriority} onChange={(e) => setTaskPriority(e.target.value)} required>
            <option className='low' value="LOW">LOW</option>
            <option className='normal' value="NORMAL">NORMAL</option>
            <option className='high' value="HIGH">HIGH</option>
            <option className='urgent' value="URGENT">URGENT</option>
          </select>

          <label>Date and Time:</label>
          <input className='bmar-30' type="datetime-local" value={taskDate} onChange={(e) => setTaskDate(e.target.value)} required />

          <label>Assignment:</label>
          <select className='bmar-20' style={{ height:'30px' }} value={selectedAssignment} onChange={(e) => setSelectedAssignment(e.target.value)} required>
            <option value="">Select an assignment</option>
            {Object.entries(assignmentsByCourse).map(([courseName, assignments]) => (
              assignments.map(assignment => (
                <option key={assignment.assignmentID} value={assignment.assignmentID}>
                  {courseName}: {assignment.assignmentTitle} - Due {assignment.assignmentDueDate} at {assignment.assignmentDueTime}
                </option>
              ))
            ))}
          </select>

          {errorMessage && <p className="error">{errorMessage}</p>}
          <button className='bmar-30' type="submit">Create Task</button>
        </form>
      </div>
    </div>
  );
};

export default CreateTaskModal;
