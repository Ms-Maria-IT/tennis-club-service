package com.tennistournament.clubservice.controller;

import com.tennistournament.clubservice.dto.CourtRequest;
import com.tennistournament.clubservice.dto.CourtResponse;
import com.tennistournament.clubservice.service.CourtService;
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
@RequestMapping("/api/clubs/{clubId}/courts")
@Tag(name = "Court Management", description = "API endpoints for managing courts within tennis clubs")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    @Operation(summary = "Add a court to a tennis club", description = "Creates a new court for the specified tennis club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Court created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found")
    })
    public ResponseEntity<CourtResponse> createCourt(
            @PathVariable Long clubId, 
            @Valid @RequestBody CourtRequest request) {
        
        log.info("POST /api/clubs/{}/courts - Creating court: courtNumber={}, surfaceType={}", 
                clubId, request.getCourtNumber(), request.getSurfaceType());
        
        long startTime = System.currentTimeMillis();
        
        try {
            CourtResponse response = courtService.createCourt(clubId, request);
            
            log.info("POST /api/clubs/{}/courts - Court created: id={}, duration={}ms", 
                    clubId, response.getId(), System.currentTimeMillis() - startTime);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("X-Court-Id", response.getId().toString())
                    .body(response);
            
        } catch (Exception e) {
            log.error("POST /api/clubs/{}/courts - Failed to create court: {}", 
                    clubId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all courts for a tennis club", description = "Retrieves all courts belonging to the specified tennis club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courts"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found")
    })
    public ResponseEntity<List<CourtResponse>> getCourtsByClubId(@PathVariable Long clubId) {
        log.info("GET /api/clubs/{}/courts - Retrieving all courts", clubId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            List<CourtResponse> courts = courtService.getCourtsByClubId(clubId);
            
            log.info("GET /api/clubs/{}/courts - Retrieved {} courts in {}ms", 
                    clubId, courts.size(), System.currentTimeMillis() - startTime);
            
            if (log.isDebugEnabled() && !courts.isEmpty()) {
                courts.forEach(court -> 
                    log.debug("Court in response: id={}, number={}", 
                            court.getId(), court.getCourtNumber()));
            }
            
            return ResponseEntity
                    .ok()
                    .header("X-Total-Count", String.valueOf(courts.size()))
                    .body(courts);
            
        } catch (Exception e) {
            log.error("GET /api/clubs/{}/courts - Failed to retrieve courts: {}", 
                    clubId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{courtId}")
    @Operation(summary = "Get court by ID", description = "Retrieves a specific court by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Court found"),
        @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<CourtResponse> getCourtById(
            @PathVariable Long clubId,
            @PathVariable Long courtId) {
        
        log.info("GET /api/clubs/{}/courts/{} - Retrieving court details", clubId, courtId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            CourtResponse response = courtService.getCourtById(courtId);
            
            log.info("GET /api/clubs/{}/courts/{} - Court retrieved in {}ms", 
                    clubId, courtId, System.currentTimeMillis() - startTime);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("GET /api/clubs/{}/courts/{} - Failed to retrieve court: {}", 
                    clubId, courtId, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{courtId}")
    @Operation(summary = "Delete court", description = "Deletes a court by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Court deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<Void> deleteCourt(
            @PathVariable Long clubId,
            @PathVariable Long courtId) {
        
        log.info("DELETE /api/clubs/{}/courts/{} - Deleting court", clubId, courtId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            courtService.deleteCourt(courtId);
            
            log.info("DELETE /api/clubs/{}/courts/{} - Court deleted in {}ms", 
                    clubId, courtId, System.currentTimeMillis() - startTime);
            
            return ResponseEntity
                    .noContent()
                    .build();
            
        } catch (Exception e) {
            log.error("DELETE /api/clubs/{}/courts/{} - Failed to delete court: {}", 
                    clubId, courtId, e.getMessage(), e);
            throw e;
        }
    }
}