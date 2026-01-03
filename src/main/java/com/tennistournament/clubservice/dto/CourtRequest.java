package com.tennistournament.clubservice.dto;

import com.tennistournament.clubservice.model.Court;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourtRequest {
    
    @NotBlank(message = "Court name is required")
    @Size(min = 1, max = 50)
    private String courtName;
    
    @NotNull(message = "Court number is required")
    @Min(1)
    private Integer courtNumber;
    
    @NotNull(message = "Surface type is required")
    private Court.SurfaceType surfaceType;
    
    private Boolean isIndoor = false;
    
    private Boolean hasFloodlights = false;
}