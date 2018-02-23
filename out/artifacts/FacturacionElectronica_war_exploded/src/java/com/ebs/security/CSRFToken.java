/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.security;

import javax.servlet.http.HttpServletRequest;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.reference.DefaultEncoder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class CSRFToken {
    //this code is in the DefaultUser implementation of ESAPI
//    1-Create the token
//    2-Add token to input and forms
//    3-verify token on server side
//    4-kill token on logout

    /**
     * This user's CSRF token.
     */
    private String csrfToken = resetCSRFToken();
    //from HTTPUtilitiles interface
    final static String CSRF_TOKEN_NAME = "ctoken";

    public String resetCSRFToken() {
        csrfToken = ESAPI.randomizer().getRandomString(8, EncoderConstants.CHAR_ALPHANUMERICS);
        return csrfToken;
    }

    //this code is from the DefaultHTTPUtilities implementation in ESAPI
    public String addCSRFToken(String href) {

        User user = ESAPI.authenticator().getCurrentUser();

        if (user.isAnonymous()) {

            return href;

        }

        // if there are already parameters append with &, otherwise append with ?
        String token = CSRF_TOKEN_NAME + "=" + user.getCSRFToken();

        return href.indexOf('?') != -1 ? href + "&" + token : href + "?" + token;

    }

    //this code is from the DefaultHTTPUtilities implementation in ESAPI
    public void verifyCSRFToken(HttpServletRequest request) throws IntrusionException {

        User user = ESAPI.authenticator().getCurrentUser();

        // check if user authenticated with this request - no CSRF protection required
        if (request.getAttribute(user.getCSRFToken()) != null) {

            return;

        }

        String token = request.getParameter(CSRF_TOKEN_NAME);

        if (!user.getCSRFToken().equals(token)) {

            throw new IntrusionException("Authentication failed", "Possibly forged HTTP request without proper CSRF token detected");

        }

    }

}
