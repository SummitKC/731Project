import React from 'react';
import Task from './Task';
import '../../assets/task.css';

const Board = ({ title, tasks, board }) => {
  return (
    <div className='main-taskboard-box'>
      <h2>{title}</h2>
      {tasks.map((task, index) => (
        <div className='tasks-container' key={task.taskDate}>
          <h3 className='task-header'>{task.taskDate}</h3>
          {task.tasks.map((task, idx) => (
            <Task
              key={idx}
              id={task.id}
              taskName={task.taskName}
              taskPriority={task.taskPriority}
              taskStatus={task.taskStatus}
              taskDate={task.taskDate}
              board={board}
            />
          ))}
        </div>
      ))}
    </div>
  );
};

export default Board;
