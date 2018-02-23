package com.ebs.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by eflores on 16/11/2017.
 */
public class JasperZip33 {

        public byte[] getZipBytes(String[] fJaspers, String[] fImages)
                throws Exception {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bout);
            // Reports
            for (String fJ : fJaspers) {
                ZipEntry zent = new ZipEntry("reports/" + fJ);
                zout.putNextEntry(zent);
                zout.write(getBytes(fJ));
                zout.closeEntry();
            }
            // Images
            for (String fI : fImages) {
                ZipEntry zent = new ZipEntry("images/" + fI);
                zout.putNextEntry(zent);
                zout.write(getBytes(fI));
                zout.closeEntry();
            }
            zout.flush();
            zout.close();

            return bout.toByteArray();
        }

        public InputStream setUnZipBytes(byte[] bytes,
                                         HashMap<String, Object> parameters) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ZipInputStream zin = new ZipInputStream(bais);
            ZipEntry zent = zin.getNextEntry();
            byte[] b = new byte[512];
            InputStream stream = null;

            int item = 0;
            while (zent != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                int buff = zin.read(b);
                while (buff > 0) {
                    bout.write(b, 0, buff);
                    buff = zin.read(b);
                }
                String name = zent.getName().toUpperCase().split("\\/")[1];
                name = name.toUpperCase().replaceAll(" ", "_")
                        .replaceAll("\\.", "_");

                if ("reports".equals(zent.getName().split("/")[0])) {
                    if (item == 0) {
                        stream = new ByteArrayInputStream(bout.toByteArray());
                    } else {
                        parameters.put(name,
                                new ByteArrayInputStream(bout.toByteArray()));
                    }

                    item++;
                }
                if ("images".equals(zent.getName().split("/")[0])) {
                    BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(
                            bout.toByteArray()));
                    parameters.put(name, bimg);
                }
                zin.closeEntry();

                zent = zin.getNextEntry();
            }
            zin.close();

            return stream;
        }

        private byte[] getBytes(String file) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);

            byte[] b = new byte[1024];

            int buff = fis.read(b);
            while (buff > 0) {
                baos.write(b, 0, buff);
                buff = fis.read(b);
            }
            fis.close();

            return baos.toByteArray();
        }
    }
