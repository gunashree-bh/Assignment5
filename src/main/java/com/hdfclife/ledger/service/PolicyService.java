package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
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
