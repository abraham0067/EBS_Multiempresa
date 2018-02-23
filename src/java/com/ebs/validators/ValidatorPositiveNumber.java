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
@FacesValidator("com.ebs.validators.validatorPositiveNumber")
public class ValidatorPositiveNumber implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        try {
            if (new BigDecimal(value.toString()).signum() < 1) {
                FacesMessage msg = new FacesMessage("Validation failed.",
                        "El valor debe ser positivo.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        } catch (NumberFormatException ex) {
            FacesMessage msg = new FacesMessage("Validation failed.", "Not a number");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
