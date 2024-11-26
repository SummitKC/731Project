package org.cps731.project.team.cps731.pomodoro.dto;

public class StatusDTO {

    private String status;
    private String message;

    public StatusDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusDTO() {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
