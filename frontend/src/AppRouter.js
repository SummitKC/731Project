import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import './assets/style.css';

function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={ <LoginPage/ > } />
        <Route path="/login" element={ <LoginPage/ > } />
        <Route path="/register" element={ <RegisterPage/ > } />
        
        
      </Routes>
    </Router>
  );
};

export default AppRouter;