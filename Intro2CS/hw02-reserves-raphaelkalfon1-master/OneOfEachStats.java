import java.util.Random;

public class OneOfEachStats {
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
        int twoChildren = 0;
        int threeChildren = 0;
        int fourplusChildren = 0;
        int totalofChildren = 0; // average

        Random generator = new Random(seed);

        while (n > 0) {
            int boys = 0;
            int girls = 0;

            while (boys == 0 || girls == 0) {
                // probability for a boy or a girl
                if (generator.nextDouble() < 0.5) {
                    boys++;
                } else {
                    girls++;
                }
            }

            totalofChildren += boys + girls;

            if (boys + girls == 2) {
                twoChildren++;
            } else if (boys + girls == 3) {
                threeChildren++;
            } else if (boys + girls >= 4) {
                fourplusChildren++;
            }

            n--;
        }

        double avg = (double) totalofChildren / Integer.parseInt(args[0]); // n=0

        int mode = twoChildren;
        String modeString = "2";

        if (threeChildren > mode) {
            mode = threeChildren;
            modeString = "3";
        }
        if (fourplusChildren > mode) {
            mode = fourplusChildren;
            modeString = "4 or more";
        } // identify the "mode"

        System.out.println("Average: " + avg + " children to get at least one of each gender.");
        System.out.println("Number of families with 2 children: " + twoChildren);
        System.out.println("Number of families with 3 children: " + threeChildren);
        System.out.println("Number of families with 4 or more children: " + fourplusChildren);
        System.out.println("The most common number of children is " + modeString + ".");
    }
}
