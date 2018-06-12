package fe.model.util;

import com.lowagie.text.html.HtmlEncoder;
import fe.pki.PKI;
import org.bouncycastle.util.encoders.Base64;

public class CifraTexto
{
    private String texto1;
    private String texto2;
    private String texto3;
    
    public CifraTexto() {
        this.texto1 = "";
        this.texto2 = "";
        this.texto3 = "";
    }
    
    public CifraTexto(final String texto1, final String texto2) {
        this.texto1 = "";
        this.texto2 = "";
        this.texto3 = "";
        this.texto1 = texto1;
        this.texto2 = texto2;
    }
    
    public CifraTexto(final String texto1, final String texto2, final String texto3) {
        this.texto1 = "";
        this.texto2 = "";
        this.texto3 = "";
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.texto3 = texto3;
    }
    
    public String httpEncode() {
        String encode = "";
        try {
            final PKI pki = new PKI();
            encode = HtmlEncoder.encode(new String(Base64.encode(pki.cipher_bytes(generaString().getBytes(), "ebs12", "Blowfish", PKI.ENCRYPT_MODE)), "UTF-8"));
            //encode = URLEncoder.encode(new String(Base64.encode(pki.cipher_bytes(this.generaString().getBytes(), "ebs12", "Blowfish", PKI.ENCRYPT_MODE))), "UTF-8");
        }
        catch (Exception ex) {}
        return encode;
    }

    public byte[] httpDecode(String id) {
        byte[] decode = "".getBytes();
        try {
            PKI pki = new PKI();
            id = id.replaceAll(" ", "+");
            decode = pki.cipher_bytes(Base64.decode(id), "ebs12", "Blowfish", PKI.DECRYPT_MODE);
//            decode = pki.cipher_bytes(Base64.decode(URLDecoder.decode(id, "UTF-8")), "ebs12", "Blowfish", PKI.DECRYPT_MODE);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return decode;
    }

    
    public String getEncode() {
        String encode = "";
        try {
            final PKI pki = new PKI();
            encode = new String(Base64.encode(pki.cipher_bytes(this.generaString().getBytes(), "ebs12", "Blowfish", PKI.ENCRYPT_MODE)));
        }
        catch (Exception ex) {}
        return encode;
    }
    
    public byte[] getDecode(final String id) {
        byte[] encode = "".getBytes();
        try {
            final PKI pki = new PKI();
            encode = pki.cipher_bytes(Base64.decode(id), "ebs12", "Blowfish", PKI.DECRYPT_MODE );
        }
        catch (Exception ex) {}
        return encode;
    }
    
    private String generaString() {
        String datos = "";
        if (!this.texto1.isEmpty() && !this.texto2.isEmpty() && !this.texto3.isEmpty()) {
            datos = this.texto1 + "|" + this.texto2 + "|" + this.texto3;
        }
        else if (!this.texto1.isEmpty() && !this.texto2.isEmpty()) {
            datos = this.texto1 + "|" + this.texto2;
        }
        else if (!this.texto2.isEmpty() && !this.texto3.isEmpty()) {
            datos = this.texto2 + "|" + this.texto3;
        }
        else if (!this.texto1.isEmpty() && !this.texto3.isEmpty()) {
            datos = this.texto1 + "|" + this.texto3;
        }
        else if (!this.texto1.isEmpty()) {
            datos = this.texto1;
        }
        else if (!this.texto2.isEmpty()) {
            datos = this.texto2;
        }
        else if (!this.texto3.isEmpty()) {
            datos = this.texto3;
        }
        return datos.trim();
    }
    
    public void setTexto1(final String texto1) {
        this.texto1 = texto1;
    }
    
    public void setTexto2(final String texto2) {
        this.texto2 = texto2;
    }
    
    public void setTexto3(final String texto3) {
        this.texto3 = texto3;
    }
}
