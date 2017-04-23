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
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

@Named(value = "bill")
@SessionScoped
@ManagedBean
public class Bill implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    private int reservationID;
    private String chargeType;
    private Date billDate = new Date();
    
    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
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
    
    public String addToBill() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("insert into bill(reservation_id, chargeprice_id, bill_date) "
                + "values(?, (select id from charge where type = ?), ?)");
        
        preparedStatement.setInt(1, reservationID);
        preparedStatement.setString(2, chargeType);
        preparedStatement.setDate(3, new java.sql.Date(billDate.getTime()));
        
        preparedStatement.executeUpdate();

        con.commit();
        con.close();
        
        return "/employee/employeeDashboard.xhtml";
    }
}
