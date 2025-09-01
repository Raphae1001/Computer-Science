public class OneOfEach {
    public static void main(String[] args) {
        int boys = 0;
        int girls = 0;

        while (boys == 0 || girls == 0) {
            // probability for a boy or a girl
            if (Math.random() < 0.5) {
                System.out.print("b ");
                boys++;
            } else {
                System.out.print("g ");
                girls++;
            }

        }
                // go to the line with
                System.out.println();

         int totalofChildren = boys + girls ;

        System.out.println("You made it... and you now have " + totalofChildren + " children.");
    }
}
