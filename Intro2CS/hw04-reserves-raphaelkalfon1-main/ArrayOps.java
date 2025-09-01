public class ArrayOps {
    public static void main(String[] args) {
        
    }
    
    public static int findMissingInt (int [] array) {
        int n = array.length;
        int MissingValue = (n * (n + 1)) / 2;
        for( int i=0; i < n; i++)
        {
            MissingValue -= array[i];
        }

        return MissingValue ;
    }

    public static int secondMaxValue(int [] array) {
        int n = array.length;
        int max = Math.max(array[0], array[1]);
        int SecondMax = Math.min(array[0], array[1]);

        for (int i = 2; i < n; i++) {
            int value = array[i];

          if (value > max) {
            SecondMax = max;
            max = value;
          } 
          else if (value > SecondMax && value != max){
            SecondMax = value;
          }
        }
        return SecondMax;
    }

    public static boolean containsTheSameElements(int[] array1, int[] array2) {
        int n = array1.length;
        int m = array2.length;
    
        if (n != m) {
            return false;  
        }
    
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < m; j++) {
                if (array1[i] == array2[j]) {
                    found = true;
                    array2[j] = Integer.MIN_VALUE;
                    break;
                }
            }
            if (!found) {
                return false;  
            }
        }
        return true;
    }
    


    public static boolean isSorted(int [] array) {

            boolean increasing = true;
            boolean decreasing = true;
        
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i] < array[i + 1]) {
                    decreasing = false; 
                } else if (array[i] > array[i + 1]) {
                    increasing = false; 
                }
            }
        
            return increasing || decreasing;
        }
    }
        
