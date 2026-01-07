package com.chatop.back.rental.controller;

import lombok.RequiredArgsConstructor;
import com.chatop.back.rental.service.*;
import org.springframework.http.MediaType;
import com.chatop.back.rental.dto.RentalDto;
import com.chatop.back.rental.dto.RentalsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import com.chatop.back.rental.request.CreateRentalRequest;
import com.chatop.back.rental.request.UpdateRentalRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${app.basePath}/rentals")
@RequiredArgsConstructor
@Tag(
    name = "Rental",
    description = "Endpoints for rentals"
)
public class RentalController {

    private final CreateRentalService createRentalService;
    private final DeleteRentalService deleteRentalService;
    private final UpdateRentalService updateRentalService;
    private final GetRentalService getRentalService;
    private final GetRentalsService getRentalsService;

    @Operation(summary = "Get all rentals", description = "Returns all rentals")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rentals retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: JWT token missing or invalid")
    })
    @GetMapping("")
    public ResponseEntity<RentalsDto> getRentals() {
        return ResponseEntity.ok(getRentalsService.execute());
    }

    @Operation(summary = "Get rental by ID", description = "Returns a single rental by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rental retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRental(@PathVariable Long id) {
        RentalDto rental = getRentalService.execute(id);
        return ResponseEntity.ok(rental);
    }

    @Operation(summary = "Create a new rental", description = "Creates a new rental")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rental created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid rental data")
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<RentalDto> createRental(@ModelAttribute CreateRentalRequest request) {
        RentalDto created = createRentalService.execute(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update rental", description = "Updates an existing rental by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<RentalDto> updateRental(
        @PathVariable Long id,
        @ModelAttribute UpdateRentalRequest request
    ) {
        RentalDto updated = updateRentalService.execute(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete rental", description = "Deletes a rental by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rental deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRental(@PathVariable Long id) {
        deleteRentalService.execute(id);
        return ResponseEntity.ok().body(
            java.util.Map.of("message", "Rental deleted successfully")
        );
    }
}
