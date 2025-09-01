public class Perfect {
    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        int sum = 1;  // First divisor is 1
        String divisors = "1";

        for (int i = 2; i <= N / 2; i++) {
            if (N % i == 0) {
                sum += i;
                divisors += " + " + i;
            }
        }

        if (sum == N) {
            System.out.println(N + " is a perfect number since " + N + " = " + divisors);
        } else {
            System.out.println(N + " is not a perfect number");
        }
    }
}
