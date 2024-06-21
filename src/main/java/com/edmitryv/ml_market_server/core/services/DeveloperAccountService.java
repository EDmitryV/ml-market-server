package com.edmitryv.ml_market_server.core.services;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.core.repos.DeveloperAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperAccountService {
    @Autowired
    private DeveloperAccountRepository developerAccountRepository;

    public DeveloperAccount findById(Long id){
        return developerAccountRepository.findById(id).orElse(null);
    }
    public DeveloperAccount save(DeveloperAccount developerAccount){
        return developerAccountRepository.save(developerAccount);
    }

    public void deleteById(Long id){
        developerAccountRepository.deleteById(id);
    }
}
