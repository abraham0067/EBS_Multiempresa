/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util.hibernateutil;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ClassTest {
    public static String est;
    public ClassTest(){
        System.out.println("Void");
    }
    
    public ClassTest(String test){
        System.out.println("ARG init");
    }
    static {
        System.out.println("static");
    }
}
