package com.tennistournament.clubservice.service.impl;

import com.tennistournament.clubservice.dto.TennisClubRequest;
import com.tennistournament.clubservice.dto.TennisClubResponse;
import com.tennistournament.clubservice.dto.CourtResponse;
import com.tennistournament.clubservice.model.TennisClub;
import com.tennistournament.clubservice.repository.TennisClubRepository;
import com.tennistournament.clubservice.service.TennisClubService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TennisClubServiceImpl implements TennisClubService {

    private final TennisClubRepository tennisClubRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TennisClubServiceImpl(TennisClubRepository tennisClubRepository) {
        this.tennisClubRepository = tennisClubRepository;
    }

    @Override
    public TennisClubResponse createClub(TennisClubRequest request) {
        log.info("Creating new tennis club: name={}, address={}", 
                request.getName(), request.getAddress());
        
        long startTime = System.currentTimeMillis();
        
        try {
            // ИСПРАВЛЕНО: Полный маппинг из request
            TennisClub club = TennisClub.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .phoneNumber(request.getPhoneNumber())
                    .email(request.getEmail())
                    .openingTime(parseTime(request.getOpeningTime()))
                    .closingTime(parseTime(request.getClosingTime()))
                    .isActive(true)
                    .build();
            
            TennisClub savedClub = tennisClubRepository.save(club);
            
            log.info("Club created successfully: id={}, name={}, duration={}ms", 
                    savedClub.getId(), savedClub.getName(), 
                    System.currentTimeMillis() - startTime);
            
            return mapToResponse(savedClub);
            
        } catch (Exception e) {
            log.error("Failed to create club '{}': {}", 
                    request.getName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TennisClubResponse> getAllClubs() {
        log.debug("Retrieving all tennis clubs");
        
        long startTime = System.currentTimeMillis();
        
        try {
            List<TennisClub> clubs = tennisClubRepository.findAll();
            
            log.info("Retrieved {} clubs in {}ms", 
                    clubs.size(), System.currentTimeMillis() - startTime);
            
            if (log.isDebugEnabled() && !clubs.isEmpty()) {
                clubs.forEach(club -> 
                    log.debug("Club found: id={}, name={}, courts={}", 
                            club.getId(), club.getName(), 
                            club.getCourts() != null ? club.getCourts().size() : 0));
            }
            
            return clubs.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Failed to retrieve clubs: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TennisClubResponse getClubById(Long id) {
        log.debug("Fetching club by id: {}", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClub club = tennisClubRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Club not found: id={}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Tennis club not found with id: " + id);
                    });
            
            log.info("Club retrieved: id={}, name={}, courts={}, duration={}ms", 
                    club.getId(), club.getName(), 
                    club.getCourts() != null ? club.getCourts().size() : 0,
                    System.currentTimeMillis() - startTime);
            
            return mapToResponse(club);
            
        } catch (Exception e) {
            log.error("Failed to fetch club id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TennisClubResponse updateClub(Long id, TennisClubRequest request) {
        log.info("Updating club id={}: name={}", id, request.getName());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClub club = tennisClubRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Club not found for update: id={}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Tennis club not found with id: " + id);
                    });
            
            String oldName = club.getName();
            
            // ИСПРАВЛЕНО: Обновляем все поля
            club.setName(request.getName());
            club.setAddress(request.getAddress());
            club.setPhoneNumber(request.getPhoneNumber());
            club.setEmail(request.getEmail());
            club.setOpeningTime(parseTime(request.getOpeningTime()));
            club.setClosingTime(parseTime(request.getClosingTime()));
            
            TennisClub updatedClub = tennisClubRepository.save(club);
            
            log.info("Club updated: id={}, name={} -> {}, duration={}ms", 
                    id, oldName, updatedClub.getName(), 
                    System.currentTimeMillis() - startTime);
            
            return mapToResponse(updatedClub);
            
        } catch (Exception e) {
            log.error("Failed to update club id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteClub(Long id) {
        log.info("Deleting club id={}", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            if (!tennisClubRepository.existsById(id)) {
                log.warn("Club not found for deletion: id={}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Tennis club not found with id: " + id);
            }
            
            tennisClubRepository.deleteById(id);
            
            log.info("Club deleted successfully: id={}, duration={}ms", 
                    id, System.currentTimeMillis() - startTime);
            
        } catch (Exception e) {
            log.error("Failed to delete club id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ИСПРАВЛЕННЫЙ метод маппинга
    private TennisClubResponse mapToResponse(TennisClub club) {
        TennisClubResponse response = new TennisClubResponse();
        response.setId(club.getId());
        response.setName(club.getName());
        response.setAddress(club.getAddress());
        response.setPhoneNumber(club.getPhoneNumber());
        response.setEmail(club.getEmail());
        response.setOpeningTime(club.getOpeningTime());
        response.setClosingTime(club.getClosingTime());
        response.setIsActive(club.getIsActive());
        
        // Маппинг кортов
        if (club.getCourts() != null && !club.getCourts().isEmpty()) {
            List<CourtResponse> courtResponses = club.getCourts().stream()
                    .map(court -> {
                        CourtResponse courtResponse = new CourtResponse();
                        courtResponse.setId(court.getId());
                        courtResponse.setCourtName(court.getCourtName());
                        courtResponse.setCourtNumber(court.getCourtNumber());
                        courtResponse.setSurfaceType(court.getSurfaceType().name());
                        courtResponse.setIsIndoor(court.getIsIndoor());
                        courtResponse.setHasFloodlights(court.getHasFloodlights());
                        courtResponse.setIsActive(court.getIsActive());
                        courtResponse.setClubId(club.getId());
                        courtResponse.setClubName(club.getName());
                        return courtResponse;
                    })
                    .collect(Collectors.toList());
            response.setCourts(courtResponses);
            
            log.trace("Mapped club to response: id={}, name={}, courts={}", 
                    club.getId(), club.getName(), courtResponses.size());
        } else {
            log.trace("Mapped club to response: id={}, name={}, no courts", 
                    club.getId(), club.getName());
        }
        
        return response;
    }
    
    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            log.warn("Invalid time format: {}, expected HH:mm", timeStr);
            return null;
        }
    }
}