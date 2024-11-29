import React, { useState } from 'react';
import '../../assets/modal.css';
import '../../assets/global.css';

const NewAssignmentModal = ({ show, handleClose, handleSubmit, courseCode, setErrorMessage }) => {
  const [assignmentTitle, setAssignmentTitle] = useState('');
  const [assignmentDescription, setAssignmentDescription] = useState('');
  const [assignmentDueDate, setAssignmentDueDate] = useState('');

  const handleAssignmentSubmit = (e) => {
    e.preventDefault();
    if (!assignmentTitle || !assignmentDescription || !assignmentDueDate) {
      setErrorMessage('All fields are required.');
      return;
    }
    const assignmentDueTimestamp = new Date(assignmentDueDate).getTime();
    handleSubmit({
      assignmentTitle: assignmentTitle,
      assignmentDescription: assignmentDescription,
      assignmentDueDate: assignmentDueTimestamp
    });
  };

  // Set min and max dates for the date input
  const today = new Date().toISOString().slice(0, 16); // Current date and time
  const maxDate = new Date(new Date().setFullYear(new Date().getFullYear() + 5)).toISOString().slice(0, 16); // 5 years from now

  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2>New Assignment</h2>
        <form onSubmit={handleAssignmentSubmit}>
          <label>Title:</label>
          <input maxLength={255} minLength={1} type="text" value={assignmentTitle} onChange={(e) => setAssignmentTitle(e.target.value)} required />
          
          <label>Description:</label>
          <textarea maxLength={255} minLength={1} style={{marginBottom: '20px', maxHeight: '200px', minHeight: '50px', minWidth:'80%', maxWidth:'100%'}} value={assignmentDescription} onChange={(e) => setAssignmentDescription(e.target.value)} required />
          
          <label>Due Date:</label>
          <input type="datetime-local" value={assignmentDueDate} onChange={(e) => setAssignmentDueDate(e.target.value)} min={today} max={maxDate} required />

          <button type="submit">Create Assignment</button>
        </form>
      </div>
    </div>
  );
};

export default NewAssignmentModal;
