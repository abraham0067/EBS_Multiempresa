/**
 * 
 */
package mx.com.ebs.emision.factura.utilierias;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.Locale;

import com.barcodelib.barcode.QRCode;

/**
 * @author Amiranda
 *
 */
public class QRImagen {
	
	
	/**
	 * Metodo encargado de obtener el codigo de barras bidimencional 
	 * @param rfcEmi : El rfc del emisor
	 * @param rfcRec : El rfc del receptor
	 * @param total  : Valor total de la factura
	 * @param uuid   : Timbrado de la factura
	 * @return Image QR
	 * */
	public Image get2DBarCode (String rfcEmi, String rfcRec, String total, String uuid) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(new Locale("sp","MX"));
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(6);
        nf.setMaximumIntegerDigits(10);
        nf.setMinimumIntegerDigits(10);
        nf.setGroupingUsed(false);

        // Gen 2D CodeBar barcode
        int        uom   = 1; //  0 - Pixel, 1 - CM, 2 - Inch
        int resolution   = 75;
        int rotate       = 0; //  0 - 0, 1 - 90, 2 - 180, 3 - 270
        float moduleSize = 0.100f;

        // save barcode in "generated" folder
        BufferedImage bi = new BufferedImage(118,118,BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D     g = (Graphics2D)bi.getGraphics();
        QRCode barcode = new QRCode();
        barcode.setData("?re=" + rfcEmi + "&rr=" + rfcRec + "&tt=" + nf.format(Double.parseDouble(total)) + "&id=" + uuid);
        barcode.setDataMode(QRCode.MODE_AUTO);// barcode.setDataMode(QRCode.MODE_BYTE);
        barcode.setVersion(10);
        barcode.setEcl(QRCode.ECL_M);
        barcode.setUOM(uom);
        barcode.setModuleSize(moduleSize);
        barcode.setLeftMargin(0.1f);
        barcode.setRightMargin(0.1f);
        barcode.setTopMargin(0.1f);
        barcode.setBottomMargin(0.1f);
        barcode.setResolution(resolution);
        barcode.setRotate(rotate);

        barcode.renderBarcode(g, new Rectangle());        
        return bi;
    }

}
