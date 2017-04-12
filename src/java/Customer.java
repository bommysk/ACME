
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.Date;
import java.util.TimeZone;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;

@Named(value = "customer")
@SessionScoped
@ManagedBean
public class Customer implements Serializable {

    @ManagedProperty(value = "#{login}")
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private DBConnect dbConnect = new DBConnect();
    private String customerLogin;
    private String customerPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String postalAddress;
    private Long creditCardNumber;
    private Date expirationDate;
    private Integer cvvNumber;
    private Date createdDate = new Date();

    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
    }
    
    public String getCustomerLogin() {
        return customerLogin;
    }

    public void setCustomerLogin(String customerLogin) {
        this.customerLogin = customerLogin;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
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

    public Long getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(Long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getCvvNumber() {
        return cvvNumber;
    }

    public void setCvvNumber(Integer cvvNumber) {
        this.cvvNumber = cvvNumber;
    }

    public String getName() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
    
        return login.getLogin();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created_date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.createdDate = created_date;
    }

    public String createCustomer() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("Insert into Customer(login, password, first_name, last_name, email, postal_address, credit_card_number, expiration_date, crc_code, created_date) values(?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1, customerLogin);
        preparedStatement.setString(2, customerPassword);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, email);
        preparedStatement.setString(6, postalAddress);
        preparedStatement.setLong(7, creditCardNumber);
        preparedStatement.setDate(8, new java.sql.Date(expirationDate.getTime()));
        preparedStatement.setInt(9, cvvNumber);
        preparedStatement.setDate(10, new java.sql.Date(createdDate.getTime()));
        preparedStatement.executeUpdate();
        
        statement.close();
        con.commit();
        con.close();
        Util.validateUserSession(customerLogin);
        
        System.out.println(Util.getUserName());
        
        return "index";
    }
}
