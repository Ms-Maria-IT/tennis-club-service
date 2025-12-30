package com.tennistournament.clubservice.dto;

public class CourtResponse {
    private Long id;
    private Integer courtNumber;
    private String surfaceType;
    private Long tennisClubId;

    // Constructors
    public CourtResponse() {
    }

    public CourtResponse(Long id, Integer courtNumber, String surfaceType, Long tennisClubId) {
        this.id = id;
        this.courtNumber = courtNumber;
        this.surfaceType = surfaceType;
        this.tennisClubId = tennisClubId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getTennisClubId() {
        return tennisClubId;
    }

    public void setTennisClubId(Long tennisClubId) {
        this.tennisClubId = tennisClubId;
    }
}
