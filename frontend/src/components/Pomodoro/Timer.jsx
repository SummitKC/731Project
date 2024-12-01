import React, { useState, useEffect } from "react";
import '../../assets/pomodoro.css';
import '../../assets/taskboard.css';
import '../../assets/professorhome.css';
import { useNavigate } from 'react-router-dom';

const Timer = ({ tasks }) => {
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [totalTime, setTotalTime] = useState(0);
  const [timeLeft, setTimeLeft] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [isPaused, setIsPaused] = useState(true);
  const [isSessionStarted, setIsSessionStarted] = useState(false);
  const [showInputs, setShowInputs] = useState(false);
  const [isOvertime, setIsOvertime] = useState(false);
  const [overtimeSeconds, setOvertimeSeconds] = useState(0);
  const [selectedTaskID, setSelectedTaskID] = useState('');
  
  const [selectedTask, setSelectedTask] = useState({
    "id": 0,
    "taskName": "",
    "taskStatus": "",
    "taskPriority": "",
    "taskDate": ""
  });

  const [incrementsCompleted, setIncrementsCompleted] = useState(0);
  const [shortBreak, setShortBreak] = useState(false);
  const [longBreak, setLongBreak] = useState(false);

  const navigate = useNavigate();
  
  const calculateTime = (time) => {
    setHours(Math.floor((time / (1000 * 60 * 60)) % 24));
    setMinutes(Math.floor((time / (1000 * 60)) % 60));
    setSeconds(Math.floor((time / 1000) % 60));
  };

  const playNotificationSound = () => {
    const audio = new Audio(process.env.PUBLIC_URL + '/notification_sound.wav');
    audio.play();
  };

  const token = localStorage.getItem('token');

  useEffect(() => {
    let interval = null;

    if (isRunning && !isPaused) {
      if (timeLeft > 0) {
        interval = setInterval(() => {
          setTimeLeft((prevTime) => prevTime - 1000);
        }, 1000);
      } else {
        if (!isOvertime) {
          setIsOvertime(true);
          playNotificationSound();
        }
        interval = setInterval(() => {
          setOvertimeSeconds((prevTime) => prevTime + 1);
        }, 1000);
      }
    } else if (!isRunning) {
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [isRunning, isPaused, timeLeft, isOvertime]);

  useEffect(() => {
    calculateTime(timeLeft);
  }, [timeLeft]);

  useEffect(() => {
    if (isOvertime) {
      calculateTime(overtimeSeconds * 1000);
    }
  }, [overtimeSeconds, isOvertime]);

  const handleStartNewSession = () => {
    handleReset();
    setShowInputs(true);
    setIsSessionStarted(false);
  };

  const getTask = (taskID) => {
    const token = localStorage.getItem('token');
  
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      fetch(`http://localhost:8080/api/student/taskboard/tasks/${taskID}`, {
        method: 'GET',
        headers: {
          Authorization: `${token}`,
          'Content-Type': 'application/json',
        },
      })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        console.log('Task data fetched:', data);
        setSelectedTask(data);
      })
      .catch(error => {
        console.error('Error fetching task:', error);
      });
    }
  };
  

  const handleStartSession = async () => {
    if (!selectedTaskID) {
      alert("Please select a task before starting the session.");
      return;
    }
    const totalMilliseconds = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
    if (totalMilliseconds <= 0) {
      alert("Please set a valid time for the session.");
      return;
    }
  
    setTotalTime(totalMilliseconds);
    setTimeLeft(totalMilliseconds);
  
    const mins = totalMilliseconds >= 6000 ? totalMilliseconds / 60000 : totalMilliseconds / 1000;
    setConvertedTime(mins);
  
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      try {
        const response = await fetch(`http://localhost:8080/api/student/pomodoro/start/${selectedTaskID}?mins=${mins}`, {
          method: 'POST',
          headers: {
            Authorization: `${token}`,
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log('Pomodoro session started:', data);
        await getTask(selectedTaskID);
        console.log(selectedTask);
      } catch (error) {
        console.error('Error starting pomodoro session:', error);
      }
    }
  
    setIsRunning(true);
    setIsPaused(false);
    setIsSessionStarted(true);
    setShowInputs(false);
  };
  

  const handlePauseResume = () => {
    if (isPaused) {
      setIsPaused(false);
      setIsRunning(true);
      
      if (!token) {
        console.log('Expired or bad token, login again!');
        navigate('/login');
      } else {
        fetch(`http://localhost:8080/api/student/pomodoro/resume/${selectedTaskID}`,
          { 
            method: 'POST', 
            headers: 
            { 
              Authorization: `${token}`,
              'Content-Type': 'application/json',
            },
            })
            .then(response => { 
              if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`); }
                return response.json(); 
            })
            .then(data => { 
              console.log('session resumed:', data);
            })   
            .catch(error => { 
              console.error('Error resuming pomodoro session:', error);  
            })
      }
      
    } else {
      setIsPaused(true);
      setIsRunning(false);
      
      if (!token) {
        console.log('Expired or bad token, login again!');
        navigate('/login');
      } else {
        fetch(`http://localhost:8080/api/student/pomodoro/pause/${selectedTaskID}`,
          { 
            method: 'POST', 
            headers: 
            { 
              Authorization: `${token}`,
              'Content-Type': 'application/json',
            },
            })
            .then(response => { 
              if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`); }
                return response.json(); 
            })
            .then(data => { 
              console.log('session paused:', data);
            })   
            .catch(error => { 
              console.error('Error pausing pomodoro session:', error);  
            })
      }
      
    }
  };

  const handleReset = () => {
    setIsRunning(false);
    setIsPaused(true);
    setIsSessionStarted(false);
    setShowInputs(false);
    setTotalTime(0);
    setTimeLeft(0);
    setOvertimeSeconds(0);
    setIsOvertime(false);
    setHours(0);
    setMinutes(0);
    setSeconds(0);
    setIncrementsCompleted(0);
    setShortBreak(false);
    setLongBreak(false);
  };

  const startShortBreak = () => {
    setTimeLeft(5 * 60 * 1000);
    setIsRunning(true);
    setIsPaused(false);
  };

  const startLongBreak = () => {
    setTimeLeft(15 * 60 * 1000);
    setIsRunning(true);
    setIsPaused(false);
  };

  const handlePomodoroEnd = () => {
    /* if (incrementsCompleted === 3) {
      setLongBreak(true);
      setIncrementsCompleted(0); // Reset Pomodoro count after long break
    } else {
      setShortBreak(true);
    }

    setIncrementsCompleted(incrementsCompleted + 1); */

    if (!token) {
      console.log('Expired or bad token, login again!');
      navigate('/login');
    } else {
      fetch(`http://localhost:8080/api/student/pomodoro/end/${selectedTaskID}`,
        { 
          method: 'POST', 
          headers: 
          { 
            Authorization: `${token}`,
            'Content-Type': 'application/json',
          },
          })
          .then(response => { 
            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`); }
              return response.json(); 
          })
          .then(data => { 
            console.log('Pomodoro session ended:', data);
          })   
          .catch(error => { 
            console.error('Error starting pomodoro session:', error);  
          })
    }
    handleReset();
    setSelectedTask('')
    
  };

  // Calculate circle progress and color
  const totalSessionTime = hours * 3600000 + minutes * 60000 + seconds * 1000;
  const progress = totalTime > 0 ? (1099 * timeLeft) / totalTime : 0;
  const circleColor = timeLeft / totalTime > 0.5 ? 'green' : timeLeft / totalTime > 0.1 ? 'yellow' : 'red';

  const [convertedTime, setConvertedTime] = useState(totalTime);
  
  

  return (
    <div className="timer">
      <div className="circle-container">
        <svg>
          <circle className="base-circle" cx="50%" cy="50%" r="175" />
          <circle
            className="progress-circle"
            cx="50%"
            cy="50%"
            r="175"
            style={{
              strokeDashoffset: 1099 - progress, // Ensure it progresses counterclockwise
              stroke: circleColor,
            }}
          />
        </svg>
        <div className={`time-display ${isOvertime ? 'overtime' : ''}`}>
          {String(hours).padStart(2, "0")}:{String(minutes).padStart(2, "0")}:{String(seconds).padStart(2, "0")}
          <br />
          <h1 className="sub-text">{isOvertime ? 'Overtime' : 'Remaining'}</h1>
        </div>
      </div>
      <h4 style={isSessionStarted ? {} : {display:'none'}} className="font">
      <strong>{convertedTime} min</strong> session for <strong>{selectedTask.taskName}</strong>
      </h4>
      <div className="session-buttons">
        <button style={!showInputs ? {} : { display: "none" }} onClick={handleStartNewSession} className="generic-button">
          Start New Session
        </button>

        {showInputs && (
          <div className="input-section">
            <div>
              <label className="user-input">
                Hours:
                <input
                  type="number"
                  min="0"
                  max="23"
                  value={hours}
                  onChange={(e) => setHours(Number(e.target.value))}
                />
              </label>
              <label className="user-input">
                Minutes:
                <input
                  type="number"
                  min="0"
                  max="59"
                  step={5}
                  value={minutes}
                  onChange={(e) => setMinutes(Number(e.target.value))}
                />
              </label>

              <label>Select a Task to work on:</label>
              <br />
              <select value={selectedTaskID} onChange={(e) => setSelectedTaskID(e.target.value)} className="task-select">
                <option value="" disabled>Select a Task</option>
                {tasks.map(task => (
                  <option key={task.id} value={task.id}>{task.taskName}</option>
                ))}
              </select>
              <br />
              <button onClick={handleStartSession} className="generic-button">
                Start Session
              </button>
            </div>
          </div>
        )}

        <button style={isSessionStarted ? {} : {display:'none'}} onClick={handlePauseResume} className="generic-button">
          {isRunning && !isPaused ? "Pause Session" : "Resume Session"}
        </button>

        <button style={isSessionStarted ? {} : {display:'none'}} onClick={handlePomodoroEnd} className="generic-button">
          End Session (Recorded)
        </button>
        <button style={isSessionStarted ? {} : {display:'none'}} onClick={handleReset} className="generic-button">
          Delete Session
        </button>
      </div>
    </div>
  );
};

export default Timer;
