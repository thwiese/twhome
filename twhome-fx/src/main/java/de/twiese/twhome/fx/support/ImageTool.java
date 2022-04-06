package de.twiese.twhome.fx.support;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import de.twiese.twhome.fx.config.Config;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ImageTool {

    private static final Logger log = LoggerFactory.getLogger(ImageTool.class);
    private static final int MAX_IMAGE_SIZE = Config.getIntProperty("maxImageSize");

    public static BufferedImage getScaledImage(URI fileUri, int width) throws IOException {
        final InputStream is = fileUri.toURL().openStream();
        byte[] imgData = new byte[is.available()];
        is.read(imgData);
        is.close();
        int rotation = calculateRotation(imgData);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgData));
        img = rotateImage(img, rotation);

        if (width > MAX_IMAGE_SIZE) {
            width = MAX_IMAGE_SIZE;
        }
        int height = (img.getHeight() * width) / img.getWidth();
        if (height > MAX_IMAGE_SIZE) {
            height = MAX_IMAGE_SIZE;
            width = (img.getWidth() * height) / img.getHeight();
        }
        log.info("Reading image {} (width: {}, height: {}, scaling to width: {}, height: {}, rotation: {})", fileUri, img.getWidth(), img.getHeight(), width, height, rotation);
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

    public static BufferedImage rotateImage(final BufferedImage src, int angel) {
        int srcWidth = src.getWidth(null);
        int srcHeight = src.getHeight(null);
        // Calculate the size of the rotated image
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(srcWidth, srcHeight)), angel);
        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // convert
        g2.translate((rect_des.width - srcWidth) / 2,(rect_des.height - srcHeight) / 2);
        g2.rotate(Math.toRadians(angel), srcWidth / 2, srcHeight / 2);
        g2.drawImage(src, null, null);
        return res;
    }

    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // If the angle of rotation is greater than 90 Do the corresponding conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angelDeltaWidth = Math.atan((double) src.height / src.width);
        double angelDeltaHeight = Math.atan((double) src.width / src.height);
        int lenDeltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha - angelDeltaWidth));
        int lenDeltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha - angelDeltaHeight));
        int desWidth = src.width + lenDeltaWidth * 2;
        int desHeight = src.height + lenDeltaHeight * 2;
        return new Rectangle(new Dimension(desWidth, desHeight));
    }

    public static int calculateRotation(byte[] image) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(image));
            StringBuilder description = new StringBuilder();
            metadata.getDirectories().forEach(directory -> {
                directory.getTags().forEach(tag -> {
                    if (tag.getTagType() == ExifDirectoryBase.TAG_ORIENTATION) {
                        description.append(tag.getDescription().replaceAll(" ", ""));
                    }
                });
            });
            if (description.length() > 0) {
                int rotateIndex = description.indexOf("Rotate");
                int cwIndex = description.indexOf("CW");
                if (rotateIndex >= 0 && cwIndex > 0 && rotateIndex < cwIndex) {
                    return Integer.valueOf(description.substring(rotateIndex + 6, cwIndex));
                }
            }
            return -1;
        } catch (ImageProcessingException | IOException e) {
            log.error("rotating image failed", e);
            return -1;
        }
    }
}
