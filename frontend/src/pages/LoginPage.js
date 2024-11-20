import React, { useState } from 'react'
import '../assets/style.css';
import { useMediaQuery } from 'react-responsive'

import ToggleButton from '../components/Auth/ToggleButton';

const LoginPage = () => {
  const isDesktopOrLaptop = useMediaQuery({query: '(min-width: 1224px)'})
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' })
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' })
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' })
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' })
  
  const [userType, setUserType] = useState('Student');
  
  const handleToggle = (newType) => { 
    setUserType(newType);
    console.log(`${newType}`);
  };
  const handleSubmit = (event) => { 
    event.preventDefault();
    console.log(`Logging in as a ${userType}`);
  }
  return (
    <div className={'form-container'}>
        <form className={'form'}>
          <h2>Login</h2>
          <input type="email" placeholder='ID or Email' required/>
          <input type="password" placeholder='Password' required/>
          <button type="submit">Login</button>
          <ToggleButton onToggle={handleToggle}/>
          <p className='font' id='register'>Don't have an account? <a href='/register'>Register here</a></p>
        </form>
      
    </div>
  )
}
          
export default LoginPage;
