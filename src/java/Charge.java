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
import java.util.Calendar;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

@Named(value = "charge")
@SessionScoped
@ManagedBean

public class Charge implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    private String type;
    private Float amount;
    private Date startDate;
    private Date endDate;
    private List<String> chargeTypes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public List<String> getChargeTypes() {
        return chargeTypes;
    }
    
    public void setChargeTypes(List<String> chargeTypes) {
        this.chargeTypes = chargeTypes;
    }
    
    public String createNewCharge() throws SQLException {
        Connection con = dbConnect.getConnection();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        Date newDate = startDate;
        
        System.out.println("BEFORE WHILE LOOP");

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = 
                con.prepareStatement("insert into defaultcharge(type, amount) values(?, ?)");
        
        preparedStatement.setString(1, type);
        preparedStatement.setFloat(2, amount);
        
        preparedStatement.executeUpdate();
        
        preparedStatement = 
                con.prepareStatement("insert into charge(type, amount, day, defaultcharge_id) "
                        + "values(?, ?, ?, (select id from defaultcharge where defaultcharge.type = ?))");
        
        while (! newDate.equals(endDate)) {
            preparedStatement.setString(1, type);
            preparedStatement.setFloat(2, amount);
            preparedStatement.setDate(3, new java.sql.Date(newDate.getTime()));
            preparedStatement.setString(4, type);

            preparedStatement.executeUpdate();
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
            newDate = cal.getTime();
            
            System.out.println("New Date: " + newDate);
            System.out.println(endDate);
        }

         System.out.println("AFTER WHILE LOOP");
        con.commit();
        con.close();
        
        return "/employee/employeeDashboard.xhtml";
    }
    
    public List<String> getAllChargeTypes() throws SQLException {
        Connection con = dbConnect.getConnection();
        
        List<String> cTypes = new ArrayList<>();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement("select type from defaultcharge");
        
        ResultSet result = preparedStatement.executeQuery();
        
        while (result.next()) {
            cTypes.add(result.getString("type"));
        }

        con.commit();
        con.close();
        
        return cTypes;
    }
    
    @PostConstruct
    public void init() {
        try {
            this.chargeTypes = getAllChargeTypes();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
