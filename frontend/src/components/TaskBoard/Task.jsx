import React, { useState } from 'react';
import '../../assets/task.css';
import '../../assets/global.css';


const Task = ( { taskName, taskPriority, taskStatus, taskDate, taskBoard }) => {
  
  const priorityClass = taskPriority.toLowerCase();
    
  return (
    <div className={`task-wrapper weight-600`}>
        <div className='task'>
          <p className='lmar-30'>{taskName}</p>
        </div>
        <div className='status'>
          { {taskBoard} ? <p>{taskStatus}</p> : <></>}
        </div>
        <div className='priority'>
          <p className={`rmar-30 ${priorityClass}`}>{taskPriority}</p>
        </div>
    </div>
  );

}

export default Task;