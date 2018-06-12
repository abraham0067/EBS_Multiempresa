package fe.model.dao;

import com.ebs.mbeans.ManagedBeanLogin;
import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import org.bouncycastle.util.encoders.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Carlo
 */
public class ControlAcceso implements Serializable {

    private HttpServletRequest request;
    private MAcceso acceso;
    private ManagedBeanLogin login;

    public ControlAcceso() {

    }

    public ControlAcceso(ManagedBeanLogin l, HttpServletRequest request) {
        try {
            this.request = request;
            login = l;
            acceso = login.getmAcceso();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public boolean validaUsusario(String pass) throws Exception {
        boolean resp = false;
        if (acceso != null && acceso.getEmpresa() != null) {
            System.out.println("Login ESTATUS empresa: " + acceso.getEmpresa().getEstatusEmpresa());
            //login.setMensaje("Tu información de inicio de sesión y contraseña están incorrectos");
            if (acceso.getEmpresa().getEstatusEmpresa() == 1) {
                resp = chkUserPass(pass);
                //System.out.println("validaUsusario2");

            } else {
                //System.out.println("validaUsusario3");
                login.setMensaje("La empresa " + acceso.getEmpresa().getRfcOrigen() + " a la cual, pertenece el usuario " + acceso.getUsuario() + " no esta activa, Favor de verificarla con el proveedor. ");
                resp = false;
            }
        } else if (acceso.getNivel() == Nivel.INTERNO) {//Acceso incluso sin empresas asociadas????
            resp = true;
        }

        return resp;
    }


    private boolean chkUserPass(String clave) throws Exception {
        //0-Bloqueo permanente
        //1-activo
        //2-boqueo temporal
        //3-pendiente de confirmacion
        if (acceso.getEstatus().intValue() == 0) {
            login.setMensaje("Este usuario ha sido bloqueado permanentemente. Contacte al administrador.");
            return false;
        } else if (acceso.getEstatus().intValue() == 3) {
            login.setMensaje(
                    "El usuario "
                    + acceso.getUsuario()
                    + " necesita confirmar su registro, con el link que se le envio a su correo"
            );
            return false;
        }
        
        String pw = this.getEncriptPW(acceso.getUsuario(), clave);              //Con RFC como nick
        String pw2 = this.getEncriptPW(acceso.getEmail(), clave);               //Con email como nick
        boolean loginSucces = false;
        if (acceso.getClave().equals(pw) || acceso.getClave().equals(pw2)) {
            loginSucces = true;
        }
        if (!loginSucces) {//Erroneous login
            Calendar cal = Calendar.getInstance();
            cal.setTime(acceso.getUltimoIntento());
            cal.add(Calendar.MINUTE, cal.MINUTE + 15); // 15 minutos de timeout
            
            acceso.setUltimoIntento(new Date());
            
            if (cal.getTime().before(new Date()) && acceso.getEstatus() != 0) {
                acceso.setIntentos(0);
                acceso.setEstatus(1);
            } else {
                acceso.setIntentos(acceso.getIntentos() + 1);
            }
            
            // Determina el estatus
            if (acceso.getIntentos() == 3) {
                acceso.setEstatus(2); // Bloqueo temporal
            }
            if (acceso.getIntentos() > 3) {
                acceso.setEstatus(0); // Bloqueo permanente
            }
            
            login.guardarActualizarAcceso(acceso);//Update the acces
            getMessage();
            return false;
        } else{//Login succesfull
            if (chkChangePW()) {
                return false;
            } else {
                return setUserOk();
            }
        }
    }

    private void getMessage() {
        // hay que revisar si el usuario esta� bloqueado temporalmente o
        // permanentemente
        // se retorna un valor booleano que indica si el usuario puede continuar
        boolean continuar = true;
        if (acceso.getEstatus() == 0) {
//			login.addFieldError("error", "Usuario " + acceso.getUsuario()
//					+ " esta bloquedo permanente.");

//            System.out.println("chkBloqueo");
            login.setMensaje(
                    "Usuario " + acceso.getUsuario()
                    + " esta bloquedo permanente."
            );
        } else if (acceso.getEstatus() == 2) {
//			login.addFieldError("error", "Usuario " + acceso.getUsuario()
//					+ " esta bloquedo temporalmente por 15 min.");
//            System.out.println("chkBloqueo2");
            login.setMensaje(
                    "Usuario " + acceso.getUsuario()
                    + " esta bloquedo temporalmente por 15 min, el siguiente intento de acceso "
                            + "erroneo antes del tiempo establecido ocasionara el bloqueo permanente de este usuario."
            );
        }
    }

    // retorna verdadero en caso de que el usuario tenga que cambiar su
    // contraseña
    // por primera vez
    private boolean chkChangePW() {
        if (acceso.getCambiaClave().intValue() == 1) {
            
            login.setCambiarClave(true);
            return true;
        }
        return false;
    }

    private boolean setUserOk() throws Exception {
        acceso.setIntentos(0);
        acceso.setEstatus(1);
        acceso.setUltimoAcceso(new Date());
        if (login.guardarActualizarAcceso(acceso)) {

            // Inicia Session
            HttpSession session = request.getSession(true);
            if (acceso.getPerfil().getTimeOut().intValue() > 0) {
                session.setMaxInactiveInterval(acceso.getPerfil().getTimeOut()
                        .intValue() * 60);
            }
            session.setAttribute("acceso", acceso);
            session.setAttribute("empresa", acceso.getEmpresa()
                    .getRazonSocial());
//			session.setAttribute("logo",
//					((acceso.getEmpresa().getLogo() != null && !acceso
//							.getEmpresa().getLogo().trim().equals("")) ? acceso
//							.getEmpresa().getLogo() : "EBS.gif"));
//			session.setAttribute("colorweb", ((acceso.getEmpresa()
//					.getColorWeb() != null && !acceso.getEmpresa()
//					.getColorWeb().trim().equals("")) ? acceso.getEmpresa()
//					.getColorWeb() : ""));
            return true;
        }
        return false;
    }

    public synchronized String getMenu(String menu) {
        String mnuStr = "";
        return mnuStr;
    }

    public byte[] makeDigest(String user, String pwd)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(user.getBytes());
        md.update(pwd.getBytes());
        return md.digest();
    }

    @SuppressWarnings("static-access")
    public String getEncriptPW(String user, String pw) {
        String resp = "";
        try {
            byte[] b = makeDigest(user, pw);
            Base64 b64e = new Base64();
            resp = new String(b64e.encode(b));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return resp;
    }
    //Comparando con la clase Control Acceso del proyecto de contabilidad hace
    //falta un metodo para generar una contraseña aleatoria passAleatorio();
}
