/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fe.model.util;

/**
 *
 * @author Liliana
 */
public class Perfiles {
    public static void main(String[] paramArrayOfString)
    {
        System.out.println(hasAccess(65L, 1L));
    }

   public static boolean hasAccess(long paramLong1, long paramLong2)
   {
        return (paramLong2 & paramLong1) == paramLong2;
   }

}
