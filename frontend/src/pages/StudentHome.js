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
  
  
  return (
    <div style={{ display: 'flex' }}> 
      <StudentSidebar firstName="John" lastName="Doe" />
      <div style={{ flex: 1, padding: '20px' }}>
        <h1>Welcome to your Dashboard</h1> 
        
      </div>
    </div>
  )
}

export default StudentHome;