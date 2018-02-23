
import java.text.NumberFormat;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class Cantidad extends JRDefaultScriptlet
{

    public Cantidad()
    {
    }

    public static void main(String args[])
    {
        Cantidad cantidad = new Cantidad();        
        System.out.println(cantidad.formatoNumero("formatoNumero"));
    }

    public String convierte(String s, String moneda)
    {    
    	
        return getCantidad((new Double(s)).doubleValue(), moneda);
    }
    
    public String convierte(String s)
    {   
    
        return getCantidad((new Double(s)).doubleValue());
    }
    
    public String getFormato(String cant)
    {
        String formato = "";
        NumberFormat sf = NumberFormat.getInstance();
        sf.setMaximumFractionDigits(2);
        sf.setMinimumFractionDigits(2);
        sf.setGroupingUsed(true);
        try
        {
            formato = sf.format(Double.parseDouble(cant));
        }
        catch(java.lang.NumberFormatException ex)
        {
            return cant;
        }
        return formato;
    }
    

    public String formatoNumero(String cant)
    {
        String formato = "";
        NumberFormat sf = NumberFormat.getInstance();
        sf.setMaximumFractionDigits(2);
        sf.setMinimumFractionDigits(2);
        sf.setGroupingUsed(true);
        try
        {
            formato = sf.format(Double.parseDouble(cant));
        }
        catch(java.lang.NumberFormatException ex)
        {
            return cant;
        }
        return formato;
    }

    public String getFormato2(String cant)
    {
        String formato = "";
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        nf.setMinimumFractionDigits(4);
        nf.setGroupingUsed(true);
        try
        {
            formato = nf.format(Double.parseDouble(cant));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return formato;
    }

    public String getCantidad(double d, String moneda)
    {
    	if(d<0){
    		d=d*(-1);
    	}
        NumberFormat numberformat = NumberFormat.getNumberInstance();
        numberformat.setMaximumFractionDigits(0);
        numberformat.setMinimumIntegerDigits(2);
        String s = "(";
        String s1 = numberformat.format(100D * (d - (long)d));
        if(moneda!=null && "USD".equals(moneda.trim())){
        	 s = (new StringBuilder()).append(s).append(num2Text((long)d)).append(" DOLARES ").append(s1).append("/100)").toString();
        }else{
        s = (new StringBuilder()).append(s).append(num2Text((long)d)).append(" PESOS ").append(s1).append("/100 M.N.)").toString();
        }
        return s;
    }
    
    public String getCantidad(double d)
    {
        NumberFormat numberformat = NumberFormat.getNumberInstance();
        numberformat.setMaximumFractionDigits(0);
        numberformat.setMinimumIntegerDigits(2);
        String s = "(";
        String s1 = numberformat.format(100D * (d - (long)d));      
        s = (new StringBuilder()).append(s).append(num2Text((long)d)).append(" PESOS ").append(s1).append("/100 M.N.)").toString();      
        return s;
    }

    public String num2Text(double d)
    {
        String s = "";
        if(d == 0.0D)
            s = "CERO";
        else
        if(d == 1.0D)
            s = "UN";
        else
        if(d == 2D)
            s = "DOS";
        else
        if(d == 3D)
            s = "TRES";
        else
        if(d == 4D)
            s = "CUATRO";
        else
        if(d == 5D)
            s = "CINCO";
        else
        if(d == 6D)
            s = "SEIS";
        else
        if(d == 7D)
            s = "SIETE";
        else
        if(d == 8D)
            s = "OCHO";
        else
        if(d == 9D)
            s = "NUEVE";
        else
        if(d == 10D)
            s = "DIEZ";
        else
        if(d == 11D)
            s = "ONCE";
        else
        if(d == 12D)
            s = "DOCE";
        else
        if(d == 13D)
            s = "TRECE";
        else
        if(d == 14D)
            s = "CATORCE";
        else
        if(d == 15D)
            s = "QUINCE";
        else
        if(d < 20D)
            s = (new StringBuilder()).append("DIECI").append(num2Text(d - 10D)).toString();
        else
        if(d == 20D)
            s = "VEINTE";
        else
        if(d < 30D)
            s = (new StringBuilder()).append("VEINTI").append(num2Text(d - 20D)).toString();
        else
        if(d == 30D)
            s = "TREINTA";
        else
        if(d == 40D)
            s = "CUARENTA";
        else
        if(d == 50D)
            s = "CINCUENTA";
        else
        if(d == 60D)
            s = "SESENTA";
        else
        if(d == 70D)
            s = "SETENTA";
        else
        if(d == 80D)
            s = "OCHENTA";
        else
        if(d == 90D)
            s = "NOVENTA";
        else
        if(d < 100D)
            s = (new StringBuilder()).append(num2Text((int)(d / 10D) * 10)).append(" Y ").append(num2Text(d % 10D)).toString();
        else
        if(d == 100D)
            s = "CIEN";
        else
        if(d < 200D)
            s = (new StringBuilder()).append("CIENTO ").append(num2Text(d - 100D)).toString();
        else
        if((d == 200D) | (d == 300D) | (d == 400D) | (d == 600D) | (d == 800D))
            s = (new StringBuilder()).append(num2Text((int)(d / 100D))).append("CIENTOS").toString();
        else
        if(d == 500D)
            s = "QUINIENTOS";
        else
        if(d == 700D)
            s = "SETECIENTOS";
        else
        if(d == 900D)
            s = "NOVECIENTOS";
        else
        if(d < 1000D)
            s = (new StringBuilder()).append(num2Text((int)(d / 100D) * 100)).append(" ").append(num2Text(d % 100D)).toString();
        else
        if(d == 1000D)
            s = "MIL";
        else
        if(d < 2000D)
            s = (new StringBuilder()).append("MIL ").append(num2Text(d % 1000D)).toString();
        else
        if(d < 1000000D)
        {
            s = (new StringBuilder()).append(num2Text((int)(d / 1000D))).append(" MIL").toString();
            if(d % 1000D >= 1.0D)
                s = (new StringBuilder()).append(s).append(" ").append(num2Text(d % 1000D)).toString();
        } else
        if(d == 1000000D)
            s = "UN MILLON";
        else
        if(d < 2000000D)
            s = (new StringBuilder()).append("UN MILLON ").append(num2Text(d % 1000000D)).toString();
        else
        if(d < 1000000000000D)
        {
            s = (new StringBuilder()).append(num2Text((long)(d / 1000000D))).append(" MILLONES").toString();
            if(d - (long)(d / 1000000D) * 0xf4240L > 0.0D)
                s = (new StringBuilder()).append(s).append(" ").append(num2Text(d - (long)(d / 1000000D) * 0xf4240L)).toString();
        } else
        if(d == 1000000000000D)
            s = "UN BILLON";
        else
        if(d < 2000000000000D)
        {
            s = (new StringBuilder()).append("UN BILLON ").append(num2Text(d - (long)(d / 1000000000000D) * 1000000000000D)).toString();
        } else
        {
            s = (new StringBuilder()).append(num2Text((long)(d / 1000000000000D))).append(" BILLONES").toString();
            if(d - (long)(d / 1000000000000D) * 1000000000000D > 0.0D)
                s = (new StringBuilder()).append(s).append(" ").append(num2Text(d - (long)(d / 1000000000000D) * 1000000000000D)).toString();
        }
        return s;
    }
}