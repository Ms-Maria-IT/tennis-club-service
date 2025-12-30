package com.tennistournament.clubservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourtRequest {

    @NotNull(message = "Court number is required")
    private Integer courtNumber;

    @Size(max = 50, message = "Surface type must not exceed 50 characters")
    private String surfaceType;

    // Constructors
    public CourtRequest() {
    }

    public CourtRequest(Integer courtNumber, String surfaceType) {
        this.courtNumber = courtNumber;
        this.surfaceType = surfaceType;
    }

    // Getters and Setters
    public Integer getCourtNumber() {
        return courtNumber;
    }

    public void setCourtNumber(Integer courtNumber) {
        this.courtNumber = courtNumber;
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(String surfaceType) {
        this.surfaceType = surfaceType;
    }
}
