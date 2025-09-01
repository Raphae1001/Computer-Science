public class GenThree {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);

        // I use if and else if to put any variable I want first

        if (a >= b) {

        int u;
        int v;
        int w; 
        
        // give a random number for each 
        double r = Math.random();
        double s = Math.random();
        double t = Math.random();
        
        // because this function give random from 0 to 1, then i multiplie it by the difference between the two variables and add 1, then a to be sure that my numbers are between a and b
        u = (int) (r * (b - a + 1) + a) ; 
        v = (int) (s * (b - a + 1) + a) ;
        w = (int) (t * (b - a + 1) + a) ;
     
        System.out.println(u);
        System.out.println(v);
        System.out.println(w);
        int minimal = (u <= v && u <= w) ? u : ((v <= u && v <= w) ? v : w);
        System.out.println("The minimal generated number ia " + minimal); 
        return;
        }
        else if ((b >= a)) {

        int u;
        int v;
        int w; 

        double r = Math.random();
        double s = Math.random();
        double t = Math.random();
        
        u = (int) (r * (a - b + 1) + b) ; 
        v = (int) (s * (a - b + 1) + b) ;
        w = (int) (t * (a - b + 1) + b) ;
     
        System.out.println(u);
        System.out.println(v);
        System.out.println(w);
        
        // give me the minimal number 
        int minimal = (u <= v && u <= w) ? u : ((v <= u && v <= w) ? v : w);
        System.out.println("The minimal number is " + minimal);
        return;
        }
    }
}
