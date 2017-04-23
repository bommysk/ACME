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
import javax.annotation.PostConstruct;

@Named(value = "room")
@SessionScoped
@ManagedBean
public class Room implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private String view;
    private String type;
    private Integer roomNumber;
    private Double price;
    private ArrayList<Integer> allRoomNumbers = new ArrayList<>();
    private List<Room> roomList;

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
    
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
    
    public ArrayList<Integer> getAllRoomNumbers() {
        return allRoomNumbers;
    }

    public void setAllRoomNumbers(ArrayList<Integer> allRoomNumbers) {
        this.allRoomNumbers = allRoomNumbers;
    }
    
    public void setAllRoomNumbersDefault() {
        for (int floor_number = 1; floor_number <= 5; floor_number++) {
            for (int room_number = 1; room_number <= 12; room_number++) {
                this.allRoomNumbers.add(Integer.parseInt(floor_number + "" + room_number));
            }
        }        
    }
    
    public List<Room> generateRoomList() throws SQLException {

        Connection con = dbConnect.getConnection();
        ArrayList<Integer> roomNumbers = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        setAllRoomNumbersDefault();
        
        PreparedStatement preparedStatement = con.prepareStatement(
                    "select view, type, room.room_number, amount from room join roomprice "
                            + "on room.room_number = roomprice.room_number order by room_number");

        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        List<Room> roomList = new ArrayList<>();

        while (result.next()) {
            Room room = new Room();
            
            room.setView(result.getString("view"));
            room.setType(result.getString("type"));
            room.setRoomNumber(result.getInt("room_number"));
            room.setPrice(result.getDouble("amount"));
            
            roomNumbers.add(room.getRoomNumber());
            
            roomList.add(room);
        }
        
        // if a price has not been set for the room yet, we want to set a default
        // price
        for (Integer rNumber : this.allRoomNumbers) {
            if (! roomNumbers.contains(rNumber)) {
                preparedStatement = con.prepareStatement(
                    "select view, type, room_number from room where room_number = ?");

                preparedStatement.setInt(1, rNumber);
                result = preparedStatement.executeQuery();
                
                while (result.next()) {
                    Room room = new Room();

                    room.setView(result.getString("view"));
                    room.setType(result.getString("type"));
                    room.setRoomNumber(result.getInt("room_number"));
                    room.setPrice(100.0);
                    
                    //store all data into a List
                    roomList.add(room);
                }
            }
        }
        
        result.close();
        con.close();
        
        return roomList;
    }
    
    @PostConstruct
    public void init() {
        try {
            this.roomList = generateRoomList();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
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
                
        result.close();
        con.close();
        
        return roomNum;
    }
    
    public void setRoomPrice() {
        System.out.println("Setting room price.");
    }
}
