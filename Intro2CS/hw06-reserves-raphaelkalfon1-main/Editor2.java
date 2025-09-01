import java.awt.Color;

public class Editor2 {

    public static void main(String[] args) {
        // Check for the correct number of command-line arguments
        if (args.length != 3) {
            System.err.println("Usage: java Editor2 <image_file.ppm> <new_width> <new_height>");
            System.exit(1); // Exit with an error code
        }

        String fileName = args[0]; // The filename of the PPM image
        int width = Integer.parseInt(args[1]); // The desired width for the scaled image
        int height = Integer.parseInt(args[2]); // The desired height for the scaled image

        // Read the original image using Runigram.read
        Color[][] imageIn = Runigram.read(fileName);

        // Check if the image was successfully read
        if (imageIn == null) {
            System.err.println("Failed to read the image file: " + fileName);
            System.exit(1); // Exit with an error code
        }

        // Scale the image to the desired dimensions using Runigram.scaled
        Color[][] scaledImage = Runigram.scaled(imageIn, width, height);

        // Display the original image
        Runigram.setCanvas(imageIn);
        Runigram.display(imageIn);
        StdDraw.pause(3000); // Pause for 3 seconds

        // Display the scaled image
        Runigram.setCanvas(scaledImage);
        Runigram.display(scaledImage);
    }
}
