import React from 'react';
import '../../assets/global.css';
import '../../assets/coursepage.css'



const Announcement = ({announcementPostDate, announcementHeader, announcementDescription, type }) => {

  return (
    
    <div className={'announcement'}>
      <div>
        <h2 className='weight-600'>{announcementHeader}</h2>
      </div>
      <div className='bmar-20'>
        <p>Posted on {announcementPostDate}</p>
      </div>
      
      <div className='bmar-20'>
        <p>{announcementDescription}</p>
      </div>
      
    </div>
    
      
    
  );
}

export default Announcement;
