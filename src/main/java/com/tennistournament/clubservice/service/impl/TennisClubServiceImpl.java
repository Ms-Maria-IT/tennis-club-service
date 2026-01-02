package com.tennistournament.clubservice.service.impl;

import com.tennistournament.clubservice.dto.TennisClubRequest;
import com.tennistournament.clubservice.dto.TennisClubResponse;
import com.tennistournament.clubservice.model.TennisClub;
import com.tennistournament.clubservice.repository.TennisClubRepository;
import com.tennistournament.clubservice.service.TennisClubService;
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
public class TennisClubServiceImpl implements TennisClubService {

    private final TennisClubRepository tennisClubRepository;

    public TennisClubServiceImpl(TennisClubRepository tennisClubRepository) {
        this.tennisClubRepository = tennisClubRepository;
    }

    @Override
    public TennisClubResponse createClub(TennisClubRequest request) {
        log.info("Creating new tennis club: name={}", request.getName());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClub club = new TennisClub();
            club.setName(request.getName());
            club.setAddress(request.getAddress());
            
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
        log.info("Updating club id={}: name={}, address={}", 
                id, request.getName(), request.getAddress());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClub club = tennisClubRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Club not found for update: id={}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Tennis club not found with id: " + id);
                    });
            
            String oldName = club.getName();
            club.setName(request.getName());
            club.setAddress(request.getAddress());
            
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

    private TennisClubResponse mapToResponse(TennisClub club) {
        TennisClubResponse response = new TennisClubResponse();
        response.setId(club.getId());
        response.setName(club.getName());
        response.setAddress(club.getAddress());
        
        if (club.getCourts() != null && !club.getCourts().isEmpty()) {
            List<Long> courtIds = club.getCourts().stream()
                    .map(court -> court.getId())
                    .collect(Collectors.toList());
            response.setCourtIds(courtIds);
            
            log.trace("Mapped club to response: id={}, name={}, courts={}", 
                    club.getId(), club.getName(), courtIds.size());
        } else {
            log.trace("Mapped club to response: id={}, name={}, no courts", 
                    club.getId(), club.getName());
        }
        
        return response;
    }
}