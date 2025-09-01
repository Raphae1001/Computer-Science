import java.awt.Color;

public class Editor4 {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("");
            System.exit(1);
        }

        String sourceFileName = args[0];
        int steps = Integer.parseInt(args[1]);

        Color[][] sourceImage = Runigram.read(sourceFileName);
        if (sourceImage == null) {
            System.err.println("Failed to read the source image.");
            System.exit(1);
        }

        Color[][] grayscaleImage = Runigram.grayScaled(sourceImage);

        Runigram.setCanvas(sourceImage);
        Runigram.morph(sourceImage, grayscaleImage, steps);
    }
}
