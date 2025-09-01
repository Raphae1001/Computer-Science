import java.awt.Color;

public class Editor3 {

    public static void main(String[] args) {
        // Ensure the correct number of command-line arguments are provided
        if (args.length != 3) {
            System.err.println("Usage: java Editor3 <source_image.ppm> <target_image.ppm> <steps>");
            System.exit(1); // Exit with an error status
        }

        String sourceFileName = args[0]; // The filename of the source PPM image
        String targetFileName = args[1]; // The filename of the target PPM image
        int steps = Integer.parseInt(args[2]); // The number of steps for the morphing process

        // Read the source and target images using Runigram.read
        Color[][] sourceImage = Runigram.read(sourceFileName);
        Color[][] targetImage = Runigram.read(targetFileName);

        // Check if the images were successfully read
        if (sourceImage == null || targetImage == null) {
            System.err.println("Failed to read one or both of the image files.");
            System.exit(1); // Exit with an error status
        }

        // Morph the source image into the target image using the specified number of steps
        Runigram.setCanvas(sourceImage); // Set the canvas size based on the source image
        Runigram.morph(sourceImage, targetImage, steps);
    }
}
