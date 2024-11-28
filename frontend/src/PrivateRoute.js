// components/PrivateRoute.js
import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = ({ allowedTypes }) => {
  const token = localStorage.getItem('token');
  const userType = localStorage.getItem('userType');

  if (!token) {
    console.log('No token found. Redirecting to /login');
    return <Navigate to="/login" />;
  }

  if (!allowedTypes.includes(userType)) {
    console.log('User type not allowed. Redirecting to /login');
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};

export default PrivateRoute;
