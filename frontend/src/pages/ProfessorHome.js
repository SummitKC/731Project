import React, { useState } from 'react'
import '../assets/professorhome.css';
import { Link } from 'react-router-dom';
import { useMediaQuery } from 'react-responsive'
import ProfessorSidebar from '../components/Common/ProfessorSidebar';

const ProfessorHome= () => {
  const isDesktopOrLaptop = useMediaQuery({query: '(min-width: 1224px)'})
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' })
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' })
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' })
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' })
  
  const firstName="John";
  const lastName="Doe";
  
  const initials = `${firstName[0]}${lastName[0]}`;
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  
  return (
    <div style={{ display: 'flex', flexDirection: 'row'}}> 
      <ProfessorSidebar firstName="Jane" lastName="Doe" />
      <div style={{width:'100vw'}}>
      <div id="profile-container" style={ isMobile ? {} : {display:'None'}}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        
        <div className="main-container">
          <div className='header-container'>
          <h1 style={isMobile ? {} : {paddingTop: '10px', paddingLeft: '30px'}}>Welcome to your Dashboard</h1>    
          <Link className="generic-button" to="/professor/createCourse">Create Course</Link>   
          </div>
        </div>

      </div>
      
    </div>
  )
}

export default ProfessorHome;
