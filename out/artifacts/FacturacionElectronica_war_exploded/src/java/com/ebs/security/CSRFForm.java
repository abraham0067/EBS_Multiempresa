/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.security;

import java.io.IOException;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author <Eduardo at EBS>
 */
public class CSRFForm extends HtmlForm {

    String CSRFTOKEN_NAME = "token";

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

// initialize the new TokenInput
        CSRFTokenInput cSRFToken = new CSRFTokenInput();

// set the clientId
        cSRFToken.setId(this.getClientId() + "_CSRFToken");

// add the component to the form
        this.getChildren().add(cSRFToken);
        super.encodeBegin(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
// get the session (don't create a new one!)
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
// get the token from the session
        String token = (String) session.getAttribute(CSRFTOKEN_NAME);
// write the component html to the response
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement("input", null);
        responseWriter.writeAttribute("type", "hidden", null);
        responseWriter.writeAttribute("name", (getClientId(context)), "clientId");
        responseWriter.writeAttribute("value", token, "CSRFTOKEN_NAME");
        responseWriter.endElement("input");

    }
}
