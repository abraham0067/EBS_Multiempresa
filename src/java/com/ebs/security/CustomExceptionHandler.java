package com.ebs.security;

import org.primefaces.context.RequestContext;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) { this.wrapped = wrapped; }

    @Override
    public ExceptionHandler getWrapped() { return wrapped; }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
            Throwable throwable = context.getException();

            try {
                throwable.printStackTrace();
                String error=throwable.getMessage()+"("+throwable.getClass().getName()+")";

                if ( throwable.getClass()==javax.faces.application.ViewExpiredException.class ) {
                    error = "Your session have been expired. Please, reload and keep working. Thanks.";
                    System.out.println("---------Expiro session-------------");
                }

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "We are sorry :(", error );
                RequestContext.getCurrentInstance().showMessageInDialog(message);

            } finally {
                iterator.remove();
            }
        }

        getWrapped().handle();
    }

}
