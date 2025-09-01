public class Triangle {
    public static void main(String[] args){
     int a = Integer.parseInt(args[0]);
     int b = Integer.parseInt(args[1]);
     int c = Integer.parseInt(args[2]);
        // I want that all the different some of two of the side is less than the third one. then I use the sign AND
     while ( a+b<c || a+c<b || b+c<a) {
        System.out.println(a + ", " + b + ", " + c + ": false");
        return;
     }
     System.out.println(a + ", " + b + ", " + c + ": true");

    }
}
