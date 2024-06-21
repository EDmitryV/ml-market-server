package com.edmitryv.ml_market_server.core.controllers;

import com.edmitryv.ml_market_server.authentication.models.Role;
import com.edmitryv.ml_market_server.core.dtos.ImageDTO;
import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.ImageService;
import com.edmitryv.ml_market_server.core.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {
    private final ImageService imageService;
    private final MediaService mediaService;

    @GetMapping(value = "/get/{id}", produces = "image/jpg")
    public ResponseEntity<Resource> getImage(@PathVariable Long id
    ) {
        if (id <= 0) return ResponseEntity.badRequest().body(null);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("image/jpg"))
                .body(imageService.getImageById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
    @AuthenticationPrincipal User user) throws IOException {
        Image image;
        image = new Image();
        image.setOwner(user);
        image = imageService.save(image);
        mediaService.saveFile(file, image);
        return new ResponseEntity<>(new ImageDTO(image), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable("mediaType") String mediaType, @PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        Image image = imageService.findById(id);
        if (image == null)
            return ResponseEntity.badRequest().body("Error: %s with id %s doesn't exists".formatted(mediaType, id));
        if (!Objects.equals(image.getOwner().getId(), user.getId()) || user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR_MARKETPLACE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: user isn't the owner of %s, and don't have right of edit".formatted(mediaType));
        imageService.delete(image);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
