package com.ebs.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by eflores on 06/06/2017.
 */
public class AtebResponseProcessorImpl implements ResponseProcessor {
    @Override
    public String[] processResponse(String response) {
        // TODO: 06/06/2017 SEARCH ON RESPONSE FOR ERRORS OR UUID O
        String[] errorCodes =  null;
        String xpathExpression = "//GeneraTimbreResponse/GeneraTimbreResult/Error[@Codigo]";
        if (response != null && !response.isEmpty()) {
            if (response.contains("<Error")) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                try {
                    builder = factory.newDocumentBuilder();
                    Document doc = builder.parse("C:\\Users\\Josel\\Documents\\_EBS_PROJECTS\\ATEB\\RespGenCFDI33.xml");
                    //Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));
                    XPathFactory xPathfactory = XPathFactory.newInstance();
                    XPath xpath = xPathfactory.newXPath();
                    XPathExpression expr = xpath.compile(xpathExpression);
                    NodeList errors = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                    errorCodes = new String[errors.getLength()];
                    for (int i = 0; i < errors.getLength(); i++) {
                        Node tmp = errors.item(i);
                        if (tmp.hasAttributes()) {
                            Attr at2 = (Attr) tmp.getAttributes().getNamedItem("Descripcion");
                            if (at2 != null) {
                                String text = at2.getValue();
                                errorCodes[i] = text;
                                System.out.println("text = " + text);
                            }
                        }
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }
        return errorCodes;
    }
}
