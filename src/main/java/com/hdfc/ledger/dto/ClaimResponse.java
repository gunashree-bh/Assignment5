package com.hdfc.ledger.dto;

public record ClaimResponse(
    String claimNo,
    String policyNo,
    int claimAmount,
    String urgency,
    String status
) {}
