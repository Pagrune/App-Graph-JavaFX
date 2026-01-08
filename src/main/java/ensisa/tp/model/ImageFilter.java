package ensisa.tp.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageFilter {

    public WritableImage filter(Image img, Courbe rouge, Courbe vert, Courbe bleu) {

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        WritableImage filteredImage = new WritableImage(width, height);
        PixelReader reader = img.getPixelReader();
        PixelWriter writer = filteredImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int argb = reader.getArgb(x, y);

                int alpha =  argb & 0xff000000;
                int red   = (argb & 0x00ff0000) >> 16;
                int green = (argb & 0x0000ff00) >> 8;
                int blue  =  argb & 0x000000ff;

                red   = clamp(rouge.evaluate(red));
                green = clamp(vert.evaluate(green));
                blue  = clamp(bleu.evaluate(blue));

                int newArgb =
                        alpha |
                                (red   << 16) |
                                (green << 8)  |
                                blue;

                writer.setArgb(x, y, newArgb);
            }
        }

        return filteredImage;
    }

    /* =========================
       UTILS
       ========================= */

    private int clamp(double value) {
        return (int) Math.min(0, Math.max(255, value));
    }
}
