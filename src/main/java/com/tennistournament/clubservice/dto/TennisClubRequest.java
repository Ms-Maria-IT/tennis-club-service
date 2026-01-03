package com.tennistournament.clubservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TennisClubRequest {
    
    @NotBlank(message = "Club name is required")
    @Size(min = 1, max = 100)
    private String name;
    
    @NotBlank(message = "Address is required")
    @Size(max = 255)
    private String address;
    
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$")
    private String phoneNumber;
    
    @Email
    @Size(max = 100)
    private String email;
    
    private String openingTime;
    private String closingTime;
}