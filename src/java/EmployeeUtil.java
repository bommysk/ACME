/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skahal
 */

import java.io.Serializable;
import java.util.*;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named(value = "employeeutil")
@SessionScoped
@ManagedBean
public class EmployeeUtil implements Serializable {

    public static void invalidateEmployeeSession() {
        HttpSession session = getSession();
        session.invalidate();
    }
    
    public static String validateEmployeeSession(String employeeLogin) {
        HttpSession session = getSession();
        session.setAttribute("employeeLogin", employeeLogin);
        System.out.println("VALIDATING");
        return "success";
    }
    
    public static String validateAdminSession(String employeeLogin) {
        HttpSession session = getSession();
        session.setAttribute("adminLogin", employeeLogin);
        return "success";
    }

    public void validateDate(FacesContext context, UIComponent component, Object value)
            throws Exception {

        try {
            Date d = (Date) value;
        } catch (Exception e) {
            FacesMessage errorMessage = new FacesMessage("Input is not a valid date");
            throw new ValidatorException(errorMessage);
        }
    }
    
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                            .getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
            return (HttpServletRequest) FacesContext.getCurrentInstance()
                            .getExternalContext().getRequest();
    }

    public static String getEmployeeLogin() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(false);
        
        return session.getAttribute("employeeLogin").toString();
    }

    public static String getEmployeeId() {
        HttpSession session = getSession();
        if (session != null)
            return (String) session.getAttribute("employeeId");
        else
            return null;
    }
}
