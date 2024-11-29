import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../assets/course.css';

const Course = ({ courseCode, courseName, courseIcon }) => {

  return (
    <div className='course-wrapper'>
      <div className='course'></div>
      <div className='course-text'>
        <span>{courseName}</span>
      </div>
    </div>
  );
};

export default Course;
