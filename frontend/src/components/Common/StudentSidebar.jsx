import React from 'react';
import { Link } from 'react-router-dom';
import '../../assets/home.css';

const StudentSidebar = ({ firstName, lastName }) => {
  const initials = `${firstName[0]}${lastName[0]}`;

  return (
    <div className="sidebar-container">
      <div className="profile-placeholder">{initials}</div>
      <div className="name">{firstName} {lastName}</div>
      <button className="button">Logout</button>
      <Link className="sidebar-button" to="/studenthome">Home</Link>
      <Link className="sidebar-button" to="/taskboard">Task Board</Link>
      <Link className="sidebar-button" to="/pomodoro">Pomodoro</Link>
      <Link className="sidebar-button" to="/analytics">Analytics</Link>
    </div>
  );
};

export default StudentSidebar;
