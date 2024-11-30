import React from 'react';
import '../../assets/analytics.css'; // Ensure you create this CSS file for styling
import '../../assets/global.css'; // Ensure you create this CSS file for styling

const Analytic = ({ title, data, unit }) => (
  <div className="analytic font">
    <h1 className='font'>{data} <p>{unit}</p></h1>
    <p className='font'>{title}</p>
  </div>
);

export default Analytic;
