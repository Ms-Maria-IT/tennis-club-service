package com.tennistournament.clubservice.service;

import com.tennistournament.clubservice.dto.TennisClubRequest;
import com.tennistournament.clubservice.dto.TennisClubResponse;

import java.util.List;

public interface TennisClubService {
    TennisClubResponse createClub(TennisClubRequest request);
    List<TennisClubResponse> getAllClubs();
    TennisClubResponse getClubById(Long id);
    TennisClubResponse updateClub(Long id, TennisClubRequest request);
    void deleteClub(Long id);
}
