/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.validators;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;

/**
 * Valida si el numero de cuenta ingresado en la factura manual es valido, debe
 * contener al menos 4 digitos y deben ser estrictamente numericos
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
@FacesValidator("lengthNumberValidator")
public class ValidatorLengthNumber implements Validator, ClientValidator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if (o == null) {
            return;
        }
        String tmp = o.toString();
        if (!tmp.equals("")) {
            try {
                if (tmp.trim().length() < 4) {
                    FacesMessage msg = new FacesMessage("Error.", "El número de cuenta debe tener al menos 4 digitos.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    //RequestContext.getCurrentInstance().addCallbackParam("succes", false);
                    throw new ValidatorException(msg);
                } else if (Double.isNaN(Double.parseDouble(tmp))) {
                    FacesMessage msg = new FacesMessage("Error.", "El número de cuenta solo debe contener números.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    //RequestContext.getCurrentInstance().addCallbackParam("succes", false);
                    throw new ValidatorException(msg);
                }
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> getMetadata() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }

    @Override
    public String getValidatorId() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "lengthNumberValidator";
    }

}
