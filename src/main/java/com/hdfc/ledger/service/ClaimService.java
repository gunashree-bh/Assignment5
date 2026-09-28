package com.hdfc.ledger.service;

import com.hdfc.ledger.dto.ClaimResponse;
import com.hdfc.ledger.dto.CreateClaimRequest;
import com.hdfc.ledger.dto.*;
import java.util.List;

public interface ClaimService {
    ClaimResponse create(CreateClaimRequest request);
    List<ClaimResponse> findByPolicy(String policyNo);
}
