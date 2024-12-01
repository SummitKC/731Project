import React, { useState, useEffect } from 'react';
import '../assets/studenthome.css'; // You can reuse some styling
import '../assets/global.css';
import { useMediaQuery } from 'react-responsive';
import StudentSidebar from '../components/Common/StudentSidebar';
import Analytic from '../components/Analytics/Analytic'; // Ensure the path is correct
import { useNavigate } from 'react-router-dom';

const AnalyticsPage = () => {
  const isMobile = useMediaQuery({ query: '(max-width: 700px)' });
  const firstName = localStorage.getItem('name')?.split(' ')[0] || " ";
  const lastName = localStorage.getItem('name')?.split(' ')[1] || " ";
  const initials = `${firstName[0]}${lastName[0]}`;

  const [analytics, setAnalytics] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      fetch('http://localhost:8080/api/student/analytics/dashboard', {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => response.json())
      .then(data => setAnalytics(data))
      .catch(error => console.error('Error fetching analytics:', error));
    }
  }, [navigate]);

  const analyticsData = [
    { title: "Average Session Time", data: analytics.averageSessionTime, unit : "minutes"},
    { title: "Max Session Time", data: analytics.maxSessionTime, unit : "minutes"},
    { title: "Min Session Time", data: analytics.minSessionTime, unit : "minutes"},
    { title: "Total Session Time", data: (analytics.totalSessionTime <= 60 ? analytics.totalSessionTime : analytics.totalSessionTime / 60 ), unit : (analytics.totalSessionTime > 60 ? "hours": "minutes")},
    { title: "Total Sessions", data: analytics.totalSessions, unit : ""},
    { title: "Total Tasks", data: analytics.totalTasks, unit : ""},
    { title: "Completed Tasks", data: analytics.completedTasks, unit : ""},
    { title: "Completion Rate", data: (analytics.completionRate || "0") + "%", unit : ""},
    { title: "Tasks Completed This Month", data: analytics.taskCompletedThisMonth, unit : ""}
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <StudentSidebar firstName={firstName} lastName={lastName} />
      <div style={{ width: '100vw' }}>
        <div id="profile-container" style={isMobile ? {} : { display: 'none' }}>
          <div className="profile-placeholder">{initials}</div>
          <div className="name">{firstName} {lastName}</div>
        </div>
        <div className="main-content">
          <h1 style={{ paddingTop: '30px', paddingLeft: '30px' }}>Analytics Dashboard</h1>
          <div className='analytics-container'>
            {analyticsData.map((item, index) => (
              <Analytic key={index} title={item.title} data={item.data || 0} unit={item.unit}/>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnalyticsPage;
