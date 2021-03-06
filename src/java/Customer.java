
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
    private Date checkinDate = new Date();
    private Room room;
    
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
    
        return login.getCustomerLogin();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created_date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.createdDate = created_date;
    }
    
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String createCustomer() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("insert into customer(login, password, first_name, last_name, email, postal_address, credit_card_number, expiration_date, cvv_code, created_date) values(?,?,?,?,?,?,?,?,?,?)");
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
        
        return "login";
    }
    
    public String createCustomerEmployee()throws SQLException, ParseException {
        createCustomer();
        
        return "/employee/employeeDashboard.xhtml";
    }
    
    public String deleteCustomer() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("delete from customer where login = ?");
        
        preparedStatement.setString(1, customerLogin);
        preparedStatement.executeUpdate();

        con.commit();
        con.close();
                
        return "/employee/employeeDashboard.xhtml";
    }
    
    public void validateLogin(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;
        String submittedLogin = (String) value;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("Select count(*) as count from customer where login = ?");
        preparedStatement.setString(1, submittedLogin);
        
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
    
    public void validateLoginExistence(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;
        String submittedLogin = (String) value;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select count(*) as count from customer where login = ?");
        preparedStatement.setString(1, submittedLogin);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
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
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select login, first_name, last_name, email, postal_address, created_date from customer order by first_name, last_name");

        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();

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
    
    public List<Customer> getCheckedInCustomerList() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select login, first_name, last_name, email, postal_address, room_number, checkin_date from "
                                + "reservation join checkin on checkin.reservation_id = reservation.id join "
                                + "customer on customer.id = reservation.customer_id order by checkin_date");
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();

        List<Customer> custList = new ArrayList<>();

        while (result.next()) {
            Customer cust = new Customer();
            cust.room = new Room();

            cust.setCustomerLogin(result.getString("login"));
            cust.setFirstName(result.getString("first_name"));
            cust.setLastName(result.getString("last_name"));
            cust.setEmail(result.getString("email"));
            cust.setPostalAddress(result.getString("postal_address"));
            cust.room.setRoomNumber(result.getInt("room_number"));
            cust.setCheckinDate(result.getDate("checkin_date"));

            //store all data into a List
            custList.add(cust);
        }
        
        con.commit();
        con.close();
        
        return custList;
    }
    
    public Integer getCustomerID(String custLogin) throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select id from customer where login = ?");

        preparedStatement.setString(1, custLogin);
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        result.next();
        
        return result.getInt("id");
    }
}
