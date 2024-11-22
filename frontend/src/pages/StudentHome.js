import React, { useState } from 'react'
import '../assets/home.css';
import { useMediaQuery } from 'react-responsive'
import StudentSidebar from '../components/Common/StudentSidebar';
import Course from '../components/Course/Course';

const StudentHome= () => {

  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const isTablet = useMediaQuery({ query: '(max-width: 1224px)'});
  
  const isDesktopOrLaptop = useMediaQuery({query: '(min-width: 1224px)'})
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' })

  
  const firstName="John";
  const lastName="Doe";
  
  const initials = `${firstName[0]}${lastName[0]}`;


  
  const term = "Fall 2024"
  return (
    <div style={{ display: 'flex', flexDirection: 'row'}}> 
      <StudentSidebar firstName="John" lastName="Doe" />
      <div style={{width:'100vw'}}>
          <div id="profile-container" style={ isMobile ? {} : {display:'None'}}>
            <div className="profile-placeholder">{initials}</div>
            <div className="name">{firstName} {lastName}</div>
          </div>
          
          <div className="main-content">
            <h1 style={isDesktopOrLaptop ? {paddingTop: '30px', paddingLeft: '30px'} : {}}>Welcome to your Dashboard</h1>      
            
            <div className='dashboard-wrapper'>
                <div className='courses-box'>
                  <h2>Your Courses for {term}</h2>
                  <div className='courses-container'>
                    <Course courseCode={'CPS 510'} courseName={'Database Systems I'} courseIcon={{}}/>
                    <Course courseCode={'CPS 510'} courseName={'Database Systems I'} courseIcon={{}}/>
   
                  </div>
                  <button className='generic-button font' style={{alignSelf: 'end'}}>Join Course</button>
                </div>
                <div className='taskboard-box'>
                  <h2>Task Board</h2>
                  <div className='courses-container'>

                  </div>
                  <button className='generic-button font' style={{alignSelf: 'end'}}>Create Task</button>
                </div>
              </div>
          </div>
  
      </div>
      
    </div>
  )
}

export default StudentHome;