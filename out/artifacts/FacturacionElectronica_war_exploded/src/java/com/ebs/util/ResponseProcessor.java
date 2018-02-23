package com.ebs.util;

import java.io.Serializable;

/**
 * Created by eflores on 06/06/2017.
 */
public interface ResponseProcessor extends Serializable{

    public String[] processResponse(String response);
}
