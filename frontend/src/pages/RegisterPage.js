import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
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
  
  const [fullName, setFullName] = useState('');
  const [id, setId] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();
  
  const handleToggle = (newType) => { 
    setUserType(newType);
    console.log(`${newType}`);
  };
  
  const handleRegister = (e) => {
    e.preventDefault();
    const endpoint = `http://localhost:8080/api/auth/${userType.toLowerCase()}/register`;

    fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email: email, password: password})
    })
    .then(response => {
      console.log('Response status:', response.status); // Debugging line
      if (response.status === 204) {
        setSuccessMessage('Registration successful! Redirecting to login...');
        setTimeout(() => {
          navigate('/');
        }, 1000);
        return; // No need to parse the response body if status is 204
      } else if (!response.ok) {
        return response.text().then(text => { throw new Error(text); });
      }
      return response.json();
    })
    .then(data => {
      if (data) {
        setSuccessMessage('Registration successful! Redirecting to login...');
        setTimeout(() => {
          navigate('/');
        }, 1000);
      }
    })
    .catch(error => {
      console.error('Fetch error:', error); // Improved error logging
      setErrorMessage(error.message || 'An error occurred. Please try again.');
    });
  };
  
  
  
  
  return (
    <div className='form-container'>
        <form className={'form'} onSubmit={handleRegister}>
          <h2>Register</h2>
          <input type="text" placeholder="Full Name" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          <input type="number" min="0" placeholder={userType === "Professor" ? "Employee ID" : "Student ID"} value={id} onChange={(e) => setId(e.target.value)} required />
          <input type="email" placeholder="School Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          <button type="submit">Register</button>
          <ToggleButton onToggle={handleToggle}/>
          {successMessage && <p className="success-message">{successMessage}</p>}
          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <p>Have an account? <a href='/'>Login</a></p>
        </form>
    </div>
  )
}

export default RegisterPage;

