package com.hdfc.ledger.web;

import com.hdfc.ledger.dto.ClaimResponse;
import com.hdfc.ledger.dto.CreatePolicyRequest;
import com.hdfc.ledger.dto.PolicyResponse;
import com.hdfc.ledger.exception.InvalidRequestException;
import com.hdfc.ledger.dto.*;
import com.hdfc.ledger.service.PolicyService;
import com.hdfc.ledger.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies")
public class PolicyController {
    private final PolicyService service;
    private final ClaimService claimService;

    public PolicyController(PolicyService service, ClaimService claimService) {
        this.service = service;
        this.claimService = claimService;
    }

    @GetMapping
    @Operation(summary = "List policies")
    @ApiResponse(responseCode = "200")
    public List<PolicyResponse> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String customer) {
        if (status != null) return service.findByStatus(status);
        if (type != null) return service.findByType(type);
        if (customer != null) return service.findByCustomer(customer);
        return service.findAll();
    }

    @GetMapping("/search")
    @Operation(summary = "Search by minimum premium")
    public List<PolicyResponse> search(@RequestParam(required = false) Integer minPremium) {
        if (minPremium == null || minPremium < 0) {
            throw new InvalidRequestException("minPremium must be provided and >= 0");
        }
        return service.findByMinPremium(minPremium);
    }

    @GetMapping("/{policyNo}")
    public PolicyResponse get(@PathVariable String policyNo) {
        return service.findByPolicyNo(policyNo);
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody CreatePolicyRequest request) {
        PolicyResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/policies/" + response.policyNo())).body(response);
    }

    @DeleteMapping("/{policyNo}")
    public ResponseEntity<Void> delete(@PathVariable String policyNo) {
        service.delete(policyNo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{policyNo}/claims")
    public List<ClaimResponse> claims(@PathVariable String policyNo) {
        return claimService.findByPolicy(policyNo);
    }
}
