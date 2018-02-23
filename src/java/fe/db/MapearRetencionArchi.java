package fe.db;

public class MapearRetencionArchi
{
    private McfdRetencion retencion;
    private int existe;
    
    public MapearRetencionArchi(final McfdRetencion retencion, final int existe) {
        this.existe = 0;
        this.retencion = retencion;
        this.existe = existe;
    }
    
    public MapearRetencionArchi() {
        this.existe = 0;
    }
    
    public McfdRetencion getRetencion() {
        return this.retencion;
    }
    
    public void setRetencion(final McfdRetencion retencion) {
        this.retencion = retencion;
    }
    
    public int getExiste() {
        return this.existe;
    }
    
    public void setExiste(final int existe) {
        this.existe = existe;
    }
}
