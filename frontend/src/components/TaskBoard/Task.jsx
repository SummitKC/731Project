import React, {useState} from 'react';
import '../../assets/task.css';
import '../../assets/global.css';
import TaskDetailOverlay from './TaskOverlayDetail';


const Task = ({ id, taskName, taskPriority, taskStatus, taskDate, board }) => {
  const [selectedTask, setSelectedTask] = useState(null);
  const handleTaskClick = () => { 
    setSelectedTask({id, taskName, taskPriority, taskStatus, taskDate});
  };
  const handleClose = () => { 
    setSelectedTask(null);
  }
  
  const priorityClass = taskPriority.toLowerCase();

  const [formattedStatus, setFormattedStatus] = useState(taskStatus);
  const [formattedDate, setTaskDate] = useState((new Date(taskDate)).toISOString().split('T')[0]);
  
  const [taskTime, setTaskTime] = 
    useState((new Date(taskDate)).
    toISOString().split('T')[1].split(':')[0] + ':' + (new Date(taskDate)).
    toISOString().split('T')[1].split(':')[1]);
  
  const [taskFormattedTime, setTaskFormattedTime] = useState(() => {
    const [hours, minutes] = taskTime.split(':');
    const isPM = parseInt(hours) >= 12;
    const formattedHours = isPM ? parseInt(hours) - 12 : parseInt(hours);
    return `${formattedHours}:${minutes} ${isPM ? 'PM'
   : 'AM'}`;
  });
  
  const [formattedDateTime, setFormattedDateTime] = useState(formattedDate + " " + taskFormattedTime);
  
  
  if (formattedStatus === 'IN_PROGRESS'){
    setFormattedStatus('IN PROGRESS')
  }

  let statusElement = null;
  if (board === 'true') {
    statusElement = <div className='status'><p>{formattedStatus}</p></div>;
  }
  
  

  let priorityOrDateElement = null;
  if (taskStatus === 'COMPLETE') {
    priorityOrDateElement = <p className='rmar-30'>{formattedDateTime}</p>;
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
        <div style={ taskStatus === "COMPLETE" ? {minWidth: '110px'} : {}} className='priority'>
          {priorityOrDateElement}
        </div>
        
      </div>
      {selectedTask && ( <TaskDetailOverlay selectedTask={selectedTask} handleClose={handleClose} /> )}
      
    </>
  );
}

export default Task;
