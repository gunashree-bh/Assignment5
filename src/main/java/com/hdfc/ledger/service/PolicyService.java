package com.hdfc.ledger.service;

import com.hdfc.ledger.domain.Policy;
import com.hdfc.ledger.dto.CreatePolicyRequest;
import com.hdfc.ledger.dto.PolicyResponse;
import java.util.List;

public interface PolicyService {
    List<PolicyResponse> findAll();
    PolicyResponse findByPolicyNo(String policyNo);
    List<PolicyResponse> findByStatus(String status);
    List<PolicyResponse> findByType(String type);
    List<PolicyResponse> findByCustomer(String customer);
    List<PolicyResponse> findByMinPremium(int minPremium);
    PolicyResponse create(CreatePolicyRequest request);
    void delete(String policyNo);
    Policy getPolicyEntity(String policyNo);
}
