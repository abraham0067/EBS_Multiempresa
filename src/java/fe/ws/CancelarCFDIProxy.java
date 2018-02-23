package fe.ws;

public class CancelarCFDIProxy implements fe.ws.CancelarCFDI_PortType {
  private String _endpoint = null;
  private fe.ws.CancelarCFDI_PortType cancelarCFDI_PortType = null;
  
  public CancelarCFDIProxy() {
    _initCancelarCFDIProxy();
  }
  
  public CancelarCFDIProxy(String endpoint) {
    _endpoint = endpoint;
    _initCancelarCFDIProxy();
  }
  
  private void _initCancelarCFDIProxy() {
    try {
      cancelarCFDI_PortType = (new fe.ws.CancelarCFDI_ServiceLocator()).getCancelarCFDIPort();
      if (cancelarCFDI_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cancelarCFDI_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cancelarCFDI_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cancelarCFDI_PortType != null)
      ((javax.xml.rpc.Stub)cancelarCFDI_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fe.ws.CancelarCFDI_PortType getCancelarCFDI_PortType() {
    if (cancelarCFDI_PortType == null)
      _initCancelarCFDIProxy();
    return cancelarCFDI_PortType;
  }
  
  public byte[] cancelaTestUuid(java.lang.String rfc, java.lang.String uuid, java.lang.String clave) throws java.rmi.RemoteException{
    if (cancelarCFDI_PortType == null)
      _initCancelarCFDIProxy();
    return cancelarCFDI_PortType.cancelaTestUuid(rfc, uuid, clave);
  }
  
  public byte[] cancela(java.lang.String rfc, java.lang.String serie_erp, java.lang.String folio_erp, java.lang.String clave) throws java.rmi.RemoteException{
    if (cancelarCFDI_PortType == null)
      _initCancelarCFDIProxy();
    return cancelarCFDI_PortType.cancela(rfc, serie_erp, folio_erp, clave);
  }
  
  public byte[] cancelaUuid(java.lang.String rfc, java.lang.String uuid, java.lang.String clave) throws java.rmi.RemoteException{
    if (cancelarCFDI_PortType == null)
      _initCancelarCFDIProxy();
    return cancelarCFDI_PortType.cancelaUuid(rfc, uuid, clave);
  }
  
  public byte[] cancelaTest(java.lang.String rfc, java.lang.String serie_erp, java.lang.String folio_erp, java.lang.String clave) throws java.rmi.RemoteException{
    if (cancelarCFDI_PortType == null)
      _initCancelarCFDIProxy();
    return cancelarCFDI_PortType.cancelaTest(rfc, serie_erp, folio_erp, clave);
  }
  
  
}