package com.ventuit.adminstrativeapp.core.services;

import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import io.trbl.blurhash.BlurHash;

@Service
public class BlurHashService {
    /**
     * Generate a blurhash for an image InputStream
     * 
     * @param inputStream the image input stream
     * @param xComponents number of X components (e.g., 4)
     * @param yComponents number of Y components (e.g., 3)
     * @return the blurhash string, or null if not an image or error
     */
    public String generateBlurHash(InputStream inputStream, int xComponents, int yComponents) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null)
                return null;
            return BlurHash.encode(image, xComponents, yComponents);
        } catch (Exception e) {
            return null;
        }
    }
}