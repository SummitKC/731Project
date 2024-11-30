import React, { useState, useEffect } from 'react';
import '../assets/taskboard.css';
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Board from '../components/TaskBoard/Board';
import CreateTaskModal from '../components/TaskBoard/CreateTaskModal';
import { useNavigate } from 'react-router-dom';

const TaskBoard = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)' });
  const isDesktopOrLaptop = useMediaQuery({ query: '(min-width: 1224px)' });
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' });

  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  const [tasks, setTasks] = useState([]);
  const [assignmentsByCourse, setAssignmentsByCourse] = useState({});
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      // get all tasks
      fetch('http://localhost:8080/api/student/taskboard/tasks', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(data => {
        const fetchedTasks = [
          ...data.todoTasks,
          ...data.inProgressTasks,
          ...data.reviewingTasks,
          ...data.completedTasks
        ];
        setTasks(fetchedTasks);
      })
      .catch(error => console.error('Error fetching tasks:', error));
      
      // get courses
      fetch('http://localhost:8080/api/student/dashboard/courses', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(coursesData => {
        // get assignments for each course
        const fetchAssignmentsPromises = coursesData.map(course => 
          fetch(`http://localhost:8080/api/student/course/${course.courseCode}/assignments`, {
            method: 'GET',
            headers: {
              Authorization: `${token}`,
              'Content-Type': 'application/json'
            }
          })
          .then(response => response.json())
          .then(assignments => ({ courseName: course.name, courseCode: course.courseCode, assignments }))
        );
        return Promise.all(fetchAssignmentsPromises);
      })
      .then(assignmentsData => {
        const assignmentsByCourse = assignmentsData.reduce((acc, { courseName, assignments }) => {
          acc[courseName] = assignments;
          return acc;
        }, {});
        setAssignmentsByCourse(assignmentsByCourse);
      })
      .catch(error => console.error('Error fetching courses or assignments:', error));
    }
  }, [navigate]);

  // group items in 3 bins and sort them tasks based on date+time
  const groupedTasks = {};

  tasks.forEach(task => {
    const { taskDate, taskStatus, id } = task;
    const dateStr = new Date(taskDate).toISOString().slice(0, 10); // YYYY-MM-DD

    if (!groupedTasks[dateStr]) {
      groupedTasks[dateStr] = { TODO: [], 'IN_PROGRESS': [], 'COMPLETE': [] };
    }

    groupedTasks[dateStr][taskStatus].push({ ...task, key: `${dateStr}-${id}` });
  });

  const sortedDates = Object.keys(groupedTasks).sort((a, b) => new Date(a) - new Date(b));

  const convertGroupedTasks = (groupedTasks, status) => {
    return sortedDates
      .map(dateStr => ({
        taskDate: dateStr,
        tasks: groupedTasks[dateStr][status]
      }))
      .filter(group => group.tasks.length > 0);
  };

  // filter by status and make 3 arrays
  const todoTasks = convertGroupedTasks(groupedTasks, 'TODO');
  const inProgressTasks = convertGroupedTasks(groupedTasks, 'IN_PROGRESS');
  const completedTasks = convertGroupedTasks(groupedTasks, 'COMPLETE');

  const handleCreateTaskClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleSubmitCreateTask = async (taskData, assignmentID) => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      try {
        console.log(taskData);
        const response = await fetch(`http://localhost:8080/api/student/taskboard/tasks?assignment=${assignmentID}`, {
          method: 'POST',
          headers: {
            Authorization: `${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(taskData),
        });
        if (response.ok) {
          window.location.reload();
        } else {
          setErrorMessage("Error occurred while creating task");
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName={firstName} lastName={lastName} />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <div className='header-container'>
            <h1>Your Taskboard</h1>      
            <button className='generic-button' onClick={handleCreateTaskClick}>Create Task</button>
          </div>
          <div className='dashboard-wrapper'>  
            <Board title="TODO" tasks={todoTasks} />
            <Board title="IN PROGRESS" tasks={inProgressTasks} />
            <Board title="COMPLETED" tasks={completedTasks} />
          </div>
        </div>
      </div>
      <CreateTaskModal
        show={showModal}
        handleClose={handleCloseModal}
        handleSubmit={handleSubmitCreateTask}
        errorMessage={errorMessage}
        assignmentsByCourse={assignmentsByCourse}
      />
    </div>
  );
};

export default TaskBoard;
