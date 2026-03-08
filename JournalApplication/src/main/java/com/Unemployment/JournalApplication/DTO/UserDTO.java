package com.Unemployment.JournalApplication.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty; // Changed from javax to jakarta
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for User registration and details")
public class UserDTO {

    @NotEmpty(message = "Username cannot be empty")
    @Schema(description = "The user's unique username", example = "aditya_123")
    private String userName;

    @Schema(description = "The user's email address", example = "aditya@example.com")
    private String email;

    @Schema(description = "Opt-in status for weekly sentiment analysis reports")
    private boolean sentimentAnalysis;

    @NotEmpty(message = "Password cannot be empty")
    @Schema(description = "The user's plain text password", example = "Password@123")
    private String password;
}