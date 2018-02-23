package com.ebs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eflores on 11/10/2017.
 */
public class CFDIStringUtils {
    public String getUiid( String str) {
        String  uuid = null;
        Matcher mat = Pattern.compile("UUID=\".{36}\"").matcher(str);
        if (mat.find()) {
            uuid = mat.group().substring(6, 42);
        }
        return uuid;
    }
}
