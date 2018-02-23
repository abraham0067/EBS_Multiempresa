package com.ebs;
import java.math.*;

/**
 * Created by eflores on 07/09/2017.
 */

public class BigDecimalDemo {

    public static void main(String[] args) {

        // create 2 BigDecimal objects
        BigDecimal bg1, bg2;

        bg1 = new BigDecimal("10");
        bg2 = new BigDecimal("20");

        //create int object
        int res;

        res = bg1.compareTo(bg2); // compare bg1 with bg2

        String str1 = "Both values are equal ";
        String str2 = "First Value is greater ";
        String str3 = "Second value is greater";

        if( res == 0 )
            System.out.println( str1 );
        else if( res == 1 )
            System.out.println( str2 );
        else if( res == -1 )
            System.out.println( str3 );


        bg1 = new BigDecimal("999999999999999999.999999");//String
        bg1 = new BigDecimal("999999999999999999.");//return 99999999999999 with no decimal part
        try{
            bg1 = new BigDecimal("");//Empty string
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        try {
            bg1 = new BigDecimal("a");//not a number string
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
}