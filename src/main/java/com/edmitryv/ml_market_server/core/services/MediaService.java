package com.edmitryv.ml_market_server.core.services;

import com.edmitryv.ml_market_server.core.models.Image;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.edmitryv.ml_market_server.core.models.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MediaService {
    @Autowired
    private final ResourceLoader resourceLoader;

    public void init() {
        try {
            Files.createDirectories(Paths.get("data/image"));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Path getFilepath(Image image) {
        Path path = Path.of("%s/%s/%s%s".formatted(getResourcePath(), "data/image", image.getId(), ".jpg"));
        return path;
    }

    public void prepareFilePath(Path pathToFile) {
        try {
            Files.createDirectories(pathToFile.getParent());
            System.out.println("Folders created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create folders.");
        }
    }

    public String getResourcePath() {
        Resource resource = resourceLoader.getResource("classpath:");
        String path = null;
        try {
            path = resource.getFile().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public void saveFile(MultipartFile file, Image image) throws IOException {
        try {
            Path path = getFilepath(image);
            prepareFilePath(path);
            file.transferTo(new File(path.toString()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }


    public Resource load(Image image) {
        try {
            Resource resource = new FileSystemResource(getFilepath(image));

            if (resource.exists() || resource.isReadable() ) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void delete( Image image) {
        Path path = getFilepath(image);
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}