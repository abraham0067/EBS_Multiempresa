/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author <Eduardo at EBS>
 */
public class CSRFSessionListener implements HttpSessionListener {

    private String CSRFTOKEN_NAME = "token";
    
    /**
     * Recieves notification that a session  has been created
     * @param event 
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
//        System.out.println("Session Createds");
        HttpSession session = event.getSession();
        String randomId = generateRandomId();
        session.setAttribute(CSRFTOKEN_NAME, randomId);
    }

    private String generateRandomId() {
        String randomID = "";
        try {

            // Generate a random string
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // inizialize a MessageDigest
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            // create a MessageDigest of the random number
            byte[] randomDigest = sha.digest(new Integer(random.nextInt()).toString().getBytes());
            // encode the byte[] into some textual representation
            randomID = hexEncode(randomDigest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CSRFSessionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomID;
    }

    /**
     * The byte[] returned by MessageDigest does not have a nice
     * textual representation, so some form of encoding is usually performed.
     *
     * This implementation follows the example of David Flanagan's book
     * "Java In A Nutshell", and converts a byte array into a String
     * of hex characters.
     *
     * Another popular alternative is to use a "Base64" encoding.
     */
    static private String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }
    
    /**
     * Recieves a notification that about to be invalidated
     * @param se 
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}
