package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    
    @Email(message = "Invalid email format")
   @NotBlank(message = "Email cannot be blank")
    private String email;

    // constructor
    public UserDTO(Long id, String username, String role, String email) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.email = email;
    }

    
}

