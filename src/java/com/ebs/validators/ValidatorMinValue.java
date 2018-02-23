/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.validators;

import java.math.BigDecimal;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
@FacesValidator("validatorMin")
public class ValidatorMinValue implements Validator, ClientValidator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if(o == null){
            return;
        }
        int tmp = new BigDecimal(o.toString()).intValue();
        try {
            if (tmp < 10) {
                FacesMessage msg = new FacesMessage("El campo "+uic.getClientId()+" debe ser mayor que 10.","Error.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                //RequestContext.getCurrentInstance().addCallbackParam("succes", false);
                throw new ValidatorException(msg); 
            } else{
                //RequestContext.getCurrentInstance().addCallbackParam("succes", true);
            }
        } catch (NumberFormatException ex) {
            FacesMessage msg = new FacesMessage("Validation failed.", "Not a number");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg); 
        }
    }

    @Override
    public Map<String, Object> getMetadata() {
         return null;
    }

    @Override
    public String getValidatorId() {
        return "validatorMin";
    }
    
}
