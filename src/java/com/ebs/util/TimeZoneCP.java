package com.ebs.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eflores on 04/12/2017.
 */
public class TimeZoneCP {
    /*
       ZONA PACIFICO
       Tijuana, Mexicali, Ensenada, Rosarito, Tecate

       zonaPacifico[] = {-2(HORAS A SUMAR/RESTAR),RANGO CP1,RANGO CP2);
   */
    private static Integer zonaPacifico[] = {-2,21000,22997};
    /*
       ZONA DE LA MONTANA
       BAJA CALIFORNIA SUR, CHIHUAHUA, NAYARIT, SINALOA, SONORA

       -1 HORA
   */
    private static Integer bajaCalifornia[] = {-1,23000,23997};
    private static Integer chihuahua[] = {-1,31000,33997};
    private static Integer sinaloa[] = {-1,80000,82996};
    private static Integer sonora[] = {-1,83000,85994};
    /*
        ZONA SURESTE
        QUINTANA ROO

        +1 HORA
    */
    private static Integer quintanaRoo[] = {1,77000,77997};
    private ArrayList<Integer[]> arr;

    /**
     * Instanciar la clase y llamar al metodo getUTC, el metodo regresa la fecha de acuerdo a la region
     *
     *  Ejemplo
     *  ZonaHoraria zh = new ZonaHoraria();
     *  Date date = zh.getUTC(CodigoPostal);
     *
     */
    public TimeZoneCP() {
        this.arr = new ArrayList<Integer[]>();
        this.arr.add(zonaPacifico);
        this.arr.add(bajaCalifornia);
        this.arr.add(chihuahua);
        this.arr.add(sinaloa);
        this.arr.add(sonora);
        this.arr.add(quintanaRoo);
    }

    //RECIBE CODIGO POSTAL COMO STRING
    public Date getUTC(String codigoPostal){
        if(codigoPostal != null && !codigoPostal.isEmpty())
            return getUTC(Integer.valueOf(codigoPostal.trim()));

        return new Date();
    }
    //RECIBE CODIGO POSTAL COMO INTEGER
    public Date getUTC(Integer codigoPostal){
        try {
            if(codigoPostal != null){
                //ITERA ENTRE LAS ZONAS
                for(Integer[] zona : this.arr) {
                    //SI EL CODIGO POSTAL SE ENCUENTRA DENTRO DEL RANGO REGRESA LA HORA QUE SE RESTARA
                    if(codigoPostal >= zona[1] && codigoPostal <= zona[2]){
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.HOUR, zona[0]);
                        return c.getTime();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("CP MAL FORMADO JMONTERO: "+codigoPostal);
            System.out.println("Error de cp: "+e.getMessage());
        }

        return new Date();
    }
}
