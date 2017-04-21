import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.List;

@Named(value = "room")
@SessionScoped
@ManagedBean
public class Room implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private String view;
    private String type;
    private Integer roomNumber;
    private Double price;
    
    public DBConnect getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
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

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
     public List<Room> getRoomList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select view, type, room.room_number, price from room join roomprice on room.room_number = roomprice.room_number order by room_number");

        //get customer data from database
        ResultSet result = ps.executeQuery();

        List<Room> roomList = new ArrayList<>();

        while (result.next()) {
            Room room = new Room();
            
            room.setView(result.getString("view"));
            room.setType(result.getString("type"));
            room.setRoomNumber(result.getInt("room_number"));
            room.setPrice(result.getDouble("price"));
            
            //store all data into a List
            roomList.add(room);
        }
        
        result.close();
        con.close();
        
        return roomList;
    }
     
    public Integer getNextRoomNumber(String view, String type) throws SQLException {
        Connection con = dbConnect.getConnection();
        Integer roomNum;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);

        PreparedStatement preparedStatement = con.prepareStatement("select min(room_number) room_number from room where view = ? and type = ?");
        preparedStatement.setString(1, view);
        preparedStatement.setString(2, type);
        
        ResultSet result = preparedStatement.executeQuery();

        result.next();
        
        roomNum = result.getInt("room_number");
        
        System.out.println("select min(room_number) room_number from room where view = " + view + " and type = " + type);
        
        result.close();
        con.close();
        
        return roomNum;
    }
}
