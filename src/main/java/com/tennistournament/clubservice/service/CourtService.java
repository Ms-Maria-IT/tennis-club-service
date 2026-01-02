package com.tennistournament.clubservice.service;

import com.tennistournament.clubservice.dto.CourtRequest;
import com.tennistournament.clubservice.dto.CourtResponse;

import java.util.List;

/**
 * Service interface for court management operations.
 * Provides CRUD operations for courts within tennis clubs.
 * 
 * <p>All methods include comprehensive logging and error handling
 * in their implementations.</p>
 */
public interface CourtService {
    
    /**
     * Creates a new court for the specified tennis club.
     * 
     * @param clubId the ID of the tennis club
     * @param request the court creation request containing court details
     * @return the created court response with generated ID
     * @throws ResourceNotFoundException if the tennis club is not found
     * @throws BusinessRuleException if court creation violates business rules
     * 
     * @implNote Logs: INFO for creation attempt, INFO for success, ERROR for failures
     */
    CourtResponse createCourt(Long clubId, CourtRequest request);
    
    /**
     * Retrieves all courts belonging to the specified tennis club.
     * 
     * @param clubId the ID of the tennis club
     * @return list of court responses for the club
     * @throws ResourceNotFoundException if the tennis club is not found
     * 
     * @implNote Logs: INFO for request, INFO with count for success, DEBUG for details
     */
    List<CourtResponse> getCourtsByClubId(Long clubId);
    
    /**
     * Retrieves a specific court by its ID.
     * 
     * @param id the ID of the court to retrieve
     * @return the court response
     * @throws ResourceNotFoundException if the court is not found
     * 
     * @implNote Logs: DEBUG for retrieval attempt, INFO for success, ERROR for failures
     */
    CourtResponse getCourtById(Long id);
    
    /**
     * Deletes a court by its ID.
     * 
     * @param id the ID of the court to delete
     * @throws ResourceNotFoundException if the court is not found
     * 
     * @implNote Logs: INFO for deletion attempt, INFO for success, ERROR for failures
     */
    void deleteCourt(Long id);
}