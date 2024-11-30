import React, { useState } from 'react';
import '../../assets/task.css';
import '../../assets/global.css';
import AssignmentDetailOverlay from './AssignmentDetailOverlay';

const Assignment = ({ assignmentID, assignmentName, assignmentDueDate, assignmentDueTime }) => {
  const [selectedAssignment, setSelectedAssignment] = useState(null);

  const handleAssignmentClick = () => {
    setSelectedAssignment({ assignmentID, assignmentName, assignmentDueDate, assignmentDueTime });
  };

  const handleClose = () => {
    setSelectedAssignment(null);
  };
  
  return (
    <>
      <div className={`task-wrapper weight-600`} onClick={handleAssignmentClick}>
        <div className='task'>
          <p className='lmar-30'>{assignmentName}</p>
        </div>
        <div style={{ width: '90px' }} className='priority high'>
          <p>{assignmentDueTime}</p>
        </div>
      </div>
      {selectedAssignment && (
        <AssignmentDetailOverlay
          selectedAssignment={selectedAssignment}
          handleClose={handleClose}
        />
      )}
    </>
  );
};

export default Assignment;
