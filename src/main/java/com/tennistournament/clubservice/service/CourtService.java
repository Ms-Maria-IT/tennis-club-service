package com.tennistournament.clubservice.service;

import com.tennistournament.clubservice.dto.CourtRequest;
import com.tennistournament.clubservice.dto.CourtResponse;

import java.util.List;

public interface CourtService {
    CourtResponse createCourt(Long clubId, CourtRequest request);
    List<CourtResponse> getCourtsByClubId(Long clubId);
    CourtResponse getCourtById(Long id);
    void deleteCourt(Long id);
}
