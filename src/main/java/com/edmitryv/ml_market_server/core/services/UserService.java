package com.edmitryv.ml_market_server.core.services;


import com.edmitryv.ml_market_server.authentication.models.Role;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepo;


    public User save(User user) {
        if(user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepo.save(user);

    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }



    public boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public void deleteUser(User user){
        userRepo.delete(user);
    }



    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email){
        return userRepo.findByEmail(email).orElse(null);
    }
    public List<User> findByUsernameContaining(String name){
        return userRepo.findByUsernameContaining(name);
    }

    public User findById(Long id){
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElse(null);
    }
}
