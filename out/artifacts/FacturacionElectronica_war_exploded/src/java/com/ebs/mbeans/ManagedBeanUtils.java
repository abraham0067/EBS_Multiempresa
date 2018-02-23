/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ebs.mbeans;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ManagedBeanUtils implements Serializable {

    /** Creates a new instance of ManagedBeanUtils */
    public ManagedBeanUtils() {
    }
    
    @PostConstruct
    public void init(){

    }
    
    public Date  parseDate(String dateInString){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = sdf.parse(dateInString);
            return date;
        } catch (ParseException ex) {
            Logger.getLogger(ManagedBeanUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }
    
    public String parseString(Date arg){
        if(arg == null)
            return "---";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = sdf.format(arg);
        return date;
    }
    

}
