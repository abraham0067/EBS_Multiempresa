package com.ebs.clienteFEWS;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import fe.xml.ReadXMLProperties;

public class ClienteFEWS {

    //private String ruta = "http://localhost:8453/FacturacionElectronicaWS";
    private String ruta = "";
    final String root = "/home/Fe_Multiempresa/config/ruta.xml";


    public ClienteFEWS() {

        ReadXMLProperties read = new ReadXMLProperties(root);
        ruta = read.getValue("ruta");

        System.out.println(ruta);

    }

    public  byte[] clienteFEWS(String path, int param) throws Exception {

        Client rest = ClientBuilder.newClient();

        WebTarget wtar = rest.target(ruta)
                .path("getFiles")
                .path(path)
                .queryParam("idCFdi", param);


        byte[] bytes = (byte[]) wtar.request().get(byte[].class);


        return bytes;
    }

    public byte[] zip(List<Integer> listIdCfdi, String path){

        String ids = "";

        ids = listIdCfdi.stream().map((integer) -> integer + ",").reduce(ids, String::concat);

        System.out.println("ids: " + ids);

        Client rest = ClientBuilder.newClient();

        WebTarget wtar = rest.target(ruta)
                .path("getFiles")
                .path(path)
                .queryParam("idCFdi", ids);


        byte[] bytes = (byte[]) wtar.request().get(byte[].class);

        System.out.println("bytes: " + bytes.length);

        return bytes;

    }

    public  byte[] clientFiles(String path, String param) throws Exception {

        Client rest = ClientBuilder.newClient();

        WebTarget wtar = rest.target(ruta)
                .path("getFiles")
                .path(path)
                .queryParam("idCFdi", param);


        byte[] bytes = (byte[]) wtar.request().get(byte[].class);

        System.out.println("bytes: " + bytes.length);

        return bytes;
    }
}
