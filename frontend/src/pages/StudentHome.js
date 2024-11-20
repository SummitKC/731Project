import React, { useState } from 'react'
import '../assets/home.css';
import { useMediaQuery } from 'react-responsive'
import StudentSidebar from '../components/Common/StudentSidebar';

const StudentHome= () => {
  const isDesktopOrLaptop = useMediaQuery({query: '(min-width: 1224px)'})
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' })
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' })
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' })
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' })
  
  const firstName="John";
  const lastName="Doe";
  
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 600px)' });
  
  return (
    <div style={{ display: 'flex', flexDirection: 'row'}}> 
      <StudentSidebar firstName="John" lastName="Doe" />
      <div >
      <div id="profile-container" style={ isMobile ? {} : {display:'None'}}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-content">
          <h1 style={isMobile ? {} : {paddingTop: '10px', paddingLeft: '30px'}}>Welcome to your Dashboard</h1>      
        </div>
        
      
      </div>
      
    </div>
  )
}

export default StudentHome;