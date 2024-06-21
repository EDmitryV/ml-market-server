package com.edmitryv.ml_market_server.core.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.repos.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerAccountService {
    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    public CustomerAccount getCustomerAccountServiceById(Long id){
        return customerAccountRepository.findById(id).orElse(null);
    }
    public CustomerAccount saveCustomerAccount(CustomerAccount customerAccount){
        return customerAccountRepository.save(customerAccount);
    }

    public void deleteCustomerAccountById(Long id){
        customerAccountRepository.deleteById(id);
    }
}
