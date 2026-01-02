package com.tennistournament.clubservice.controller;

import com.tennistournament.clubservice.dto.TennisClubRequest;
import com.tennistournament.clubservice.dto.TennisClubResponse;
import com.tennistournament.clubservice.service.TennisClubService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clubs")
@Tag(name = "Tennis Club Management", description = "API endpoints for managing tennis clubs")
public class TennisClubController {

    private final TennisClubService tennisClubService;

    public TennisClubController(TennisClubService tennisClubService) {
        this.tennisClubService = tennisClubService;
    }

    @PostMapping
    @RateLimiter(name = "clubCreationStrict")
    @Operation(summary = "Create a new tennis club", description = "Creates a new tennis club with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tennis club created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
    })
    public ResponseEntity<TennisClubResponse> createClub(@Valid @RequestBody TennisClubRequest request) {
        log.info("POST /api/clubs - Creating club: name={}, address={}", 
                request.getName(), request.getAddress());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClubResponse response = tennisClubService.createClub(request);
            
            log.info("POST /api/clubs - Club created: id={}, name={}, duration={}ms", 
                    response.getId(), response.getName(), 
                    System.currentTimeMillis() - startTime);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("X-Club-Id", response.getId().toString())
                    .header("Location", "/api/clubs/" + response.getId())
                    .body(response);
            
        } catch (Exception e) {
            log.error("POST /api/clubs - Failed to create club '{}': {}", 
                    request.getName(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @RateLimiter(name = "clubApiGlobal")
    @Operation(summary = "Get all tennis clubs", description = "Retrieves a list of all tennis clubs")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tennis clubs")
    public ResponseEntity<List<TennisClubResponse>> getAllClubs() {
        log.info("GET /api/clubs - Retrieving all clubs");
        
        long startTime = System.currentTimeMillis();
        
        try {
            List<TennisClubResponse> clubs = tennisClubService.getAllClubs();
            
            log.info("GET /api/clubs - Retrieved {} clubs in {}ms", 
                    clubs.size(), System.currentTimeMillis() - startTime);
            
            if (log.isDebugEnabled() && !clubs.isEmpty()) {
                clubs.forEach(club -> 
                    log.debug("Club in response: id={}, name={}, courts={}", 
                            club.getId(), club.getName(), 
                            club.getCourtIds() != null ? club.getCourtIds().size() : 0));
            }
            
            return ResponseEntity
                    .ok()
                    .header("X-Total-Count", String.valueOf(clubs.size()))
                    .body(clubs);
            
        } catch (Exception e) {
            log.error("GET /api/clubs - Failed to retrieve clubs: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "clubApiGlobal")
    @Operation(summary = "Get tennis club by ID", description = "Retrieves a specific tennis club by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tennis club found"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found")
    })
    public ResponseEntity<TennisClubResponse> getClubById(@PathVariable Long id) {
        log.info("GET /api/clubs/{} - Retrieving club details", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClubResponse response = tennisClubService.getClubById(id);
            
            log.info("GET /api/clubs/{} - Club retrieved: name={}, duration={}ms", 
                    id, response.getName(), System.currentTimeMillis() - startTime);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("GET /api/clubs/{} - Failed to retrieve club: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tennis club", description = "Updates an existing tennis club with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tennis club updated successfully"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TennisClubResponse> updateClub(
            @PathVariable Long id, 
            @Valid @RequestBody TennisClubRequest request) {
        
        log.info("PUT /api/clubs/{} - Updating club: name={}, address={}", 
                id, request.getName(), request.getAddress());
        
        long startTime = System.currentTimeMillis();
        
        try {
            TennisClubResponse response = tennisClubService.updateClub(id, request);
            
            log.info("PUT /api/clubs/{} - Club updated: name={}, duration={}ms", 
                    id, response.getName(), System.currentTimeMillis() - startTime);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("PUT /api/clubs/{} - Failed to update club: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tennis club", description = "Deletes a tennis club by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tennis club deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found")
    })
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.info("DELETE /api/clubs/{} - Deleting club", id);
        
        long startTime = System.currentTimeMillis();
        
        try {
            tennisClubService.deleteClub(id);
            
            log.info("DELETE /api/clubs/{} - Club deleted in {}ms", 
                    id, System.currentTimeMillis() - startTime);
            
            return ResponseEntity
                    .noContent()
                    .header("X-Club-Deleted", "true")
                    .build();
            
        } catch (Exception e) {
            log.error("DELETE /api/clubs/{} - Failed to delete club: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}