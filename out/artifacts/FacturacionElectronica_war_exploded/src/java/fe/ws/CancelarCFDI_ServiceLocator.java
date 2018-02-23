/**
 * CancelarCFDI_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fe.ws;

@SuppressWarnings("serial")
public class CancelarCFDI_ServiceLocator extends org.apache.axis.client.Service implements fe.ws.CancelarCFDI_Service {

    public CancelarCFDI_ServiceLocator() {
    }


    public CancelarCFDI_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CancelarCFDI_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CancelarCFDIPort
    //private java.lang.String CancelarCFDIPort_address = "http://192.168.0.105:80/wsCancelacionCFDI/CancelarCFDI";
    private java.lang.String CancelarCFDIPort_address = "http://localhost/wsCancelacionCFDI/CancelarCFDI";
    public java.lang.String getCancelarCFDIPortAddress() {
        return CancelarCFDIPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CancelarCFDIPortWSDDServiceName = "CancelarCFDIPort";

    public java.lang.String getCancelarCFDIPortWSDDServiceName() {
        return CancelarCFDIPortWSDDServiceName;
    }

    public void setCancelarCFDIPortWSDDServiceName(java.lang.String name) {
        CancelarCFDIPortWSDDServiceName = name;
    }

    public fe.ws.CancelarCFDI_PortType getCancelarCFDIPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CancelarCFDIPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCancelarCFDIPort(endpoint);
    }

    public fe.ws.CancelarCFDI_PortType getCancelarCFDIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fe.ws.CancelarCFDIPortBindingStub _stub = new fe.ws.CancelarCFDIPortBindingStub(portAddress, this);
            _stub.setPortName(getCancelarCFDIPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCancelarCFDIPortEndpointAddress(java.lang.String address) {
        CancelarCFDIPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fe.ws.CancelarCFDI_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                fe.ws.CancelarCFDIPortBindingStub _stub = new fe.ws.CancelarCFDIPortBindingStub(new java.net.URL(CancelarCFDIPort_address), this);
                _stub.setPortName(getCancelarCFDIPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CancelarCFDIPort".equals(inputPortName)) {
            return getCancelarCFDIPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.fe/", "CancelarCFDI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.fe/", "CancelarCFDIPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CancelarCFDIPort".equals(portName)) {
            setCancelarCFDIPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
