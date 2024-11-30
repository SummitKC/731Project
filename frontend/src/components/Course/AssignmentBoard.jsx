import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Assignment from '../Course/Assignment';
import '../../assets/coursepage.css';
import NewAssignmentModal from '../Course/NewAssignmentModal';


const AssignmentBoard = ({ assignments, title, type, courseCode, fetchCourseData}) => {
  const [showAssignmentModal, setShowAssignmentModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  
  const groupedAssignments = assignments.reduce((acc, assignment) => {
    const { assignmentDueDate } = assignment;
    if (!acc[assignmentDueDate]) {
      acc[assignmentDueDate] = [];
    }
    acc[assignmentDueDate].push(assignment);
    return acc;
  }, {});
  
  const navigate = useNavigate();

  const sortedDates = Object.keys(groupedAssignments).sort((a, b) => new Date(a) - new Date(b));

  const handleNewAssignment = (data) => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
    console.log(data);
    fetch(`http://localhost:8080/api/professor/course/${courseCode}/assignment`, {
      method: 'POST',
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
      console.log('New assignment created:', result);
      setShowAssignmentModal(false);
      fetchCourseData();
    })
    .catch(error => {
      console.error('Error creating assignment:', error);
      setErrorMessage('Error creating assignment. Please try again.');
    });
    }
  };

  return (
    <div className='board-wrapper'>
      <h2>Assignments</h2>
      <div className='board-box'>     
        <button style={type === 'professor' ? 
          { display: 'flex', alignItems: 'center', marginBottom: '0px', alignSelf: 'flex-end'} : { display: 'none' }}
          className='generic-button' onClick={() => setShowAssignmentModal(true)}>New Assignment</button>
        <br></br>
        {sortedDates.map((date) => (
          <div className='tasks-container' key={date}>
            <h3 className='task-header'>{date}</h3>
            {groupedAssignments[date].map((assignment, idx) => (
              <Assignment
                key={idx}
                assignmentID={assignment.assignmentID}
                assignmentName={assignment.assignmentTitle}
                assignmentDueDate={assignment.assignmentDueDate}
                assignmentDueTime={assignment.assignmentDueTime}
                type={type}
              />
            ))}
          </div>
        ))}
      </div>
      <NewAssignmentModal
        show={showAssignmentModal}
        handleClose={() => setShowAssignmentModal(false)}
        handleSubmit={handleNewAssignment}
        courseCode={courseCode}
        setErrorMessage={setErrorMessage}
      />
    </div>
  );
};

export default AssignmentBoard;
