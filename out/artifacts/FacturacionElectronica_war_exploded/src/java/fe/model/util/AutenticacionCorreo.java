/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author Lily
 */
public class AutenticacionCorreo extends Authenticator {

    String user;
    String pw;

    public AutenticacionCorreo(String username, String password) {
        super();
        this.user = username;
        this.pw = password;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pw);
    }
}
