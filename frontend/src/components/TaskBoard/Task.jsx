import React, {useState} from 'react';
import '../../assets/task.css';
import '../../assets/global.css';
import TaskDetailOverlay from './TaskOverlayDetail';


const Task = ({ taskName, taskPriority, taskStatus, taskDate, board }) => {
  const [selectedTask, setSelectedTask] = useState(null);
  const handleTaskClick = () => { 
    setSelectedTask({taskName, taskPriority, taskStatus, taskDate});
  };
  const handleClose = () => { 
    setSelectedTask(null);
  }
  
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
    <>
      <div className={`task-wrapper weight-600`} onClick={handleTaskClick}>
        <div className='task'>
          <p className='lmar-30'>{taskName}</p>
        </div>
        {statusElement}
        <div style={ taskStatus === "Completed" ? {minWidth: '110px'} : {}} className='priority'>
          {priorityOrDateElement}
        </div>
        
      </div>
      {selectedTask && ( <TaskDetailOverlay selectedTask={selectedTask} handleClose={handleClose} /> )}
      
    </>
  );
}

export default Task;
