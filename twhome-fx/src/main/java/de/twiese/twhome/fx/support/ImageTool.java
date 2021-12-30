package de.twiese.twhome.fx.support;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

public class ImageTool {

    private static final Logger log = LoggerFactory.getLogger(ImageTool.class);

    public static BufferedImage getScaledImage(URI fileUri, int width) throws IOException {
        BufferedImage img = ImageIO.read(fileUri.toURL());
        int height = img.getHeight() * width / img.getWidth();
        log.info("Reading image {} (width: {}, height: {}, scaling to width: {}, height: {})", fileUri, img.getWidth(), img.getHeight(), width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics()
                .drawImage(img, 0, 0, width, height, null);
        return bufferedImage;
    }

    public static javafx.scene.image.Image convertImage(BufferedImage srcImage) {
        WritableImage convertedImage = new WritableImage(srcImage.getWidth(), srcImage.getHeight());
        PixelWriter pw = convertedImage.getPixelWriter();
        for (int x = 0; x < srcImage.getWidth(); x++) {
            for (int y = 0; y < srcImage.getHeight(); y++) {
                pw.setArgb(x, y, (srcImage).getRGB(x, y));
            }
        }
        return convertedImage;
    }
}