import java.awt.Color;

public class Editor1 {

    public static void main(String[] args) {
        // Ensure that exactly two command-line arguments are provided
        if (args.length != 2) {
            System.err.println("Usage: java Editor1 <image_file.ppm> <action>");
            System.exit(1); // Exit the program with an error status
        }

        String fileName = args[0]; // The filename of the PPM image
        String action = args[1]; // The action to be performed ("fh", "fv", or "gs")

        // Read the input image using the read method from Runigram
        Color[][] imageIn = Runigram.read(fileName);
        Color[][] imageOut = null; // This will hold the processed image

        // Determine the action and apply the corresponding image processing operation
        switch (action) {
            case "fh": // Horizontal flip
                imageOut = Runigram.flippedHorizontally(imageIn);
                break;
            case "fv": // Vertical flip
                imageOut = Runigram.flippedVertically(imageIn);
                break;
            case "gs": // Grayscale conversion
                imageOut = Runigram.grayScaled(imageIn);
                break;
            default: // Invalid action
                System.err.println("Invalid action: " + action);
                System.err.println("Valid actions are 'fh' for horizontal flip, 'fv' for vertical flip, and 'gs' for grayscale.");
                System.exit(1); // Exit the program with an error status
        }

        // Display the original and processed images
        Runigram.setCanvas(imageIn); // Set the canvas size based on the input image
        Runigram.display(imageIn); // Display the original image
        StdDraw.pause(3000); // Wait for 3 seconds before displaying the processed image
        Runigram.setCanvas(imageOut); // Set the canvas size based on the processed image
        Runigram.display(imageOut); // Display the processed image
    }
}
