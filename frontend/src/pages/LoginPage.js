import React, { useState } from 'react'
import '../assets/style.css';
import { useMediaQuery } from 'react-responsive'

const LoginPage = () => {
  const isDesktopOrLaptop = useMediaQuery({query: '(min-width: 1224px)'})
  const isBigScreen = useMediaQuery({ query: '(min-width: 1824px)' })
  const isTabletOrMobile = useMediaQuery({ query: '(max-width: 1224px)' })
  const isPortrait = useMediaQuery({ query: '(orientation: portrait)' })
  const isRetina = useMediaQuery({ query: '(min-resolution: 2dppx)' })
  
  
  const [isProfessor, setIsProfessor] = useState(false);
  
  return (
    <div>
        <form className={'form'}>
          <h2>Login</h2>
          <input type="email" placeholder='School Email' required/>
          <input type="password" placeholder='Password' required/>
          <button onClick={()=>setIsProfessor(!isProfessor)}>
          {isProfessor ? 'Professor' : 'Student'}
        </button>
          <button type="submit">Login as a {isProfessor ? 'Professor' : 'Student'}</button>
        </form>
    </div>
  )
}

export default LoginPage;