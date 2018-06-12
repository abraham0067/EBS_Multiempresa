package fe.model.util;

import org.bouncycastle.util.encoders.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

	// METODOS PARA ENCRIPTAR LA CLAVDE DEL USUARIO
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t= new Test();
		String user="admingral";
		String clave ="raG85bb#";
		System.out.println(t.getEncriptPW(user, clave));


	}

}
