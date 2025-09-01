/** String processing exercise 1. */
public class LowerCase {
    public static void main(String[] args) {  
        String str = args[0];
        System.out.println(lowerCase(str));
    }

   /**
    * Returns a string which is identical to the original string, 
    * except that all the upper-case letters are converted to lower-case letters.
    * Non-letter characters are left as is.
    */
    public static String lowerCase(String s) {
        int i = 0; 
        int a= s.length();
        String b = "";
        while (i < a) {
            char c = s.charAt(i);
            if ( 64<c && c<91 ) {
                b = b + (char)(c + 32);
            }
            else {
               b = b + c; 
            }
            i++;
        }

        return b;
    }
}
