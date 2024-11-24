import React from 'react';
import '../../assets/task.css';
import '../../assets/global.css';

const Task = ({ taskName, taskPriority, taskStatus, taskDate, board }) => {
  const priorityClass = taskPriority.toLowerCase();

  let statusElement = null;
  if (board === 'true') {
    statusElement = <div className='status'><p>{taskStatus}</p></div>;
  }

  let priorityOrDateElement = null;
  if (taskStatus === 'Completed') {
    priorityOrDateElement = <p className='rmar-30'>{taskDate}</p>;
  } else {
    priorityOrDateElement = <p className={`rmar-30 ${priorityClass}`}>{taskPriority}</p>;
  }

  return (
    <div className={`task-wrapper weight-600`}>
      <div className='task'>
        <p className='lmar-30'>{taskName}</p>
      </div>
      {statusElement}
      <div style={ taskStatus === "Completed" ? {minWidth: '110px'} : {}} className='priority'>
        {priorityOrDateElement}
      </div>
    </div>
  );
}

export default Task;
