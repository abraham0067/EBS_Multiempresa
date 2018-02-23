/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Lily
 */
public class ControlPasword implements Serializable{

    private Integer[] Listnum = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private String[] abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private String[] ABC = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] Caracteres = {"!", "#", "$", "%", "&", "(",")", "?", "-", "_", "{", "}", "[", "]", "="};
    private int Inicial = 0;
    private int Final = 25;
    private static final int MIN_PASS_LENGHT = 8;

    public ControlPasword() {
    }

    public String ValidaPass(String Pass) {
        String mensaje = "";
        if (Pass.length() >= MIN_PASS_LENGHT) {
            mensaje = ValidadCaracterEspecial(Pass);
            if (mensaje.equals("Correcto")) {
                if (Alfanumerico(Pass)) {
                    if (ValSecuenciaNum(Pass)) {
                        if (ValidaSecuenciaLetra(Pass)) {
                            mensaje = "Correcto";
                        } else {
                            mensaje = "El password contiene una secuencia de letras,  -- Favor de cambiarlo --";
                        }
                    } else {
                        mensaje = "El password contiene una secuencia de numeros, -- Favor de cambiarlo --- ";
                    }

                } else {
                    mensaje = "El password no es Alfanumerico. -- Favor de cambiarlo --";
                }
            }
        } else {
            mensaje = "El password debe ser mayor o igual a ocho caracteres.-- Favor de cambiarlo --";
        }
        return mensaje;


    }

    public String ValidaPassPasado(String NuevoPass, String PassAnte) {
        String mensaje = "";
        if (!NuevoPass.contains(PassAnte)) {
            if (NuevoPass.length() >= MIN_PASS_LENGHT) {
                mensaje = ValidadCaracterEspecial(NuevoPass);
                if (mensaje.equals("Correcto")) {
                    if (Alfanumerico(NuevoPass)) {
                        if (ValSecuenciaNum(NuevoPass)) {
                            if (ValidaSecuenciaLetra(NuevoPass)) {
                                mensaje = "Correcto";
                            } else {
                                mensaje = "El nuevo password contiene una secuencia de letras,¡Favor de cambiarlo! ";
                            }
                        } else {
                            mensaje = "El nuevo password contiene una secuencia de numeros,¡Favor de cambiarlo! ";
                        }

                    } else {
                        mensaje = "El nuevo password no es Alfanumerico, ¡Favor de cambiarlo!";
                    }
                }
            } else {
                mensaje = "El nuevo password debe ser mayor o igual a ocho caracteres, ¡Favor de cambiarlo!";
            }
        } else {
            mensaje = "El nuevo password contiene la contraseña anterior, ¡Favor de cambiarlo!";
        }
        return mensaje;
    }

    private boolean Alfanumerico(String nuevoPass) {
        boolean value = false;
        boolean numeri = false;
        boolean letras = false;
        boolean caracter = false;


        int TotalL = nuevoPass.length();
        int inicio = 0;
        while (inicio < TotalL) {
            char letrai = nuevoPass.charAt(inicio);
            if (numeri == false) {
                numeri = isNumerico(letrai);
            }
            if (letras == false) {
                letras = buscaABC(letrai) >= 0;
            }
            if (caracter == false) {
                caracter = CaracterEs(letrai) >= 0;
            }
            inicio++;
        }
        if (numeri == true && letras == true && caracter == true) {
            value = true;
        }
        return value;

    }

    private boolean ValSecuenciaNum(String nuevoPass) {
        boolean value = true;
        int TotalL = nuevoPass.length();
        int inicio = 0;
        while (inicio < TotalL - 2) {
            char letrai = nuevoPass.charAt(inicio);
               if (isNumerico(letrai)) {
                int num = Listnum[Integer.parseInt(String.valueOf(letrai))];
                char segletra = nuevoPass.charAt(inicio + 1);
                char terceletra = nuevoPass.charAt(inicio + 2);
                if (isNumerico(segletra) && isNumerico(terceletra)) {
                    int num2 = Listnum[Integer.parseInt(String.valueOf(segletra))];
                    int num3 = Listnum[Integer.parseInt(String.valueOf(terceletra))];
                    int aux = -1;
                    int aux2 = -1;
                    if (num < 8) {
                        aux = num + 1;
                        aux2 = num + 2;
                    } else {
                        aux = num;
                        aux2 = num;
                    }

                    if (((num == num2) && (num == num3)) || (num2 == aux && num3 == aux2)) {
                        value = false;
                        break;
                    }
                }
            }
            inicio++;
        }
        return value;
    }

    private boolean ValidaSecuenciaLetra(String nuevoPass) {
        boolean value = true;
        int TotalL = nuevoPass.length();
        int inicio = 0;
        while (inicio < TotalL - 2) {
            char letrai = nuevoPass.charAt(inicio);
            //System.out.println(" letra i "+ letrai);
            if (!isNumerico(letrai)) {
                char segletra = nuevoPass.charAt(inicio + 1);
                char tercera = nuevoPass.charAt(inicio + 2);
                if (!isNumerico(segletra)) {
                    //System.out.println(" segunda " + segletra+ "tercera "+ tercera);
                    int num = buscaABC(letrai);
                    // System.out.println("num " + num + "BuscaLetra() " + buscaABC(segletra));
                    if (num >= 0 && num < ABC.length - 2) {
                        if ((ABC[num].equals(String.valueOf(segletra)) && ABC[num].equals(String.valueOf(tercera)))
                                || (abc[num].equals(String.valueOf(segletra)) && abc[num].equals(String.valueOf(tercera)))
                                || (ABC[num].equals(String.valueOf(segletra)) && abc[num].equals(String.valueOf(tercera)))
                                || (abc[num].equals(String.valueOf(segletra)) && ABC[num].equals(String.valueOf(tercera)))
                                || (abc[num + 1].equals(String.valueOf(segletra)) && abc[num + 2].equals(String.valueOf(tercera)))
                                || (ABC[num + 1].equals(String.valueOf(segletra)) && ABC[num + 2].equals(String.valueOf(tercera)))
                                || (ABC[num + 1].equals(String.valueOf(segletra)) && abc[num + 2].equals(String.valueOf(tercera)))
                                || (abc[num + 1].equals(String.valueOf(segletra)) && ABC[num + 2].equals(String.valueOf(tercera)))) {
                            value = false;
                            break;
                        }
                    } else if (num >= 0 && num == ABC.length - 1) {
                        if ((ABC[num].equals(String.valueOf(segletra)) && abc[num].equals(String.valueOf(tercera)))
                                || (ABC[num].equals(String.valueOf(segletra)) && ABC[num].equals(String.valueOf(tercera)))
                                || (abc[num].equals(String.valueOf(segletra)) && abc[num].equals(String.valueOf(tercera)))
                                || (abc[num].equals(String.valueOf(segletra)) && ABC[num].equals(String.valueOf(tercera)))) {
                            value = false;
                            break;
                        }
                    }
                }
            }
            inicio++;
        }
        return value;

    }

    private boolean isNumerico(char letra) {
        boolean value = true;
        try {
            Integer.parseInt(String.valueOf(letra));
        } catch (NumberFormatException ex) {
            value = false;
        }
        return value;
    }

    private int buscaABC(char a) {
        int obt = -1;
        for (int i = 0; i < abc.length; i++) {
            if (abc[i].equals(String.valueOf(a))) {
                obt = i;
                break;
            }
        }
        if (obt == -1) {
            for (int i = 0; i < ABC.length; i++) {
                if (ABC[i].equals(String.valueOf(a))) {
                    obt = i;
                    break;
                }
            }
        }
        return obt;
    }

    private int CaracterEs(char a) {
        int obt = -1;
        for (int i = 0; i < Caracteres.length; i++) {
            if (Caracteres[i].equals(String.valueOf(a))) {
                obt = i;
                break;
            }
        }
        return obt;
    }

    private String ValidadCaracterEspecial(String nuevoPass) {
        String letra = "Correcto";
        int TotalL = nuevoPass.length();
        int inicio = 0;
        while (inicio < TotalL) {
            char letrai = nuevoPass.charAt(inicio);
            //System.out.println(" letra i"+ letrai);
            if (!isNumerico(letrai)) {
                int num = buscaABC(letrai);
                if (num == -1) {
                    num = CaracterEs(letrai);
                    if (num == -1) {
                        letra = "El caracter " + letrai + " no esta considerado en la contraseña";
                    }
                }
            }
            inicio++;
        }
        return letra;
    }

    private int numeroAleatorio() {
        return (int) (Math.random() * (Final - Inicial + 1) + Inicial);
    }

    public String GeneraccionPass() {
        int num, i = 0;
        String pass = "";
        while (i < 6) {
            num = numeroAleatorio();
            if (i == 2 || i == 5) {
                pass = pass + i;
            } else if (i == 0 || i == 4) {
                pass = pass + abc[num];
            } else {
                pass = pass + ABC[num];
            }
            i++;
        }
        return pass;
    }
    
    //METODOS PARA ENCRIPTAR LA CLAVDE DEL USUARIO
    public byte[] makeDigest(String user, String pwd) throws NoSuchAlgorithmException {
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
    
}
