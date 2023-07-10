package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserRepository userRepository;

    @Value("${app.image.bucket}")
    private String bucket; //где храниться картинка

    public void upload(String imagePath, InputStream content) throws IOException {
        Path fullImagePath = Path.of(bucket, imagePath);

        try (content) {
            Files.write(fullImagePath, content.readAllBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);
        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }

}
