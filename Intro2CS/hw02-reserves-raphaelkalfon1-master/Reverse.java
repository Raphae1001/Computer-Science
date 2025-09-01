public class Reverse {
    public static void main(String[] args) {
        String i = args[0];
        int n = i.length() - 1;
        int a = n / 2;
        char b = i.charAt(a);
        String c = "";

        for (; n >= 0; n--) {
            c += i.charAt(n);
        }
        System.out.println(c);

        System.out.println("The middle character is " + b);
    }
}
