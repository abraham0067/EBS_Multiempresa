/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.conv;

import fe.db.MEmpresa;
import fe.model.dao.EmpresaDAO;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author <Eduardo at EBS>
 */
@FacesConverter("converterEmpresa")
public class ConverterEmpresa implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        //Recibe el string del  elemento que se selecciono
        if(value != null && value.trim().length() > 0) {
            try {
                int index=0;
                //Obtenemos el bean del contexto que nos llego externamente y obtenemos el mapeado del tipo session(alcanze de cada bean) usando el
                //nombre del bean que queremos
                //Necesita estar en el contexto session
//                ManagedBeanEmpresasAsignadas hl = (ManagedBeanEmpresasAsignadas) fc.getExternalContext().getSessionMap().get("managedBeanEmpresasAsignadas");
//                for(MEmpresa tmp:hl.getEmpresasAsignadas()){
//                    if(tmp.getId() == Integer.parseInt(value)){
//                        break;
//                    }
//                    index++;
//                }
                //Otra forma
                MEmpresa tmp = new EmpresaDAO().BuscarEmpresaId(Integer.valueOf(value));
//                return hl.getEmpresasAsignadas().get(index);
                return tmp;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid MEmpresas."));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if(object != null) {
            MEmpresa tmp = (MEmpresa) object;
            String id = String.valueOf(tmp.getId());
            return id;//Se usara para el atributo "value" del tag del selected item
        } 
            //return "NOT CONVERTED";
            return null;
    }
    
}
