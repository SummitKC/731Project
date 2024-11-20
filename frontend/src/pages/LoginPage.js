import React, { useState } from 'react'
import '../assets/style.css';
import { useMediaQuery } from 'react-responsive'

import ToggleButton from '../components/Auth/ToggleButton';

const LoginPage = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 600px)' });

  
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
          <input placeholder='ID or Email' required/>
          <input type="password" placeholder='Password' required/>
          <button type="submit">Login</button>
          <ToggleButton onToggle={handleToggle}/>
          <p className='font' id='register'>Don't have an account? <a href='/register'>Register</a></p>
        </form>
      
    </div>
  )
}
          
export default LoginPage;
