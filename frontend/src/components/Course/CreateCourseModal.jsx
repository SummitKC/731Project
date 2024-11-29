import React from 'react';
import '../../assets/modal.css';
import '../../assets/global.css';


const CreateCourseModal = (
  { show, 
    handleClose, 
    handleSubmitCreateCourse, 
    courseCode, 
    setCourseCode, 
    courseName, 
    setCourseName, 
    year, 
    setYear, 
    term, 
    setTerm, 
    setErrorMessage,
    errorMessage }) => {
  
  const handleSubmit = (e) => {
    e.preventDefault();

    if (Number(year) < 2023 || Number(year) > 2040){
      setErrorMessage('Please Enter a valid year.');
      return;
    }
    
    if (String(term) === " " || !(String(term) === "FALL" || String(term) === "WINTER" || String(term) === "SPRING")){
      setErrorMessage('Invalid Term');
      
    }
    
    handleSubmitCreateCourse(e);
  };
  
  
  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2>Create Course</h2>
        <form onSubmit={handleSubmit}>
          <label>Unique Course Code:</label>
          <input maxLength={255} minLength={1} type="text" value={courseCode} onChange={(e) => setCourseCode(e.target.value)} required />
          <label>Course Name:</label>
          <input maxLength={255} minLength={1} type="text" value={courseName} onChange={(e) => setCourseName(e.target.value)} required />
          
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
