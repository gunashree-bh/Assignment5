package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    @Override
    @Transactional
    public ClaimResponse create(CreateClaimRequest request) {
        Policy policy = policyRepository.findByPolicyNo(request.policyNo())
                .orElseThrow(() -> new PolicyNotFoundException(request.policyNo()));

        long next = claimRepository.count() + 1;
        String claimNo;
        do {
            claimNo = String.format("CLM-%02d", next++);
        } while (claimRepository.findByClaimNo(claimNo).isPresent());

        Claim claim = new Claim(claimNo, policy, request.claimAmount(),
                request.urgency(), "SUBMITTED", request.hospitalName());
        return toResponse(claimRepository.save(claim));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponse> findByPolicy(String policyNo) {
        if (!policyRepository.existsByPolicyNo(policyNo)) {
            throw new PolicyNotFoundException(policyNo);
        }
        return claimRepository.findByPolicy_PolicyNoOrderByClaimNoAsc(policyNo)
                .stream().map(this::toResponse).toList();
    }

    private ClaimResponse toResponse(Claim c) {
        return new ClaimResponse(c.getClaimNo(), c.getPolicy().getPolicyNo(),
                c.getAmount(), c.getUrgency(), c.getStatus());
    }
}
