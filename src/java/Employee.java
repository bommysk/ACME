
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;

@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee implements Serializable {

    @ManagedProperty(value = "#{employeelogin}")
    private EmployeeLogin login;

    public EmployeeLogin getLogin() {
        return login;
    }

    public void setEmployeeLogin(EmployeeLogin login) {
        this.login = login;
    }

    private DBConnect dbConnect = new DBConnect();
    private Integer isAdmin;
    private String employeeLogin;
    private String employeePassword;
    private String employeeOldPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String postalAddress;
    private Date createdDate = new Date();
    
    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
    }
    
    public Integer getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Integer isAdmin) {
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
    
    public String getEmployeeOldPassword() {
        return employeeOldPassword;
    }

    public void setEmployeeOldPassword(String employeeOldPassword) {
        this.employeeOldPassword = employeeOldPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalAddress() {
        return postalAddress;
    }
    
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getName() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        EmployeeLogin login = (EmployeeLogin) elContext.getELResolver().getValue(elContext, null, "employeelogin");
    
        return login.getEmployeeLogin();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created_date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.createdDate = created_date;
    }

    public String createEmployee() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("Insert into employee(is_admin, login, password, first_name, last_name, email, postal_address, created_date) values(?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, isAdmin);
        preparedStatement.setString(2, employeeLogin);
        preparedStatement.setString(3, employeePassword);
        preparedStatement.setString(4, firstName);
        preparedStatement.setString(5, lastName);
        preparedStatement.setString(6, email);
        preparedStatement.setString(7, postalAddress);
        preparedStatement.setDate(8, new java.sql.Date(createdDate.getTime()));
        preparedStatement.executeUpdate();
        
        statement.close();
        con.commit();
        con.close();
        EmployeeUtil.validateEmployeeSession(employeeLogin);
        
        System.out.println(EmployeeUtil.getEmployeeLogin());
        
        return "index";
    }
    
    public String deleteEmployee() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("delete from employee where login = ?");
        preparedStatement.setString(1, employeeLogin);
        preparedStatement.executeUpdate();
        
        statement.close();
        con.commit();
        con.close();
        
        return "admin";
    }
    
    public void validateLogin(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("Select count(*) as count from employee where login = ?");
        preparedStatement.setString(1, employeeLogin);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
        if (count != 0) {
            FacesMessage errorMessage = new FacesMessage("This login already exists, please pick another one.");
            throw new ValidatorException(errorMessage);
        }
        
        result.close();
        con.close();
    }
    
    public void validateDeleteLogin(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("Select count(*) as count from employee where login = ?");
        preparedStatement.setString(1, employeeLogin);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
        System.out.println("Employee Login: " + employeeLogin);
        System.out.println("Count: " + count);
        
        result.close();
        con.close();
        
        if (count == 0) {
            FacesMessage errorMessage = new FacesMessage("This login does not exist.");
            throw new ValidatorException(errorMessage);
        }
    }
    
    public List<Customer> getCustomerList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select login, first_name, last_name, email, postal_address, created_date from customer order by first_name, last_name");

        //get customer data from database
        ResultSet result = ps.executeQuery();

        List<Customer> custList = new ArrayList<>();

        while (result.next()) {
            Customer cust = new Customer();

            cust.setCustomerLogin(result.getString("login"));
            cust.setFirstName(result.getString("first_name"));
            cust.setLastName(result.getString("last_name"));
            cust.setEmail(result.getString("email"));
            cust.setPostalAddress(result.getString("postal_address"));
            cust.setCreatedDate(result.getDate("created_date"));

            //store all data into a List
            custList.add(cust);
        }
        
        result.close();
        con.close();
        
        return custList;
    }
    
     public List<Employee> getEmployeeList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select is_admin, login, first_name, last_name, email, postal_address, created_date from employee order by first_name, last_name");

        //get customer data from database
        ResultSet result = ps.executeQuery();

        List<Employee> empList = new ArrayList<>();

        while (result.next()) {
            Employee emp = new Employee();

            emp.setIsAdmin(result.getInt("is_admin"));
            emp.setEmployeeLogin(result.getString("login"));
            emp.setFirstName(result.getString("first_name"));
            emp.setLastName(result.getString("last_name"));
            emp.setEmail(result.getString("email"));
            emp.setPostalAddress(result.getString("postal_address"));
            emp.setCreatedDate(result.getDate("created_date"));

            //store all data into a List
            empList.add(emp);
        }
        
        result.close();
        con.close();
        
        return empList;
    }
     
    public String changePassword() throws SQLException {
       Connection con = dbConnect.getConnection();

       if (con == null) {
           throw new SQLException("Can't get database connection");
       }
       con.setAutoCommit(false);

       Statement statement = con.createStatement();

       PreparedStatement preparedStatement = con.prepareStatement("update employee set password = ? where login = ?");
       preparedStatement.setString(1, employeePassword);
       preparedStatement.setString(2, EmployeeUtil.getEmployeeLogin());
       preparedStatement.executeUpdate();

       statement.close();
       con.commit();
       con.close();

       return "admin";
    }
    
    public void validateOldPassword(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select count(*) as count from customer where login = ? and password = ?");
        System.out.println(EmployeeUtil.getEmployeeLogin());
        System.out.println("YOOO");
        System.out.println(employeePassword);
        System.out.println(employeeOldPassword);
        preparedStatement.setString(1, EmployeeUtil.getEmployeeLogin());
        preparedStatement.setString(2, employeeOldPassword);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
        System.out.println(count + " Lets gooo");
        
        if (count == 0) {
            FacesMessage errorMessage = new FacesMessage("Incorrect old password.");
            throw new ValidatorException(errorMessage);
        }
        
        result.close();
        con.close();
    }
}
