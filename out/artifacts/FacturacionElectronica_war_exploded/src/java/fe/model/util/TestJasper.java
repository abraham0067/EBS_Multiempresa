package fe.model.util;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Locale;
import java.util.HashMap;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TestJasper {
	private String[] reps = {"fesiemens.jasper","subConceptos.jasper"};
	private String[] imgs = {"logo.jpg","rfc.jpg","backgroud.jpg"};
	
	public static void main(String[] args) throws Exception {
		new TestJasper().process();
    }
    
    public void process() throws Exception {
    	
    	// ******** Crea ZIP desde Files con imagenes y jaspers ***********
    	byte[] zip = getZip(reps, imgs);
    	
    	// Guarda zip en disco

//    	FileOutputStream fos = new FileOutputStream("zip.zip");
//    	fos.write(zip);
//    	fos.close();
    	
    	// Genera PDF usando jaspers
        JRXmlDataSource jrxmlds = new JRXmlDataSource( 
        	new FileInputStream("A91B3C63-EAF7-4824-AFAA-FEDB2AE34130_P.xml") , 
        	"/Invoice/E2EDP01[not(MENGE=0)][not(E2EDP19/KTEXT='Liquidación de anticipo')][not(E2EDP19/KTEXT='Down payment settlement')]"
        );
        jrxmlds.setLocale(new Locale("sp","MX"));
        jrxmlds.setNumberPattern("##0.00");
        
        HashMap<String,Object> parameters = new HashMap<String,Object>();
        
//        FileInputStream fis = new FileInputStream("subConceptos.jasper");
//        parameters.put("SUBREPORTES", fis);

        parameters.put("IMAGES", "./");

        // **********  Crea subreportes e imagenes ***********
        InputStream in = setParameters(zip, parameters);

        JasperPrint jp = JasperFillManager.fillReport(
            in,
            parameters,
            jrxmlds
        );
        
        // Visualiza PDF en una ventana swing
        JasperViewer.viewReport(jp, false);
    }
    
    private byte[] getZip(String[] reps, String[] imgs) throws Exception {
    	return new JasperZip().getZipBytes(reps, imgs);
    }
    
    private InputStream setParameters(byte[] zip, HashMap<String,Object> parameters) throws Exception {
    	return new JasperZip().setUnZipBytes(zip, parameters);
    }
    
	private class JasperZip {
	    public byte[] getZipBytes(String[] fJaspers, String[] fImages) throws Exception {
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        ZipOutputStream zout = new ZipOutputStream(bout);
	        // Reports
	        for (String fJ : fJaspers) {
		        ZipEntry zent = new ZipEntry("reports/" + fJ);
		        zout.putNextEntry(zent);
		        zout.write(getBytes(fJ));
		        zout.closeEntry();
	        }
	        // Images
	        for (String fI : fImages) {
		        ZipEntry zent = new ZipEntry("images/" + fI);
		        zout.putNextEntry(zent);
		        zout.write(getBytes(fI));
		        zout.closeEntry();
	        }
	        zout.flush();
	        zout.close();
	        
	        return bout.toByteArray();
	    }
	    
	    public InputStream setUnZipBytes(byte[] bytes, HashMap<String,Object> parameters) throws Exception {
	        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	        ZipInputStream zin = new ZipInputStream(bais);
	        ZipEntry zent = zin.getNextEntry();
	        byte[] b = new byte[512];
	        InputStream stream = null;
	        
	        int item = 0;
	        while ( zent != null ) {
		        ByteArrayOutputStream bout = new ByteArrayOutputStream();
		        
		        int buff = zin.read(b);
		        while ( buff > 0 ) {
		            bout.write(b, 0, buff);
		            buff = zin.read(b);
		        }
				String name = zent.getName().toUpperCase().substring(0, zent.getName().indexOf(".")).split("\\/")[1];
				
		        if ( "reports".equals(zent.getName().split("/")[0]) ) {
		        	if ( item == 0 )
		        		stream = new ByteArrayInputStream( bout.toByteArray() );
		        	else
	        			parameters.put(name,new ByteArrayInputStream( bout.toByteArray() ));
	        		
	        		item++;
		        }
		        if ( "images".equals(zent.getName().split("/")[0]) )
	        		parameters.put(name,new ByteArrayInputStream( bout.toByteArray() ));
		        zin.closeEntry();
		        
		        zent = zin.getNextEntry();
	        }
	        zin.close();
	        
	        return stream;
	    }
	    
	    private byte[] getBytes(String file) throws Exception {
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	FileInputStream fis = new FileInputStream(file);
	    	
	        byte[] b = new byte[1024];
	        
	    	int buff = fis.read(b);
	    	while( buff > 0 ) {
	    		baos.write(b, 0 , buff);
	    		buff = fis.read(b);
	    	}
	    	fis.close();
	    	
	    	return baos.toByteArray();
	    }
	}
}
