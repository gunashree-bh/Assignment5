package com.hdfc.ledger.service;

import com.hdfc.ledger.domain.Customer;
import com.hdfc.ledger.domain.Policy;
import com.hdfc.ledger.domain.Rider;
import com.hdfc.ledger.dto.CreatePolicyRequest;
import com.hdfc.ledger.dto.PolicyResponse;
import com.hdfc.ledger.exception.DuplicatePolicyException;
import com.hdfc.ledger.exception.InvalidRequestException;
import com.hdfc.ledger.exception.PolicyNotFoundException;
import com.hdfc.ledger.repo.CustomerRepository;
import com.hdfc.ledger.repo.PolicyRepository;
import com.hdfc.ledger.domain.*;
import com.hdfc.ledger.dto.*;
import com.hdfc.ledger.exception.*;
import com.hdfc.ledger.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository, CustomerRepository customerRepository) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> findAll() {
        return policyRepository.findAll(org.springframework.data.domain.Sort.by("policyNo"))
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyResponse findByPolicyNo(String policyNo) {
        return toResponse(getPolicyEntity(policyNo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> findByStatus(String status) {
        return policyRepository.findByStatusOrderByPolicyNoAsc(status).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> findByType(String type) {
        return policyRepository.findByProductTypeOrderByPolicyNoAsc(type).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> findByCustomer(String customer) {
        return policyRepository.findByCustomer_FullNameOrderByPolicyNoAsc(customer).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> findByMinPremium(int minPremium) {
        if (minPremium < 0) throw new InvalidRequestException("minPremium must be >= 0");
        return policyRepository.findWithPremiumAtLeast(minPremium).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public PolicyResponse create(CreatePolicyRequest request) {
        if (policyRepository.existsByPolicyNo(request.policyNo())) {
            throw new DuplicatePolicyException(request.policyNo());
        }
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseGet(() -> customerRepository.save(new Customer(request.customer(), request.email())));
        Policy policy = new Policy(request.policyNo(), customer, request.type(), request.basePremium(), request.status());
        return toResponse(policyRepository.save(policy));
    }

    @Override
    @Transactional
    public void delete(String policyNo) {
        policyRepository.delete(getPolicyEntity(policyNo));
    }

    @Override
    @Transactional(readOnly = true)
    public Policy getPolicyEntity(String policyNo) {
        return policyRepository.findByPolicyNo(policyNo)
                .orElseThrow(() -> new PolicyNotFoundException(policyNo));
    }

    private PolicyResponse toResponse(Policy p) {
        List<String> codes = p.getRiders().stream()
                .map(Rider::getCode)
                .sorted()
                .toList();
        return new PolicyResponse(p.getPolicyNo(), p.getCustomer().getFullName(),
                p.getCustomer().getEmail(), p.getProductType(), p.getBasePremium(),
                p.getStatus(), codes);
    }
}
