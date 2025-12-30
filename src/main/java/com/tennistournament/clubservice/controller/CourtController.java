package com.tennistournament.clubservice.controller;

import com.tennistournament.clubservice.dto.CourtRequest;
import com.tennistournament.clubservice.dto.CourtResponse;
import com.tennistournament.clubservice.service.CourtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CourtResponse> createCourt(@PathVariable Long clubId, 
                                                     @Valid @RequestBody CourtRequest request) {
        CourtResponse response = courtService.createCourt(clubId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all courts for a tennis club", description = "Retrieves all courts belonging to the specified tennis club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courts"),
        @ApiResponse(responseCode = "404", description = "Tennis club not found")
    })
    public ResponseEntity<List<CourtResponse>> getCourtsByClubId(@PathVariable Long clubId) {
        List<CourtResponse> courts = courtService.getCourtsByClubId(clubId);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/{courtId}")
    @Operation(summary = "Get court by ID", description = "Retrieves a specific court by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Court found"),
        @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<CourtResponse> getCourtById(@PathVariable Long courtId) {
        CourtResponse response = courtService.getCourtById(courtId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courtId}")
    @Operation(summary = "Delete court", description = "Deletes a court by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Court deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<Void> deleteCourt(@PathVariable Long courtId) {
        courtService.deleteCourt(courtId);
        return ResponseEntity.noContent().build();
    }
}
