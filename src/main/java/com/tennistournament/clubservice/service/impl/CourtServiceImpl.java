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
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Creating court for clubId={}, courtName={}, courtNumber={}", 
                clubId, request.getCourtName(), request.getCourtNumber());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClub club = tennisClubRepository.findById(clubId)
                    .orElseThrow(() -> {
                        log.warn("Club not found for court creation: clubId={}", clubId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Tennis club not found with id: " + clubId);
                    });
            
            log.debug("Club found: id={}, name={}", club.getId(), club.getName());
            
            // ИСПРАВЛЕНО: Полный маппинг из request в entity
            Court court = Court.builder()
                    .courtName(request.getCourtName())
                    .courtNumber(request.getCourtNumber())
                    .surfaceType(request.getSurfaceType())
                    .isIndoor(request.getIsIndoor() != null ? request.getIsIndoor() : false)
                    .hasFloodlights(request.getHasFloodlights() != null ? request.getHasFloodlights() : false)
                    .isActive(true) // Новый корт всегда активен
                    .tennisClub(club)
                    .build();
            
            Court savedCourt = courtRepository.save(court);
            
            log.info("Court created successfully: id={}, name={}, courtNumber={}, clubId={}", 
                    savedCourt.getId(), savedCourt.getCourtName(), 
                    savedCourt.getCourtNumber(), clubId);
            
            CourtResponse response = mapToResponse(savedCourt);
            
            log.debug("Court creation completed in {}ms", 
                    System.currentTimeMillis() - startTime);
            
            return response;
            
        } catch (Exception e) {
            log.error("Failed to create court for clubId={}: {}", clubId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourtResponse> getCourtsByClubId(Long clubId) {
        log.debug("Fetching courts for clubId={}", clubId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            if (!tennisClubRepository.existsById(clubId)) {
                log.warn("Club not found when fetching courts: clubId={}", clubId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Tennis club not found with id: " + clubId);
            }
            
            List<Court> courts = courtRepository.findByTennisClubId(clubId);
            
            log.info("Retrieved {} courts for clubId={}", courts.size(), clubId);
            
            if (log.isDebugEnabled()) {
                courts.forEach(court -> 
                    log.debug("Court found: id={}, name={}, number={}, surface={}", 
                            court.getId(), court.getCourtName(), 
                            court.getCourtNumber(), court.getSurfaceType()));
            }
            
            List<CourtResponse> responses = courts.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
            log.debug("Court retrieval completed in {}ms", 
                    System.currentTimeMillis() - startTime);
            
            return responses;
            
        } catch (Exception e) {
            log.error("Error fetching courts for clubId={}: {}", clubId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CourtResponse getCourtById(Long id) {
        log.debug("Fetching court by id={}", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Court court = courtRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Court not found: id={}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Court not found with id: " + id);
                    });
            
            log.info("Court retrieved: id={}, name={}, number={}, clubId={}", 
                    court.getId(), court.getCourtName(), court.getCourtNumber(),
                    court.getTennisClub() != null ? court.getTennisClub().getId() : "null");
            
            CourtResponse response = mapToResponse(court);
            
            log.debug("Court retrieval by id completed in {}ms", 
                    System.currentTimeMillis() - startTime);
            
            return response;
            
        } catch (Exception e) {
            log.error("Error fetching court with id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteCourt(Long id) {
        log.info("Deleting court: id={}", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            if (!courtRepository.existsById(id)) {
                log.warn("Court not found for deletion: id={}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Court not found with id: " + id);
            }
            
            courtRepository.deleteById(id);
            
            log.info("Court deleted successfully: id={}", id);
            log.debug("Court deletion completed in {}ms", 
                    System.currentTimeMillis() - startTime);
            
        } catch (Exception e) {
            log.error("Failed to delete court with id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ИСПРАВЛЕННЫЙ метод маппинга
    private CourtResponse mapToResponse(Court court) {
        CourtResponse response = new CourtResponse();
        response.setId(court.getId());
        response.setCourtName(court.getCourtName());
        response.setCourtNumber(court.getCourtNumber());
        response.setSurfaceType(court.getSurfaceType().name()); // Enum -> String
        response.setIsIndoor(court.getIsIndoor());
        response.setHasFloodlights(court.getHasFloodlights());
        response.setIsActive(court.getIsActive());
        
        if (court.getTennisClub() != null) {
            response.setClubId(court.getTennisClub().getId());
            response.setClubName(court.getTennisClub().getName());
        }
        
        return response;
    }
}