
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.util;

import com.ebs.security.MyResponseRequestWrapper;
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
public class SecurityFlagsFilter implements Filter {

    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public SecurityFlagsFilter() {
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
        MyResponseRequestWrapper responseWrapper = new MyResponseRequestWrapper(response);
        HttpSession session = request.getSession(true);
        //Set secureflag and httponlyflag on cookie
        String sessionid = request.getSession().getId();
        //Example
        //Set-Cookie: JSESSIONID=CA99F7529096F5322296856E71A459D0; Path=/FacturacionElectronica/; HttpOnly
        //With secure flag, secure only works over ssl(HTTPS), if u set secure flag on no-ssl causes viewexpiredexception on webapp
        responseWrapper.setHeader("Set-Cookie", "JSESSIONID=" + sessionid + "; Path=" + request.getContextPath() + "; secure; HttpOnly");
        //Without secure flag
        //responseWrapper.setHeader("Set-Cookie", "JSESSIONID=" + sessionid + "; Path=" + request.getContextPath() + "; HttpOnly");

        //Add securityheaders
        responseWrapper.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate, private"); // HTTP 1.1.
        responseWrapper.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        responseWrapper.setDateHeader("Expires", 0); // Proxies.
        responseWrapper.setHeader("X-XSS-Protection", "1; mode=block");
        responseWrapper.setHeader("X-Frame-Options", "DENY");
        responseWrapper.setHeader("X-Content-Type-Options", "nosniff");
        responseWrapper.setHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains");
        chain.doFilter(request, responseWrapper); // So, just continue request.
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
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
                log("SecurityFlagsFilter: Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("SecurityFlagsFilter()");
        }
        StringBuffer sb = new StringBuffer("SecurityFlagsFilter(");
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

}
