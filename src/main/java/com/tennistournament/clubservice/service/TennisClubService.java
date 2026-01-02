package com.tennistournament.clubservice.service;

import com.tennistournament.clubservice.dto.TennisClubRequest;
import com.tennistournament.clubservice.dto.TennisClubResponse;

import java.util.List;

/**
 * Service interface for managing tennis club operations.
 * Provides CRUD operations for tennis clubs with comprehensive logging
 * and error handling in implementations.
 */
public interface TennisClubService {
    
    /**
     * Creates and persists a new tennis club.
     * 
     * @param request the club creation request containing club details
     * @return the created club response with generated ID
     * @throws BusinessRuleException if club name already exists
     * @throws ValidationException if input validation fails
     */
    TennisClubResponse createClub(TennisClubRequest request);
    
    /**
     * Retrieves all tennis clubs in the system.
     * 
     * @return list of all club responses, never null but possibly empty
     */
    List<TennisClubResponse> getAllClubs();
    
    /**
     * Retrieves a specific tennis club by its ID.
     * 
     * @param id the unique identifier of the club
     * @return the club response
     * @throws ResourceNotFoundException if no club exists with the given ID
     */
    TennisClubResponse getClubById(Long id);
    
    /**
     * Updates an existing tennis club with new data.
     * 
     * @param id the unique identifier of the club to update
     * @param request the update request containing new club details
     * @return the updated club response
     * @throws ResourceNotFoundException if no club exists with the given ID
     * @throws BusinessRuleException if update violates business rules
     */
    TennisClubResponse updateClub(Long id, TennisClubRequest request);
    
    /**
     * Permanently removes a tennis club from the system.
     * 
     * @param id the unique identifier of the club to delete
     * @throws ResourceNotFoundException if no club exists with the given ID
     * @throws BusinessRuleException if club has associated courts or tournaments
     */
    void deleteClub(Long id);
}