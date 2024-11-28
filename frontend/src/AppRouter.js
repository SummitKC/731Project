import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import StudentHome from './pages/StudentHome';
import ProfessorHome from './pages/ProfessorHome';
import TaskBoard from './pages/TaskBoard';
import CoursePageStudent from './pages/CoursePageStudent';
import PrivateRoute from './PrivateRoute';
import './assets/style.css';

function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route element={<PrivateRoute allowedTypes={['Student']} />}>
          <Route path="/student/home" element={<StudentHome />} />
        </Route>
        <Route element={<PrivateRoute allowedTypes={['Professor']} />}>
          <Route path="/professor/home" element={<ProfessorHome />} />
        </Route>
        <Route path="/taskboard" element={<TaskBoard />} />
        <Route path="/course/:courseCode" element={<CoursePageStudent />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
