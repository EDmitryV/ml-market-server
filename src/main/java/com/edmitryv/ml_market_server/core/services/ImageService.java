package com.edmitryv.ml_market_server.core.services;

import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.repos.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageService {
    @Autowired
    private final ImageRepository imageRepository;
    @Autowired
    private final MediaService mediaService;

    public Image findById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public Resource getImageById(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null)
            throw new IllegalArgumentException("Image with that id doesn't exists: " + id);
        return mediaService.load(image);
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public void saveWithFile(Image image, MultipartFile file) {
        try {
            mediaService.saveFile(file, image);
            save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(Image image) {
        mediaService.delete(image);
        imageRepository.delete(image);
    }

    public boolean existsById(Long imageId) {
        return imageRepository.existsById(imageId);
    }
}
