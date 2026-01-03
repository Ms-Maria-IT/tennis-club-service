package com.tennistournament.clubservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TennisClubResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean isActive;
    private List<CourtResponse> courts;
}