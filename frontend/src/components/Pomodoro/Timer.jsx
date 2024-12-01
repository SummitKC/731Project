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

  const [incrementsCompleted, setIncrementsCompleted] = useState(0);
  const [shortBreak, setShortBreak] = useState(false);
  const [longBreak, setLongBreak] = useState(false);

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
            clearInterval(interval);
            setIsRunning(false);
            setIsPaused(true);

            // If it's time for a break, handle it
            if (shortBreak) {
              setShortBreak(false);
              startShortBreak();
            } else if (longBreak) {
              setLongBreak(false);
              startLongBreak();
            } else {
              handlePomodoroEnd();
            }

            return 0;
          }
          return prevTime - 1000;
        });
      }, 1000);
    } else if (!isRunning) {
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [isRunning, isPaused, totalTime, shortBreak, longBreak]); // Dependencies include break states

  useEffect(() => {
    calculateTime(totalTime);
  }, [totalTime]);

  const handleStartNewSession = () => {
    handleReset();
    setShowInputs(true);
    setIsSessionStarted(false);
  };

  const handleStartSession = () => {
    const totalMilliseconds = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
    if (totalMilliseconds <= 0) {
      alert("Please set a valid time for the session.");
      return;
    }

    setTotalTime(totalMilliseconds);
    setIsRunning(true);
    setIsPaused(false);
    setIsSessionStarted(true);
    setShowInputs(false);
  };

  const handlePauseResume = () => {
    if (isPaused) {
      setIsPaused(false);
      setIsRunning(true);
    } else {
      setIsPaused(true);
      setIsRunning(false);
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
    setIncrementsCompleted(0);
    setShortBreak(false);
    setLongBreak(false);
  };

  const startShortBreak = () => {
    // Start a short break (5 minutes)
    setTotalTime(5 * 60 * 1000);
    setIsRunning(true);
    setIsPaused(false);
  };

  const startLongBreak = () => {
    // Start a long break (15 minutes)
    setTotalTime(15 * 60 * 1000);
    setIsRunning(true);
    setIsPaused(false);
  };

  const handlePomodoroEnd = () => {
    if (incrementsCompleted === 3) {
      // After 3 sessions, long break
      setLongBreak(true);
      setIncrementsCompleted(0); // Reset Pomodoro count after long break
    } else {
      // Start short break
      setShortBreak(true);
    }

    // Increase completed Pomodoro sessions
    setIncrementsCompleted(incrementsCompleted + 1);
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
          {String(seconds).padStart(2, "0")} <br />
          <h1 className="sub-text">Remaining</h1>
        </div>
      </div>

      <div className="session-buttons">
        <button style={!showInputs ? {} : { display: "none" }} onClick={handleStartNewSession} className="generic-button">
          Start New Session
        </button>

        {showInputs && (
          <div className="input-section">
            <div>
              <button onClick={handleStartSession} className="generic-button">
                Start Session
              </button>
              <br />
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

      </div>
    </div>
  );
};

export default Timer;
