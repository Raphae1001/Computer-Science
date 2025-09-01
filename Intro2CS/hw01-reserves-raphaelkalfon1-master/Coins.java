public class Coins {
    public static void main(String[] args) {
        int y = 25;
        int x = 1;
        
        int v = Integer.parseInt(args[0]);
        
        // k equal to the entire part of the division.
        int k = v / y;

        // z equal v modulo y, so that it gives me the rest of the division of v/y.
        int z = v % y;
        // here, x=1, so it is the same but if x=2 cents then z will not be v modulo y.
        z = z/x;

        System.out.println("Use " + k + " quarters and " + z + " cents");
    }
}
