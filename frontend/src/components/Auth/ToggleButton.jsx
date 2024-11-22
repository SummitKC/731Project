import React, { useState } from 'react';
import '../../assets/togglebutton.css'


const ToggleButton = ({ onToggle }) => {
  const [isProfessor, setIsProfessor] = useState(false);

  const toggleMode = () => {
    const newMode = !isProfessor;
    setIsProfessor(newMode);
    if (onToggle) onToggle(newMode ? 'Professor' : 'Student');
  };

  return (
    <>
      <div className="toggle-button-container">
        <div className={`toggle-button ${isProfessor ? 'professor' : 'student'}`} onClick={toggleMode}>
          <div className="toggle-circle" />
        </div>
        <span className="toggle-text">
          {isProfessor ? 'Professor' : 'Student'}
        </span>
      </div>
    </>
  );
};

export default ToggleButton;
