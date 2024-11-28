import React from 'react';
import '../../assets/modal.css';
import '../../assets/studenthome.css';

import '../../assets/global.css';


const Modal = ({ show, handleClose, handleSubmit, courseCode, setCourseCode, errorMessage }) => {
  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2 className='bmar-20'>Join a Course</h2>
        <form onSubmit={handleSubmit}>
          <label>Course Code:</label>
            <input
              type="text"
              value={courseCode}
              onChange={(e) => setCourseCode(e.target.value)}
              required
            />
          
          {errorMessage && <p className="error">{errorMessage}</p>}
          <button className="modal-button" type="submit">Join Course</button>
        </form>
      </div>
    </div>
  );
}

export default Modal;
