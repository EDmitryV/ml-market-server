package com.edmitryv.ml_market_server.core.controllers;

import com.edmitryv.ml_market_server.core.dtos.UserProfileDTO;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.dtos.UpdateUserDTO;
import com.edmitryv.ml_market_server.core.services.ImageService;
import com.edmitryv.ml_market_server.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final ImageService imageService;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    @PatchMapping("/edit/me")
    public ResponseEntity<UserProfileDTO> editUser(@RequestBody UpdateUserDTO userdto) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User userWithSameUsername = userService.findByUsername(userdto.getUsername());
        User userWithSameEmail = userService.findByEmail(userdto.getEmail());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId()))
            throw new RuntimeException("User with this username already exists");
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId()))
            throw new RuntimeException("User with this email already exists");

        if (userdto.getUsername() != null) user.setUsername(userdto.getUsername());
        if (userdto.getEmail() != null) user.setEmail(userdto.getEmail());
        if (userdto.getProfileImageId() != -1) user.setProfileImage(imageService.findById(userdto.getProfileImageId()));
        if(userdto.getPassword() != null) user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(new UserProfileDTO(user), HttpStatus.OK);
    }

    @GetMapping("/get-info/{id}")
    public ResponseEntity<UserProfileDTO> getInfoByUserId(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(new UserProfileDTO(user),HttpStatus.OK );
    }
    @GetMapping("/get-info/me")
    public ResponseEntity<UserProfileDTO> getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        UserProfileDTO userDTO = new UserProfileDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<String> deleteUser() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.deleteUser(user);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

}