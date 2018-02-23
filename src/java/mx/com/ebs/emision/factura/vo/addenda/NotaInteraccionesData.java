/**
 * 
 */
package mx.com.ebs.emision.factura.vo.addenda;

/**
 * @author CarloG
 *
 */
public class NotaInteraccionesData
implements NotaInteracciones
{

public NotaInteraccionesData()
{
    notas = new String[0];
}

public String[] getNotas()
{
    return notas;
}

public void setNotas(String notas[])
{
    this.notas = notas;
}

private String notas[];
}