import React, { useState } from 'react';
import '../../assets/course.css';

const Course = ( { courseCode, courseName, courseIcon }) => {
  
  let placeholder = '';
  
  if (courseIcon == null){
    placeholder = courseName;
  }

  return (
    <div className='course-wrapper'>
      <div className='course'></div>
      <div className='course-text'>
        <span>{courseCode} - {courseName}</span>
      </div>
    </div>
  );

}

export default Course;