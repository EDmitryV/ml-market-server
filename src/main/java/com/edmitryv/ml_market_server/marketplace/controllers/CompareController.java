package com.edmitryv.ml_market_server.marketplace.controllers;
import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.CustomerAccountService;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.marketplace.dtos.AppModelListDTO;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.services.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/compare")
@RequiredArgsConstructor
public class CompareController {
    @Autowired
    private final AppService appService;
    @Autowired
    private final CustomerAccountService customerAccountService;
    @Autowired
    private final UserService userService;

    @GetMapping("/getAllComparableApps")
    public ResponseEntity<?> getAllComparableApps() {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            Set<AppModel> apps = user.getCustomerAccount().getComparableApps();
            List<AppModelListDTO> appListDTOS = new ArrayList<>();
            for (AppModel app : apps) {
                appListDTOS.add(new AppModelListDTO(app));
            }
            return ResponseEntity.ok(appListDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/addAppToComparableById/{id}")
    public ResponseEntity<?> addAppToComparableById(@PathVariable Long id){
        try{
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            AppModel app = appService.findById(id);
            CustomerAccount customerAccount = user.getCustomerAccount();
            Set<CustomerAccount> newComparators = new HashSet<>();
            newComparators.addAll(app.getComparators());
            if(!newComparators.contains(customerAccount))
                newComparators.add(customerAccount);
            app.setComparators(newComparators);
            appService.save(app);
            Set<AppModel> newComparableApps = new HashSet<>();
            newComparableApps.addAll(customerAccount.getComparableApps());
            if(!newComparableApps.contains(app))
                newComparableApps.add(app);
            customerAccount.setComparableApps(newComparableApps);
            customerAccountService.saveCustomerAccount(customerAccount);
            return ResponseEntity.ok(null);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAppFromComparableById/{id}")
    public ResponseEntity<?> deleteAppFromComparableById(@PathVariable Long id){
        try{
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            AppModel app = appService.findById(id);
            CustomerAccount customerAccount = user.getCustomerAccount();
            Set<CustomerAccount> newComparators = new HashSet<>();
            newComparators.addAll(app.getComparators());
            newComparators.remove(customerAccount);
            app.setComparators(newComparators);
            appService.save(app);
            Set<AppModel> newComparableApps = new HashSet<>();
            newComparableApps.addAll(customerAccount.getComparableApps());
            newComparableApps.remove(app);
            customerAccount.setComparableApps(newComparableApps);
            customerAccountService.saveCustomerAccount(customerAccount);
            return ResponseEntity.ok(null);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
