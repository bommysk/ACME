import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named(value = "reservation")
@SessionScoped
@ManagedBean
public class Reservation implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private Integer reservationID;
    private Integer customerID;
    private Integer roomNumber;
    private String view;
    private String type;
    private Date startDate;
    private Date endDate;
    private Double totalPrice;
    private Room room;
    private List<Reservation> customerReservationList;
    private Date checkinDate = new Date();
    Bill bill = new Bill();

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }
    
    public Integer getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startingDate) {
        this.startDate = startingDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public List<Reservation> getCustomerReservationList() {
        return this.customerReservationList;
    }
    
    public void setCustomerReservationList(List<Reservation> customerReservationList) {
        this.customerReservationList = customerReservationList;
    }
    
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }
    
    public String createReservation() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("insert into reservation(customer_id, room_number, start_date, end_date) "
                + "values((select id from customer where login = ?),?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
        
        preparedStatement.setString(1, Util.getUserName());
        preparedStatement.setInt(2, (new Room()).getNextRoomNumber(view, type));
        preparedStatement.setDate(3, new java.sql.Date(this.startDate.getTime()));
        preparedStatement.setDate(4, new java.sql.Date(this.endDate.getTime()));
        
        preparedStatement.executeUpdate();
        
        con.commit();
        
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                insertRoomBill(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating Reservation failed, no ID obtained.");
            }
        }
        
        con.close();
        
        return "customerDashboard";
    }
    
    public List<Reservation> getReservationList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select id, reservation.room_number, view, type, start_date, end_date from reservation join room "
                                + "on reservation.room_number = room.room_number order by start_date");
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();

        List<Reservation> reservationList = new ArrayList<>();

        while (result.next()) {
            Reservation reservation = new Reservation();
            
            reservation.setReservationID(result.getInt("id"));
            reservation.setRoomNumber(result.getInt("room_number"));
            reservation.setView(result.getString("view"));
            reservation.setType(result.getString("type"));
            reservation.setStartDate(result.getDate("start_date"));
            reservation.setEndDate(result.getDate("end_date"));
   
            //store all data into a List
            reservationList.add(reservation);
        }
        
        result.close();
        con.close();
        
        return reservationList;
    }
    
    public List<Reservation> getSpecificCustomerReservationList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select id, reservation.room_number, view, type, start_date, end_date from reservation join room "
                                + "on reservation.room_number = room.room_number where "
                                + "customer_id = (select id from customer where login = ?) order by start_date");

        preparedStatement.setString(1, Util.getUserName());
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();

        List<Reservation> reservationList = new ArrayList<>();

        while (result.next()) {
            Reservation reservation = new Reservation();
            
            reservation.setReservationID(result.getInt("id"));
            reservation.setRoomNumber(result.getInt("room_number"));
            reservation.setView(result.getString("view"));
            reservation.setType(result.getString("type"));
            reservation.setStartDate(result.getDate("start_date"));
            reservation.setEndDate(result.getDate("end_date"));
            
            // loop through these dates and for each get the price form the room price table and add them to the reservation
   
            //store all data into a List
            reservationList.add(reservation);
        }
        
        result.close();
        con.close();
        
        return reservationList;
    }
    
    public void insertRoomBill(Integer reservationId) throws SQLException {
        
        Connection con = dbConnect.getConnection();
        Date tempDate;
        Date finalDate;
        Calendar cal = Calendar.getInstance();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement
                = con.prepareStatement(
                        "select id, reservation.room_number, view, type, start_date, end_date from reservation join room "
                                + "on reservation.room_number = room.room_number where "
                                + "customer_id = (select id from customer where login = ?) and id = ?");
        
        PreparedStatement roomPricePreparedStatement
                = con.prepareStatement(
                        "select amount from roomprice where room_number = ? and day = ?");
        
        PreparedStatement roomDefaultPricePreparedStatement
                = con.prepareStatement(
                        "select amount from defaultroomprice where room_number = ?");
        
        PreparedStatement insertRoomBillPreparedStatement
                = con.prepareStatement(
                        "insert into roombill(reservation_id, day, amount) values(?, ?, ?)");
                                
        
        System.out.println("LOGIN: " + Util.getUserName());
        preparedStatement.setString(1, Util.getUserName());
        System.out.println("reservation id: " + reservationId);
        preparedStatement.setInt(2, reservationId);
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        ResultSet roomPriceResult;
        ResultSet defaultRoomPriceResult;

        while (result.next()) {
            
            tempDate = result.getDate("start_date");
            finalDate = result.getDate("end_date");
                        
            cal.setTime(finalDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            finalDate = cal.getTime();

            cal.setTime(tempDate);
            
            while (! tempDate.equals(finalDate)) {
            
                insertRoomBillPreparedStatement.setInt(1, result.getInt("id"));
                insertRoomBillPreparedStatement.setDate(2, new java.sql.Date(tempDate.getTime()));
 
                roomPricePreparedStatement.setInt(1, result.getInt("room_number"));
                roomPricePreparedStatement.setDate(2, new java.sql.Date(tempDate.getTime()));
                
                roomPriceResult = roomPricePreparedStatement.executeQuery();
                
                if (roomPriceResult.next()) {
                    insertRoomBillPreparedStatement.setFloat(3, roomPriceResult.getFloat("amount"));
                }
                else {
                    roomDefaultPricePreparedStatement.setInt(1, result.getInt("room_number"));
                    
                    defaultRoomPriceResult = roomDefaultPricePreparedStatement.executeQuery();
                     
                    defaultRoomPriceResult.next();
                    
                    insertRoomBillPreparedStatement.setFloat(3, defaultRoomPriceResult.getFloat("amount"));
                }
                
                cal.add(Calendar.DAY_OF_MONTH, 1);
                tempDate = cal.getTime();
                
                insertRoomBillPreparedStatement.executeUpdate();
                
                con.commit();
            }
        }
        
        PreparedStatement insertRoomTransactionHistoryPreparedStatement
                = con.prepareStatement(
                        "insert into roomtransactionhistory(reservation_id, customer_id, room_number, view, type, day, amount) "
                            + "(select reservation_id, customer_id, reservation.room_number, view, type, day, amount from roombill join reservation "
                            + "on roombill.reservation_id = reservation.id join room on room.room_number = reservation.room_number"
                            + " where reservation_id = (select max(id) from reservation where customer_id = ?) order by reservation_id)");
        
        insertRoomTransactionHistoryPreparedStatement.setInt(1, (new Customer()).getCustomerID(Util.getUserName()));
        
        insertRoomTransactionHistoryPreparedStatement.executeUpdate();
        
        result.close();
        con.commit();
        con.close();
    }
    
    public void validateReservationExistence(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;
        Integer submittedReservationID = (Integer) value;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select count(*) as count from reservation where id = ?");
        preparedStatement.setInt(1, submittedReservationID);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
        result.close();
        con.close();
        
        if (count == 0) {
            FacesMessage errorMessage = new FacesMessage("This reservation id does not exist.");
            throw new ValidatorException(errorMessage);
        }
    }
    
    public void validateReservationCancellation(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        Connection con = dbConnect.getConnection();
        int count;
        Integer submittedReservationID = (Integer) value;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select count(*) as count from reservation where id = ? and start_date <= now()");
        preparedStatement.setInt(1, submittedReservationID);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        count = result.getInt("count");
        
        result.close();
        con.close();
        
        if (count > 0) {
            FacesMessage errorMessage = new FacesMessage("This reservation id does not exist.");
            throw new ValidatorException(errorMessage);
        }
    }
    
    public String deleteReservation() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("delete from reservation where id = ?");
        preparedStatement.setInt(1, reservationID);
        
        preparedStatement.executeUpdate();
        
        con.commit();
        con.close();
        
        return "customerDashboard";
    }
    
     public String checkin() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("insert into checkin(reservation_id, checkin_date) values(?, ?)");
        
        preparedStatement.setInt(1, reservationID);
        preparedStatement.setDate(2, new java.sql.Date(checkinDate.getTime()));
        
        preparedStatement.executeUpdate();

        con.commit();
        con.close();
        
        return "/employee/employeeDashboard.xhtml";
    }
     
    public String displayReservationBill() {
        return "displayReservationBill";
    }
     
    public void checkout() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("delete from reservation where id = ?");
        
        preparedStatement.setInt(1, this.bill.getReservationID());
        
        preparedStatement.executeUpdate();

        con.commit();
        con.close();
    }
    
    public Integer getCustomerIDFromReservationID(Integer reservationID) throws SQLException {
        Connection con = dbConnect.getConnection();
        Integer customerID;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select customer_id from reservation where id = ?");
        preparedStatement.setInt(1, this.reservationID);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        customerID = result.getInt("customer_id");
        
        result.close();
        con.close();
        
        return customerID;
    }
}
