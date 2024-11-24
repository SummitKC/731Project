import React, { useState, useEffect }from 'react';
import { Link } from 'react-router-dom';
import '../../assets/sidebar.css';
import { useMediaQuery } from 'react-responsive';

const StudentSidebar = ({ firstName, lastName }) => {
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });

  const [isCollapsed, setIsCollapsed] = useState(false);
  const isCollapseNeeded = useMediaQuery({ query: '(max-width: 1400px)'});
  
  useEffect(() => { 
    if (isCollapseNeeded) { 
      setIsCollapsed(true);
    } else {
      setIsCollapsed(false);
      }
    }, [isCollapseNeeded]);
  
  const toggleSidebar = () => {
    setIsCollapsed(!isCollapsed);
  };


  return (
      <div className='sidebar-wrapper'>
        <button onClick={toggleSidebar} className='collapse-button' style={isMobile ? { display: 'none'} : { }}>
          {isCollapsed ? '>>': '<<'}
        </button>
        
        <div className={`sidebar-container ${(isCollapsed && !isMobile) ? 'hidden' : ''} ${isMobile ? 'bottom-nav' : ''}`}> 
          <div style={isMobile ? { display:"None" } : {}} className="profile-placeholder">{initials}</div>
          <div style={isMobile ? { display:"None" } : {}} className="name">{firstName} {lastName}</div>
          <button style={isMobile ? { display:"None" } : {}} className="button">Logout</button>
          <Link className="sidebar-button" to="/student/home">Home</Link>
          <Link className="sidebar-button" to="/taskboard">Task Board</Link>
          <Link className="sidebar-button" to="/pomodoro">Pomodoro</Link>
          <Link className="sidebar-button" to="/analytics">Analytics</Link>
        </div>
      </div>
  );
};

export default StudentSidebar;

