import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named(value = "reservation")
@SessionScoped
@ManagedBean
public class Reservation implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private Integer reservationID;
    private Integer roomNumber;
    private String view;
    private String type;
    private String startDate;
    private String endDate;
    private Double price;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startingDate) {
        this.startDate = startingDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String createReservation() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("insert into reservation(customer_id, room_number, start_date, end_date) "
                + "values((select id from customer where login = ?),?,?,?)");
        
        preparedStatement.setString(1, Util.getUserName());
        preparedStatement.setInt(2, (new Room()).getNextRoomNumber(view, type));
        preparedStatement.setDate(3, new java.sql.Date(new Date(startDate).getTime()));
        preparedStatement.setDate(4, new java.sql.Date(new Date(endDate).getTime()));
        
        preparedStatement.executeUpdate();
        
        con.commit();
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
            reservation.setStartDate(result.getString("start_date"));
            reservation.setEndDate(result.getString("end_date"));
   
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
            reservation.setStartDate(result.getString("start_date"));
            reservation.setEndDate(result.getString("end_date"));
   
            //store all data into a List
            reservationList.add(reservation);
        }
        
        result.close();
        con.close();
        
        return reservationList;
    }
}
