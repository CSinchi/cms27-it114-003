import java.util.Arrays;

public class Problem3 {
    public static void main(String[] args) {
        //Don't edit anything here
        Integer[] a1 = new Integer[]{-1, -2, -3, -4, -5, -6, -7, -8, -9, -10};
        Integer[] a2 = new Integer[]{-1, 1, -2, 2, 3, -3, -4, 5};
        Double[] a3 = new Double[]{-0.01, -0.0001, -.15};
        String[] a4 = new String[]{"-1", "2", "-3", "4", "-5", "5", "-6", "6", "-7", "7"};
        
        bePositive(a1);
        bePositive(a2);
        bePositive(a3);
        bePositive(a4);
    }
    // <T> turns this into a generic so it can take in any datatype, it'll be passed as an Object so casting is required
    static <T> void bePositive(T[] arr){
        System.out.println("Processing Array:" + Arrays.toString(arr));
        //your code should set the indexes of this array
        Object[] output = new Object[arr.length];
        //hint: use the arr variable; don't diretly use the a1-a4 variables  UCID: cms27 Date: 9/25
        //TODO convert each value to positive
        for (int i = 0; i < output.length; i++) //loops on "output" indexs
        {
            Object arrNumObject = arr[i];  //creates an new temp object based on the object at current index of "arr"
            if (arr[i] instanceof String){ //If that object in "arr" is a String
                String s = arrNumObject.toString(); //creates a temp String "s" based from "arrNumObject"
                int num = Integer.parseInt(s);//creates a temp int "num" by converting "s" into a int
                int p = Math.abs(num); //creates a temp int 'p' by getting the Absolute Value of num
                arrNumObject = Integer.toString(p);//turns "arrNumObject" into the converted String of positive 'p'
            }

            else if(arr[i] instanceof Double) { // Else If that object in "arr" is a Double
                Double od = (Double) arrNumObject; //creates a temp Double "od" based from "arrNumObjects"
                double d = Math.abs(od); //
                Double rd = d;
                arrNumObject = rd;          
            }

            else if(arr[i] instanceof Integer){ // Else If that object in "arr" is a Integer
                Integer itg = (Integer) arrNumObject;
                int ig = Math.abs(itg);
                Integer ni = ig;
                arrNumObject = ni;
            }

            output[i] = arrNumObject;
            
        }
        //set the result to the proper index of the output array
        //hint: don't forget to handle the data types properly, the result datatype should be the same as the original datatype
        
        //end edit section

        StringBuilder sb = new StringBuilder();
        for(Object i : output){
            if(sb.length() > 0){
                sb.append(",");
            }
            sb.append(String.format("%s (%s)", i, i.getClass().getSimpleName().substring(0,1)));
        }
        System.out.println("Result: " + sb.toString());
    }
}
