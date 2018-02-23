package fe.model.util;


import org.apache.axis.encoding.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class ClientWSCancelar {
	private static final String USER_WS = "wsreader";
	private static final String PASS_WS = "wsreader";
	private static final String URL_WS = "http://demo.libreplan.org/libreplan/ws/rest/";
	
	public static void main(String[] args) throws Exception {

        String service = "calendars";

        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(URL_WS + service);
        get.setRequestHeader("Authorization", "Basic "+ Base64.encode((USER_WS + ":" + PASS_WS).getBytes()));

        try {
            // execute the GET
            System.out.println("Executing GET: " + get.getPath());
            int status = client.executeMethod(get);

            // print the status and response
            System.out.println("Status: "+ status + "\nResponse: " + get.getResponseBodyAsString());

        } finally {
            get.releaseConnection();
        }
    }

}
