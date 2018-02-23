/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.util;

import fe.db.MAcceso;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class SessionUrlFilter implements Filter {

    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    public static final String LOGIN_PAGE = "/login/login.xhtml";
    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    public SessionUrlFilter() {
    }

    /**
     *
     * @param req
     * @param res
     * @param chain
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);
        String loginURL =  "/login/login.xhtml";
        String trialPage =  "/login/trial.xhtml";
        String homePage =  "/main/principal.xhtml";
        String regURL =  "/login/autoRegistro.xhtml";
        String recURL =  "/recovery/passwordRecovery.xhtml";
        String activationURL =  "/activation.xhtml";
        
        boolean loggedIn = (session != null) && (session.getAttribute("macceso") != null);
        boolean loginRequest = request.getRequestURI().contains(loginURL);
        boolean registerRequest = request.getRequestURI().contains(regURL);
        boolean recoverRequest = request.getRequestURI().contains(recURL);
        boolean homePageRequested = request.getRequestURI().contains(homePage);
        boolean activationRequest = request.getRequestURI().contains(activationURL);
        boolean resourceRequest = request.getRequestURI().contains( ResourceHandler.RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(request.getHeader("Faces-Request"));
        boolean canAcces = true;//ALLOW login page on first time

        //Set secureflag and httponlyflag on cookie
        String sessionid = request.getSession().getId();
        //Example
        //Set-Cookie: JSESSIONID=CA99F7529096F5322296856E71A459D0; Path=/FacturacionElectronica/; HttpOnly
        //With secure flag, secure only works over ssl(HTTPS), if u set secure flag on no-ssl causes viewexpiredexception on webapp
        //Without secure flag
        //responseWrapper.setHeader("Set-Cookie", "JSESSIONID=" + sessionid + "; Path=" + request.getContextPath() + "; HttpOnly");
        // TODO: 19/07/2017 COMPROBAR SI EXISTE UNA SESSION, EN CASO DE QUE SI REDIRIGIR A LA PAGINA PRINCIPAL
        //Evita que los usuarios puedan acceder a paginas no autorizadas
        if (loggedIn && !resourceRequest && !loginRequest) {
            canAcces = canUserAcces((MAcceso) session.getAttribute("macceso"), getLongPage((String) request.getRequestURL().toString()));//Filtrar acceso a paginas
            if (!canAcces && !homePageRequested) {
                ///System.out.printf("El usuario  <%s> NO puede acceder a <%s>\n",((String)session.getAttribute("usernick")), request.getRequestURL().toString());
                // TODO resolver el error TO_MANY_REDIRECTS
                response.sendRedirect(request.getContextPath()+ homePage); // So, just perform standard synchronous redirect.
            } else{
                ///System.out.printf("El usuario  <%s> SI puede acceder a <%s>\n",((String)session.getAttribute("usernick")), request.getRequestURL().toString());
            }
        }
        if (loggedIn || loginRequest || resourceRequest || registerRequest || activationRequest || recoverRequest) {
            if (!resourceRequest) { // Prevent browser from caching restricted resources. See also http://stackoverflow.com/q/4194207/157882
                response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate, private"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0); // Proxies.
            }
            chain.doFilter(request, response); // So, just continue request.
        } else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, request.getContextPath() +loginURL); // So, return special XML response instructing JSF ajax to send a redirect.
        } else {
            response.sendRedirect(request.getContextPath() +loginURL); // So, just perform standard synchronous redirect.
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set
     * @param filterConfig The fi the filter configuration object for this filter.
     *lter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("SessionUrlFilter: Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("SessionUrlFilter()");
        }
        StringBuffer sb = new StringBuffer("SessionUrlFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());

    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    /**
     * Determina si el usuario actual tiene acceso a la pagina solicitada
     *
     * @param cuentaUsuario
     * @param longPage
     * @return
     */
    public boolean canUserAcces(MAcceso cuentaUsuario, long longPage) {
        boolean res = false;
        if (cuentaUsuario.getPerfil().getPerfil() != null) {
            long perfil = cuentaUsuario.getPerfil().getPerfil();
            if ((perfil & longPage) == longPage) {
                res = true;
            }
        } else {
            res = true;
        }
        return res;
    }

    /**
     *
     * @param page
     * @return
     */
    public long getLongPage(String page) {
        long res = 1;
        // TODO: 13/09/2017 REEMPLAZAR LOS VALORES HARDCODED DE LOS PERFILES DE LAS PAGINAS POR ALGO DINAMICO
        if (page.contains("facturas/facturasCFD.xhtml")) {
            res = 1048576;
        } else if (page.contains("facturas/facturasCFDI.xhtml")) {
            res = 2;
        } else if (page.contains("facturas/retenciones.xhtml")) {
            res = 2;
        } else if (page.contains("facturas/facturasManual.xhtml")) {
            res = 8192;
        } else if (page.contains("admin/administracionEmpresas.xhtml")) {
            res = 8;
        } else if (page.contains("admin/adminPerfiles.xhtml")) {
            res = 16;
        } else if (page.contains("admin/usuarios/adminUsuariosExterno.xhtml") || page.contains("admin/usuarios/adminUsuariosInterno.xhtml")) {
            res = 32;
        } else if (page.contains("admin/administracionCertificados.xhtml")) {
            res = 16384;
        } else if (page.contains("admin/plantillas/adminPlantillas.xhtml")) {
            res = 64;
        } else if (page.contains("admin/parametros/adminParametros.xhtml")) {
            res = 65536;
        } else if (page.contains("admin/pacemp/adminPacEmpresa.xhtml")) {
            res = 262144;
        } else if (page.contains("admin/catalogos/catClientes.xhtml")) {
            res = 256;
        } else if (page.contains("admin/catalogos/catFolios.xhtml")) {
            res = 512;
        } else if (page.contains("admin/catalogos/catConceptos.xhtml")) {
            res = 1024;
        } else if (page.contains("admin/catalogos/catPacs.xhtml")) {
            res = 524288;
        } else if (page.contains("admin/logs/logAcceso.xhtml")) {
            res = 4096;
        } else if (page.contains("admin/logs/logProcFact.xhtml")) {
            res = 131072;
        }
        return res;

    }

}
