package com.hdfclife.ledger.service;

import com.hdfclife.ledger.dto.*;
import java.util.List;

public interface ClaimService {
    ClaimResponse create(CreateClaimRequest request);
    List<ClaimResponse> findByPolicy(String policyNo);
}
