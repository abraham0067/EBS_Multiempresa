/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import fe.db.MConfig;
import fe.model.util.hibernateutil.HibernateUtilApl;

import java.io.Serializable;

/**
 * @author Lily
 */
public class SendMail implements Serializable {

    private Properties props = new Properties();
    private javax.mail.Session session;

    private String servidorSMTP;
    private String puerto;
    private String usuario;
    private String password;

    /*private String servidorSMTP = "smtp.sendgrid.net";
    private String puerto = "25";
    private String usuario = "buzon@ebs.com.mx";
    private String password = "Buz0n12ebsebs"; */


    private HibernateUtilApl hibManagerRO;//Read only interface


    @SuppressWarnings("unchecked")
    public SendMail() {
        ArrayList<MConfig> listaConfig = null;
        hibManagerRO = new HibernateUtilApl();
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConfig.class);
            cr.add(Restrictions.ilike("clasificacion", "CORREO"));
            listaConfig = (ArrayList<MConfig>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        if (listaConfig != null) {
            for (int i = 0; i < listaConfig.size(); i++) {
                if (listaConfig.get(i).getDato() != null && !listaConfig.get(i).getDato().isEmpty()) {
                    if (listaConfig.get(i).getDato().equals("SERVIDOR")) {
                        servidorSMTP = listaConfig.get(i).getValor();
                    } else if (listaConfig.get(i).getDato().equals("PORT")) {
                        puerto = listaConfig.get(i).getValor();
                    } else if (listaConfig.get(i).getDato().equals("AUTH_USER")) {
                        usuario = listaConfig.get(i).getValor();
                    } else if (listaConfig.get(i).getDato().equals("AUTH_PWD")) {
                        password = listaConfig.get(i).getValor();
                    }
                }
            }
        }
    }

    public boolean sendEmail(String asunto, String mensaje, String mails[]) {
        boolean resp = false;
        try {
            System.out.println("SENDING MAIL USING:");
            System.out.println("mail.smtp.host: " + servidorSMTP);
            System.out.println("mail.smtp.port: " + puerto);
            System.out.println("mail.smtp.user: " + usuario);

            props.setProperty("mail.smtp.host", servidorSMTP);
            props.setProperty("mail.smtp.starttls.enable", "true");
            // Puerto de gmail para envio de correos
            props.setProperty("mail.smtp.port", puerto);
            // Nombre del usuario
            props.setProperty("mail.smtp.user", usuario);
            // Si requiere o no usuario y password para conectarse.
            props.setProperty("mail.smtp.auth", "true");


            SMTPAuthenticator smtpauthenticator = new SMTPAuthenticator();
            session = javax.mail.Session.getDefaultInstance(props, smtpauthenticator);
            session.setDebug(false);
            MimeMessage mimemessage = new MimeMessage(session);
//			System.out.println("usuario"+ usuario);
            InternetAddress internetaddress = new InternetAddress(usuario);
            mimemessage.setFrom(internetaddress);
            InternetAddress ainternetaddress[] = new InternetAddress[mails.length];
            for (int i = 0; i < mails.length; i++) {
                ainternetaddress[i] = new InternetAddress(mails[i]);
            }
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, ainternetaddress);
            mimemessage.setSubject(asunto);
            mimemessage.setSentDate(new Date());
            mimemessage.setText(mensaje);

            Transport transport = session.getTransport("smtp");
            transport.send(mimemessage);
            transport.close();

            resp = true;
        } catch (MessagingException me) {
            me.printStackTrace(System.err);
            resp = false;
        }
        return resp;
    }

    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usuario, password);
        }

        private SMTPAuthenticator() {
            super();
        }
    }
}
