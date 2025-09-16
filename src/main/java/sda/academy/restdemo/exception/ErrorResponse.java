package sda.academy.restdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ErrorResponse {
    String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
