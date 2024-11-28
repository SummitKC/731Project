import React, {useState} from 'react';
import '../../assets/task.css';
import '../../assets/global.css';

import TaskDetailOverlay from '../TaskBoard/TaskOverlayDetail';


const Assignment = ({assignmentName , assignmentDueDate, assignmentDueTime, type }) => {
  const [selectedAssignment, setSelectedAssignment] = useState(null);
  const handleAssignmentClick = () => { 
    setSelectedAssignment({assignmentName, assignmentDueDate, assignmentDueTime, type});
  };
  const handleClose = () => { 
    selectedAssignment(null);
  }
  
  const typeClass = type.toLowerCase();

  
  return (
    <>
      <div className={`task-wrapper weight-600`} onClick={selectedAssignment}>
        <div className='task'>
          <p className='lmar-30'>{assignmentName}</p>
   
        </div>
        <div style={{width: '90px'}} className='priority high'> 
          <p>{assignmentDueTime}</p>
        </div>

      </div>
      
      
    </>
  );
}

export default Assignment;
