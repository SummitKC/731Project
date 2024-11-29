import React from 'react';
import '../../assets/modal.css';
import '../../assets/global.css';


const CreateCourseModal = ({ show, handleClose, handleSubmit, courseCode, setCourseCode, courseName, setCourseName, year, setYear, term, setTerm, errorMessage }) => {
  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2>Create Course</h2>
        <form onSubmit={handleSubmit}>
          <label>Course Code:</label>
          <input type="text" value={courseCode} onChange={(e) => setCourseCode(e.target.value)} required />
          <label>Course Name:</label>
          <input type="text" value={courseName} onChange={(e) => setCourseName(e.target.value)} required />
          
          <label>Year:</label>
          <input type="number" value={year} onChange={(e) => setYear(e.target.value)} required />
         
          <label>Term:</label>
          <select style={{height:'30px'}} className='bmar-30' value={term} onChange={(e) => setTerm(e.target.value)} required>
              <option value="FALL">FALL</option>
              <option value="WINTER">WINTER</option>
              <option value="SPRING">SPRING/SUMMER</option>
            </select>
          {errorMessage && <p className="error bmar-30">{errorMessage}</p>}
          <button type="submit">Create Course</button>
        </form>
      </div>
    </div>
  );
};

export default CreateCourseModal;
