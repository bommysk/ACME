/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shubham.kahal
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Named(value = "bill")
@SessionScoped
@ManagedBean
public class Bill implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    private Integer reservationID;
    private String chargeType;
    private Date billDate = new Date();
    private Integer roomNumber;
    private Float amount;
    private List<Bill> roomBill;
    private List<Bill> chargeBill;
    private List<Bill> specificReservationRoomBill;
    private List<Bill> specificReservationChargeBill;
    private Room room;
    
    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
    }

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
    public List<Bill> getRoomBill() {
        return this.roomBill;
    }

    public void setRoomBill(List<Bill> roomBill) {
        this.roomBill = roomBill;
    }

    public List<Bill> getChargeBill() {
        return chargeBill;
    }

    public void setChargeBill(List<Bill> chargeBill) {
        this.chargeBill = chargeBill;
    }

    public List<Bill> getSpecificReservationRoomBill() {
        return specificReservationRoomBill;
    }

    public void setSpecificReservationRoomBill(List<Bill> specificReservationRoomBill) {
        this.specificReservationRoomBill = specificReservationRoomBill;
    }

    public List<Bill> getSpecificReservationChargeBill() {
        return specificReservationChargeBill;
    }

    public void setSpecificReservationChargeBill(List<Bill> specificReservationChargeBill) {
        this.specificReservationChargeBill = specificReservationChargeBill;
    }
    
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public String addToChargeBill() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("insert into chargebill(reservation_id, chargetype, day, amount) "
                + "values(?, ?, ?, ?)");
        
        preparedStatement.setInt(1, reservationID);
        preparedStatement.setString(2, chargeType);
        preparedStatement.setDate(3, new java.sql.Date(billDate.getTime()));
        
        PreparedStatement chargePreparedStatement = con.prepareStatement("select amount from charge where type = ? and day = ?");
        chargePreparedStatement.setString(1, chargeType);
        chargePreparedStatement.setDate(2, new java.sql.Date(billDate.getTime()));
        
        ResultSet chargeResult = chargePreparedStatement.executeQuery();
        
        if (chargeResult.next()) {
            preparedStatement.setFloat(4, chargeResult.getFloat("amount"));
        }
        else {
            PreparedStatement defaultChargePreparedStatement = con.prepareStatement("select amount from defaultcharge where type = ?");
            defaultChargePreparedStatement.setString(1, chargeType);
            ResultSet defaultChargeResult = defaultChargePreparedStatement.executeQuery();
            
            defaultChargeResult.next();
            
            preparedStatement.setFloat(4, defaultChargeResult.getFloat("amount"));
        }
        
        preparedStatement.executeUpdate();
        
        PreparedStatement insertChargeTransactionHistoryPreparedStatement
                = con.prepareStatement(
                        "insert into chargetransactionhistory(reservation_id, customer_id, chargetype, day, amount) "
                            + "select reservation_id, customer_id, chargetype, day, amount from chargebill join reservation "
                            + "on chargebill.reservation_id = reservation.id where reservation_id = ?"
                            + " order by reservation_id");
        
        insertChargeTransactionHistoryPreparedStatement.setInt(1, this.reservationID);
        
        insertChargeTransactionHistoryPreparedStatement.executeUpdate();

        con.commit();
        con.close();
        
        return "/employee/employeeDashboard.xhtml";
    }
    
    public List<Bill> getRoomBillList() throws SQLException {
        Connection con = dbConnect.getConnection();
        ArrayList<Bill> roomBill = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        } 
        
       PreparedStatement preparedStatement = con.prepareStatement(
                    "select reservation_id, room_number, view, type, day, amount from roomtransactionhistory"
                            + " where customer_id = ? order by reservation_id;");

       preparedStatement.setInt(1, (new Customer()).getCustomerID(Util.getUserName()));
       
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        while (result.next()) {
            Bill bill = new Bill();
            
            bill.room = new Room();
            
            bill.setReservationID(result.getInt("reservation_id"));
            bill.setRoomNumber(result.getInt("room_number"));
            bill.room.setView(result.getString("view"));
            bill.room.setType(result.getString("type"));
            bill.setBillDate(result.getDate("day"));
            bill.setAmount(result.getFloat("amount"));
            
            roomBill.add(bill);
        }
        
        return roomBill;
    }
    
    public List<Bill> getChargeBillList() throws SQLException {
        Connection con = dbConnect.getConnection();
        ArrayList<Bill> chargeBill = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        } 
        
        PreparedStatement preparedStatement = con.prepareStatement(
               "select reservation_id, chargetype, day, amount from chargetransactionhistory"
               + " where customer_id = ? order by reservation_id;");
       
        preparedStatement.setInt(1, (new Customer()).getCustomerID(Util.getUserName()));
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        while (result.next()) {
            Bill bill = new Bill();
                
            bill.setReservationID(result.getInt("reservation_id"));
            bill.setChargeType(result.getString("chargetype"));
            bill.setAmount(result.getFloat("amount"));
            bill.setBillDate(result.getDate("day"));
            
            chargeBill.add(bill);
        }
        
        return chargeBill;
    }
    
    public List<Bill> getSpecificReservationRoomBillList() throws SQLException {
        Connection con = dbConnect.getConnection();
        ArrayList<Bill> roomBill = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        } 
        
        PreparedStatement preparedStatement = con.prepareStatement(
                    "select reservation_id, reservation.room_number, view, type, day, amount from roombill join reservation "
                            + "on roombill.reservation_id = reservation.id join room on room.room_number = reservation.room_number"
                            + " where reservation_id = ? order by reservation_id");

        preparedStatement.setInt(1, this.reservationID);

        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        while (result.next()) {
            Bill bill = new Bill();
            
            bill.room = new Room();
            
            bill.setReservationID(result.getInt("reservation_id"));
            bill.setRoomNumber(result.getInt("room_number"));
            bill.room.setView(result.getString("view"));
            bill.room.setType(result.getString("type"));
            bill.setBillDate(result.getDate("day"));
            bill.setAmount(result.getFloat("amount"));
            
            roomBill.add(bill);
        }
        
        return roomBill;
    }
    
    public List<Bill> getSpecificReservationChargeBillList() throws SQLException {
        Connection con = dbConnect.getConnection();
        ArrayList<Bill> chargeBill = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        } 
        
       PreparedStatement preparedStatement = con.prepareStatement(
                    "select reservation_id, chargetype, day, amount from chargebill join reservation "
                            + "on chargebill.reservation_id = reservation.id where reservation_id = ?"
                            + " order by reservation_id");
       
       preparedStatement.setInt(1, reservationID);

        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        while (result.next()) {
            Bill bill = new Bill();
                      
            bill.setReservationID(result.getInt("reservation_id"));
            bill.setChargeType(result.getString("chargetype"));
            bill.setAmount(result.getFloat("amount"));
            bill.setBillDate(result.getDate("day"));
            
            chargeBill.add(bill);
        }
        
        return chargeBill;
    }
}
