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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named(value = "employeelogin")
@SessionScoped
@ManagedBean
public class EmployeeLogin implements Serializable {

    private Boolean isAdmin;
    private String employeeLogin;
    private String employeePassword;
    private UIInput loginUI;
    private final DBConnect dbConnect = new DBConnect();
    
    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public String getEmployeeLogin() {
        return employeeLogin;
    }

    public void setEmployeeLogin(String employeeLogin) {
        this.employeeLogin = employeeLogin;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }
    
    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }
    
    public boolean checkLoginPassword(String login, String password) throws SQLException {
        
        Connection con = dbConnect.getConnection();
        String loginDB, passwordDB;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select is_admin, login, password from employee where"
                                + " login = ? and password = ?");
        
        ps.setString(1, login);
        ps.setString(2, password);
        
        ResultSet result = ps.executeQuery();
        
        if (! result.next()) {
            System.out.println("Please don't be in here.");
            return false;
        }

        isAdmin = (result.getInt("is_admin") == 1);
        loginDB = result.getString("login");
        passwordDB = result.getString("password");
        
        result.close();
        con.close();
        
        System.out.println("Employee Login: " + loginDB);
        System.out.println("Employee Password: " + passwordDB);
        
        return (login.equals(loginDB) && password.equals(passwordDB));
    }

    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        employeeLogin = loginUI.getLocalValue().toString();
        employeePassword = value.toString();

        if (! checkLoginPassword(employeeLogin, employeePassword)) {
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);
        }
    }

    public String go() {
        if (isAdmin) {
            EmployeeUtil.validateAdminSession(employeeLogin);
            
            EmployeeUtil.validateEmployeeSession(employeeLogin);
        }
        else {
            EmployeeUtil.validateEmployeeSession(employeeLogin);
            
            System.out.println("EMP LOGIN: " + EmployeeUtil.getEmployeeLogin());
        }
        
        return "success";
    }
    
    //logout event, invalidate session
    public String logout() {
        EmployeeUtil.invalidateEmployeeSession();

        return "logout";
    }
}
