package com.ebs.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Calendar;

/**
 * Created by eflores on 11/09/2017.
 */
@WebListener
public class HttpSessionChecker implements  HttpSessionListener {

    public void sessionCreated(HttpSessionEvent event) {
        System.out.printf("\n Session ID %s created at %s", event.getSession().getId(), Calendar.getInstance().getTime());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.printf("\n Session ID %s destroyed at %s", event.getSession().getId(), Calendar.getInstance().getTime());
    }
}
