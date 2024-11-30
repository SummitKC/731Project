import React, { useState, useEffect } from "react";
import '../../assets/pomodoro.css';
import '../../assets/taskboard.css';
import '../../assets/professorhome.css';

const Timer = () => {
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [totalTime, setTotalTime] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [isPaused, setIsPaused] = useState(true);
  const [isSessionStarted, setIsSessionStarted] = useState(false);
  const [showInputs, setShowInputs] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const calculateTime = (time) => {
    setHours(Math.floor((time / (1000 * 60 * 60)) % 24));
    setMinutes(Math.floor((time / (1000 * 60)) % 60));
    setSeconds(Math.floor((time / 1000) % 60));
  };

  useEffect(() => {
    let interval = null;

    if (isRunning && !isPaused && totalTime > 0) {
      interval = setInterval(() => {
        setTotalTime((prevTime) => {
          if (prevTime <= 1000) {
            clearInterval(interval); // Stop the timer when it reaches 0
            setIsRunning(false);
            setIsPaused(true);
            return 0;
          }
          return prevTime - 1000; // Decrease by 1 second (1000 milliseconds)
        });
      }, 1000);
    } else if (!isRunning) {
      clearInterval(interval); // Clear the interval if not running
    }

    // Clean up interval when component unmounts or effect is re-triggered
    return () => clearInterval(interval);
  }, [isRunning, isPaused, totalTime]); // Dependency array

  useEffect(() => {
    calculateTime(totalTime); // Update the time display whenever totalTime changes
  }, [totalTime]);

  const handleStartNewSession = () => {
    handleReset();
    setShowInputs(true);
    setIsSessionStarted(false); // This should remain false until the session starts
  };
  
  const handleStartSession = () => {
    const totalMilliseconds = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
    if (totalMilliseconds <= 0) {
      alert("Please set a valid time for the session.");
      return;
    }
  
    setTotalTime(totalMilliseconds); // Set the total time for the session
    setIsRunning(true); // Start the countdown
    setIsPaused(false); // Unpause the session
    setIsSessionStarted(true); // Mark the session as started
    setShowInputs(false); // Hide input fields
  };
  const handlePauseResume = () => {
    if (isPaused) {
      setIsPaused(false); // Unpause the session
      setIsRunning(true); // Start the countdown
    } else {
      setIsPaused(true); // Pause the session
      setIsRunning(false); // Stop the countdown
    }
  };

  const handleReset = () => {
    setIsRunning(false);
    setIsPaused(true); 
    setIsSessionStarted(false);
    setShowInputs(false); 
    setTotalTime(0);
    setHours(0);
    setMinutes(0);
    setSeconds(0); 
  };

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
      strokeDashoffset: 314 * (1 - totalTime / (hours * 3600000 + minutes * 60000 + seconds * 1000)),
    }}
  />
</svg>
  <div className="time-display">
    {String(hours).padStart(2, "0")}:{String(minutes).padStart(2, "0")}:
    {String(seconds).padStart(2, "0")} <br></br>
    <h1 className="sub-text">Remaining</h1>
  </div>
</div>

      <div className="session-buttons">
          <button style={!showInputs ? {} : {display: "none"}} onClick={handleStartNewSession} className="generic-button">Start New Session</button>

        {showInputs && (
          <div className="input-section">
            <div>
            <button onClick={handleStartSession} className="generic-button">
            Start Session</button> <br></br>
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
            </div>
            <div>
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
            </div>
          </div>
        )}

    
        <button onClick={handlePauseResume} className="generic-button">
          {isRunning && !isPaused ? "Pause Session" : "Resume Session"}
        </button>

        <button onClick={handleReset} className="generic-button">
          End Session
        </button>

        <button className="generic-button">Delete Session</button>
      </div>
    </div>
  );
};

export default Timer;