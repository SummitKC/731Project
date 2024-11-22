import React from 'react';
import { Link } from 'react-router-dom';
import '../../assets/home.css';
import { useMediaQuery } from 'react-responsive';

const ProfessorSidebar = ({ firstName, lastName }) => {
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 600px)' });

  return (
      <div className={`sidebar-container ${isMobile ? 'bottom-nav' : ''}`}>
        <div style={isMobile ? { display:"None" } : {}} className="profile-placeholder">{initials}</div>
        <div style={isMobile ? { display:"None" } : {}} className="name">{firstName} {lastName}</div>
        <button style={isMobile ? { display:"None" } : {}} className="button">Logout</button>
        <Link className="sidebar-button" to="/professor/home">Home</Link>
        <Link className="sidebar-button" to="/archived">Archived Courses</Link>
      </div>
  );
};

export default ProfessorSidebar;
