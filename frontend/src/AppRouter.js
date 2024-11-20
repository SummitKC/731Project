import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import StudentHome from './pages/StudentHome';
import './assets/style.css';

function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={ <LoginPage/ > } />
        <Route path="/login" element={ <LoginPage/ > } />
        <Route path="/register" element={ <RegisterPage/ > } />
        <Route path="/student/home" element={ <StudentHome/ > } />
        
        
        
      </Routes>
    </Router>
  );
};

export default AppRouter;
