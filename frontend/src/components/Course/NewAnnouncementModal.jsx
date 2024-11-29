import React, { useState } from 'react';
import '../../assets/modal.css';
import '../../assets/global.css';

const NewAnnouncementModal = ({ show, handleClose, handleSubmit, courseCode, setErrorMessage }) => {
  const [announcementHeader, setAnnouncementHeader] = useState('');
  const [announcementDescription, setAnnouncementDescription] = useState('');

  const handleAnnouncementSubmit = (e) => {
    e.preventDefault();
    if (!announcementHeader || !announcementDescription) {
      setErrorMessage('All fields are required.');
      return;
    }
    const date = new Date();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const year = date.getFullYear();
    const formattedDate = `${month}/${day}/${year}`;
  
    handleSubmit({
      announcementHeader: announcementHeader,
      announcementDescription: announcementDescription,
      announcementPostDate: formattedDate // Use the formatted date
    });
  };
  

  return (
    <div className={`modal ${show ? 'show' : ''}`}>
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2>New Announcement</h2>
        <form onSubmit={handleAnnouncementSubmit}>
          <label>Header:</label>
          <input maxLength={255} minLength={1} type="text" value={announcementHeader} onChange={(e) => setAnnouncementHeader(e.target.value)} required />
          
          <label>Description:</label>
          <textarea maxLength={255} minLength={1} style={{marginBottom: '20px', maxHeight: '200px', minHeight: '50px', minWidth:'80%'}} value={announcementDescription} onChange={(e) => setAnnouncementDescription(e.target.value)} required />

          <button type="submit">Create Announcement</button>
        </form>
      </div>
    </div>
  );
};

export default NewAnnouncementModal;
