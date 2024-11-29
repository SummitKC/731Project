import React, { useState } from 'react';
import '../assets/style.css';
import { useMediaQuery } from 'react-responsive';
import { useNavigate } from 'react-router-dom';
import ToggleButton from '../components/Auth/ToggleButton';

const LoginPage = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });

  const [userType, setUserType] = useState('Student');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const handleToggle = (newType) => {
    setUserType(newType);
    console.log(`${newType}`);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log(`Logging in as a ${userType}`);

    const loginData = {
      email: email,
      password: password
    };

    try {
      const response = await fetch(`http://localhost:8080/api/auth/${userType.toLowerCase()}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
      });

      if (response.ok) {
        const token = await response.text();
        
        // storing token and user types in local storage
        localStorage.setItem('token', `Bearer ${token}`);
        localStorage.setItem('userType', userType);
 
        navigate(`/${userType.toLowerCase()}/home`);
      } else {
        const data = await response.json();
        setError(data.message || 'Login failed');
      }
    } catch (error) {
      console.error('Error during login:', error);
      setError('An unexpected error occurred');
    }
  };


  return (
    <div className={'form-container'}>
      <form className={'form'} onSubmit={handleSubmit}>
        <h2>Login</h2>
        <input 
          placeholder='ID or Email'
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input 
          type="password"
          placeholder='Password'
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
        <ToggleButton onToggle={handleToggle} />
        {error && <p className='error'>{error}</p>}
        <p className='font' id='register'>Don't have an account? <a href='/register'>Register</a></p>
      </form>
    </div>
  );
};

export default LoginPage;
