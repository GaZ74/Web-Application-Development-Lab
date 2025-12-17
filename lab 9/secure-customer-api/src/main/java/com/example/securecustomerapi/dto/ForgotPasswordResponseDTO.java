package com.example.securecustomerapi.dto;

public class ForgotPasswordResponseDTO {
    
    private String message;
    private String resetToken;
    
    // Constructors
    public ForgotPasswordResponseDTO() {
    }
    
    public ForgotPasswordResponseDTO(String message, String resetToken) {
        this.message = message;
        this.resetToken = resetToken;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getResetToken() {
        return resetToken;
    }
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
