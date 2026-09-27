package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.*;
import com.hdfclife.ledger.service.ClaimService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claims")
public class ClaimController {
    private final ClaimService service;

    public ClaimController(ClaimService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ClaimResponse> create(@Valid @RequestBody CreateClaimRequest request) {
        ClaimResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/claims/" + response.claimNo())).body(response);
    }
}
