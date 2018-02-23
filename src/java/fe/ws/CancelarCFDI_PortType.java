/**
 * CancelarCFDI_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fe.ws;

public interface CancelarCFDI_PortType extends java.rmi.Remote {
    public byte[] cancelaTestUuid(java.lang.String rfc, java.lang.String uuid, java.lang.String clave) throws java.rmi.RemoteException;
    public byte[] cancela(java.lang.String rfc, java.lang.String serie_erp, java.lang.String folio_erp, java.lang.String clave) throws java.rmi.RemoteException;
    public byte[] cancelaUuid(java.lang.String rfc, java.lang.String uuid, java.lang.String clave) throws java.rmi.RemoteException;
    public byte[] cancelaTest(java.lang.String rfc, java.lang.String serie_erp, java.lang.String folio_erp, java.lang.String clave) throws java.rmi.RemoteException;
}
