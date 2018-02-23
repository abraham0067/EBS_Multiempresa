/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.security;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class MyResponseRequestWrapper extends HttpServletResponseWrapper {

    public MyResponseRequestWrapper(HttpServletResponse response) {
        super(response);
    }

    
    @Override
    public String getHeader(String name) {
        // TODO Auto-generated method stub
        return super.getHeader(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        // TODO Auto-generated method stub
        return super.getHeaderNames();
    }

    @Override
    public void addHeader(String name, String value) {
        super.addHeader(name, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value); //To change body of generated methods, choose Tools | Templates.
    }

//    protected void doFilterInternal(HttpServletRequest request,
//            HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        HttpServletResponse myResponse = (HttpServletResponse) response;
//        MyResponseRequestWrapper responseWrapper = new MyResponseRequestWrapper(myResponse);
//        responseWrapper.addHeader("X-XSS-Protection", "1");
//        responseWrapper.addHeader("secure", "*");
//        filterChain.doFilter(request, myResponse);
//    }
}
