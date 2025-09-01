public class InOrder {
    public static void main(String[] args) {
        int x = (int) (Math.random() * 10);
        System.out.print(x + " ");

        int y = x + 1;

        do {
            y = (int) (Math.random() * 10);

            if (y >= x) {
                System.out.print(y + " ");
                x = y; // Update x to the current value of y
                y = (int) (Math.random() * 10);
            } 
        } while (x <= y); 
    }
}
