import React, { useState } from 'react'
import '../assets/style.css';
import { useMediaQuery } from 'react-responsive'
import ToggleButton from '../components/Auth/ToggleButton';


const RegisterPage = () => {
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
  
  return (
    <div className='form-container'>
        <form className={'form'}>
          <h2>Register</h2>
          <input type="text" placeholder='Full Name' required/>
          <input type="number" min="0" placeholder={(userType=="Professor") ? 'Employee ID' : 'Student ID'} required/>
          <input type="email" placeholder='School Email' required/>
          <input type="password" placeholder='Password' required/>
          
          <ToggleButton onToggle={handleToggle}/>

          <button type="submit">Register</button>
          <p>Have an account? <a href='/'>Login</a></p>
        </form>
    </div>
  )
}

export default RegisterPage;

