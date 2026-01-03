package com.tennistournament.clubservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtResponse {
    private Long id;
    private String courtName;
    private Integer courtNumber;
    private String surfaceType;
    private Boolean isIndoor;
    private Boolean hasFloodlights;
    private Boolean isActive;
    private Long clubId;
    private String clubName;
}