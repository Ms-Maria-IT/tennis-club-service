package com.tennistournament.clubservice.service.impl;

import com.tennistournament.clubservice.dto.CourtRequest;
import com.tennistournament.clubservice.dto.CourtResponse;
import com.tennistournament.clubservice.model.Court;
import com.tennistournament.clubservice.model.TennisClub;
import com.tennistournament.clubservice.repository.CourtRepository;
import com.tennistournament.clubservice.repository.TennisClubRepository;
import com.tennistournament.clubservice.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final TennisClubRepository tennisClubRepository;

    public CourtServiceImpl(CourtRepository courtRepository, TennisClubRepository tennisClubRepository) {
        this.courtRepository = courtRepository;
        this.tennisClubRepository = tennisClubRepository;
    }

    @Override
    public CourtResponse createCourt(Long clubId, CourtRequest request) {
        TennisClub club = tennisClubRepository.findById(clubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Tennis club not found with id: " + clubId));
        
        Court court = new Court();
        court.setCourtNumber(request.getCourtNumber());
        court.setSurfaceType(request.getSurfaceType());
        court.setTennisClub(club);
        
        Court savedCourt = courtRepository.save(court);
        return mapToResponse(savedCourt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourtResponse> getCourtsByClubId(Long clubId) {
        if (!tennisClubRepository.existsById(clubId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Tennis club not found with id: " + clubId);
        }
        
        return courtRepository.findByTennisClubId(clubId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourtResponse getCourtById(Long id) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Court not found with id: " + id));
        return mapToResponse(court);
    }

    @Override
    public void deleteCourt(Long id) {
        if (!courtRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Court not found with id: " + id);
        }
        courtRepository.deleteById(id);
    }

    private CourtResponse mapToResponse(Court court) {
        CourtResponse response = new CourtResponse();
        response.setId(court.getId());
        response.setCourtNumber(court.getCourtNumber());
        response.setSurfaceType(court.getSurfaceType());
        if (court.getTennisClub() != null) {
            response.setTennisClubId(court.getTennisClub().getId());
        }
        return response;
    }
}
