public class DamkaBoard {
    public static void main(String[] args) {
        // Size of the board 
        int x = Integer.parseInt(args[0]);

        for (int i = 0; i < x; i++) {
            // Add a space at the beginning of odd-numbered rows
            if (i % 2 == 1) {
                System.out.print(" ");
            }

            for (int j = 0; j < x; j++) {

		    System.out.print("*");
                // Add a space between asterisks, except for the last one in the row
                if (j < x - 1) {
                    System.out.print(" "); 
                }
            }
                // Add a space at the end of even-numbered rows
            if (i % 2 == 0 && i < x - 1) {
                System.out.print(" ");
            }
            
            // Move to the next line to start a new row
            System.out.println();
        }
    }
}
