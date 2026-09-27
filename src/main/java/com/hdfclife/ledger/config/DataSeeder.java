package com.hdfclife.ledger.config;

import com.hdfclife.ledger.domain.*;
import com.hdfclife.ledger.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    @Profile("dev")
    CommandLineRunner seed(CustomerRepository customers, PolicyRepository policies, RiderRepository riders) {
        return args -> {
            if (policies.count() > 0) return;

            Customer anita = customers.save(new Customer("Anita Sharma", "anita.sharma@hdfclife.example"));
            Customer rahul = customers.save(new Customer("Rahul Mehta", "rahul.mehta@hdfclife.example"));
            Customer priya = customers.save(new Customer("Priya Nair", "priya.nair@hdfclife.example"));
            Customer vikram = customers.save(new Customer("Vikram Singh", "vikram.singh@hdfclife.example"));
            Customer sneha = customers.save(new Customer("Sneha Patel", "sneha.patel@hdfclife.example"));

            Rider accident = riders.findByCode("ACCIDENT_COVER").orElseThrow();
            Rider waiver = riders.findByCode("WAIVER_OF_PREMIUM").orElseThrow();

            Policy p1 = new Policy("HDFC-LIFE-1001", anita, "TERM", 18500, "Active");
            p1.getRiders().addAll(List.of(accident, waiver));
            policies.save(p1);
            policies.save(new Policy("HDFC-LIFE-1002", rahul, "ULIP", 42000, "Active"));
            policies.save(new Policy("HDFC-LIFE-1003", priya, "ENDOWMENT", 27000, "Lapsed"));
            policies.save(new Policy("HDFC-LIFE-1004", vikram, "TERM", 15200, "Active"));
            policies.save(new Policy("HDFC-LIFE-1005", sneha, "ULIP", 36000, "Active"));
            policies.save(new Policy("HDFC-LIFE-1006", anita, "ENDOWMENT", 22000, "Pending"));

            System.out.println("Active profile → dev");
            System.out.println("Policy count → " + policies.count());
            System.out.println("Customer count → " + customers.count());
            System.out.println("Lookup HDFC-LIFE-1004 customer → " +
                    customers.findByEmail("vikram.singh@hdfclife.example").orElseThrow().getFullName());
            System.out.println("Active count → " + policies.findByStatusOrderByPolicyNoAsc("Active").size());
            System.out.println("TERM count → " + policies.findByProductTypeOrderByPolicyNoAsc("TERM").size());
            System.out.println("Anita Sharma policy count → " + policies.findByCustomer_FullNameOrderByPolicyNoAsc("Anita Sharma").size());
            System.out.println("minPremium=20000 policy numbers → " +
                    policies.findWithPremiumAtLeast(20000).stream().map(Policy::getPolicyNo).reduce((a,b) -> a + ", " + b).orElse(""));
            System.out.println("HDFC-LIFE-1001 rider codes → " +
                    List.of(accident.getCode(), waiver.getCode()).stream().sorted()
                            .reduce((a,b) -> a + ", " + b).orElse(""));
        };
    }
}
