import React from 'react';
import Assignment from '../Course/Assignment'; // Adjust the path if necessary
import '../../assets/coursepage.css';


const AssignmentBoard = ({ assignments, title, type }) => {
  // Group assignments by due date
  const groupedAssignments = assignments.reduce((acc, assignment) => {
    const { assignmentDueDate } = assignment;
    if (!acc[assignmentDueDate]) {
      acc[assignmentDueDate] = [];
    }
    acc[assignmentDueDate].push(assignment);
    return acc;
  }, {});

  const sortedDates = Object.keys(groupedAssignments).sort((a, b) => new Date(a) - new Date(b));
  console.log(type);
  return (
    <div className='board-wrapper'>
      <h2>Assignments</h2>
      
      <div className='board-box'>     
        <button style={type =='professor' ? 
          { display: 'flex', alignItems: 'center', marginBottom: '0px', alignSelf: 'flex-end'} : { display: 'none' }}
          className='generic-button'>New Assignment</button>
        <br></br>
        {sortedDates.map((date) => (
          <div className='tasks-container' key={date}>
            <h3 className='task-header'>{date}</h3>
            {groupedAssignments[date].map((assignment, idx) => (
              <Assignment
                key={idx}
                assignmentName={assignment.assignmentTitle}
                assignmentDueDate={assignment.assignmentDueDate}
                assignmentDueTime={assignment.assignmentDueTime}
                type={type}
              />
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default AssignmentBoard;
