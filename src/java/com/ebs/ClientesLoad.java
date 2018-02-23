package com.ebs;

import fe.db.MDireccion;
import fe.db.MEmpresa;
import fe.db.MReceptor;
import fe.model.dao.DaoGenerico;
import fe.model.dao.DireccionDAO;
import fe.model.dao.FoliadorDao;
import fe.model.dao.FoliosDAO;
import fe.model.dao.ReceptorDAO;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


/**
 * Created by eflores on 09/11/2017.
 */
public class ClientesLoad {
    public static void main(String[] args) {

        HibernateUtilEmi hibManagerSU = new HibernateUtilEmi();
        FoliadorDao dao = new FoliadorDao();
        String fileName = "C:\\AppsData\\A.csv";
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
            stream.forEach(item -> {
                System.out.println("ITEM " + item);
                item = item.replace("NULL","");
                String[] data = item.split(",");
                System.out.println("DATALENGHT:" + data.length);
                MDireccion mydir = new MDireccion();
                MReceptor myrec = new MReceptor();
                MEmpresa emp = new MEmpresa();
                mydir.setCalle(data[6]);
                String cp = data[14];
                if(cp.length()>6){
                    cp = cp.substring(0,5);
                }
                mydir.setCp(cp);
                mydir.setColonia(data[9]);
                mydir.setEstado(data[12]);
                mydir.setLocalidad("");
                mydir.setMunicipio(data[11]);
                mydir.setNoExterior(data[7]);
                mydir.setNoInterior(data[8]);
                mydir.setPais(data[13]);
                mydir.setReferencia("");
                mydir = (MDireccion) dao.saveNewObject(mydir);
                System.out.println("INSERTEDDIRECCION:" + mydir.getId());
                myrec.setDireccion(mydir);
                emp.setId(1);
                myrec.setEmpresa(emp);
                myrec.setRazonSocial(data[4]);
                myrec.setRfcOrigen(data[3]);
                myrec = (MReceptor) dao.saveNewObject(myrec);
                System.out.println("INSERTED:" + myrec.getId() + "\n\n"+
                "--------------------------------------------------------");
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
