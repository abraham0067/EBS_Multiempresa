package com.ebs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Clase de utilidad para comprimir y descomprimir documentos tomando como
 * entrada un arreglo de bytes. Se utilizan las clases del paquete
 * <i>java.util.zip</i>
 *
 *
 * @author robb
 * @see java.util.zip.ZipInputStream Flujo para comprimir
 * @see java.util.zip.ZipOutputStream Flujo para descomprimir
 */
public class Zipper {

    /**
     * @param nombre Es el nombre que tendr&aacute; la entidad, es decir, el
     * <i>String</i>
     * <br/>que ser&aacute; retornado cuando se llame al
     * m&eacute;todo<br/><b><i>entry.getName()</i></b>
     * @param data El documento como un arreglo de bytes a ser comprimido
     * @return El documento comprimido como un arreglo de bytes
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(stream);
        zip.putNextEntry(new ZipEntry("cfdi"));
        zip.write(data);
        zip.closeEntry();
        return stream.toByteArray();
    }

    /**
     * @param nombre Es el nombre que tendr&aacute; la entidad, es decir, el
     * <i>String</i>
     * <br/>que ser&aacute; retornado cuando se llame al
     * m&eacute;todo<br/><b><i>entry.getName()</i></b>
     * @param data El documento como un arreglo de bytes a ser comprimido
     * @return El documento comprimido como un arreglo de bytes
     * @throws IOException
     */
    public static byte[] compress(String nombre, byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(stream);
        zip.putNextEntry(new ZipEntry(nombre));
        zip.write(data);
        zip.closeEntry();
        return stream.toByteArray();
    }

    /**
     * @param zipped El contenido del archivo comprimido como un arreglo de
     * bytes
     * @return El contenido de dicho archivo
     * @throws IOException
     */
    public static byte[] uncompress(byte[] zipped) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipped));
        ZipEntry entry = in.getNextEntry();
        InputStreamReader isr = new InputStreamReader(in);
        int b;
        while ((b = isr.read()) != -1)
            stream.write(b);

        isr.close();
        in.close();
        return stream.toByteArray();
    }

    /**
     * Metodo de utilidad para descomprimir a partir de un arreglo de bytes
     *
     * @param zipped El archivo comprimido
     * @param name El nombre del archivo a recuperar
     * @return El archivo que tiene nombre <b>name</b> o null si no se encuentra
     * @throws IOException
     */
    public static byte[] uncompress(byte[] zipped, String name) throws IOException {
        byte[] f = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipped));
        ZipEntry entry = null;
        while ((entry = in.getNextEntry()) != null) {
            if (entry.getName().equals(name)) {
                InputStreamReader isr = new InputStreamReader(in);
                int b;
                while ((b = isr.read()) != -1)
                    stream.write(b);

                isr.close();
                f = stream.toByteArray();
                break;
            }
        }
        return f;
    }

}