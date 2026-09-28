package com.hdfc.ledger.web;

import com.hdfc.ledger.dto.ClaimResponse;
import com.hdfc.ledger.dto.CreateClaimRequest;
import com.hdfc.ledger.dto.*;
import com.hdfc.ledger.service.ClaimService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

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
