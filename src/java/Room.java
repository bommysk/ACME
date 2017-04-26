import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Float amount;
    private Date startDate;
    private Date endDate;
    private Date day;
    private ArrayList<Integer> allRoomNumbers = new ArrayList<>();
    private List<Room> roomList;
    private Room selectedRoom;

    public Room() {
        this.view = "";
        this.type = "";
        this.roomNumber = 0;
        this.amount = new Float(0.0);
    }
    
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
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

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }
    
    public List<Room> generateRoomList() throws SQLException, ParseException {

        Connection con = dbConnect.getConnection();
        ArrayList<Integer> roomNumbers = new ArrayList<>(); // this is used to keep track of pricing
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        setAllRoomNumbersDefault();
        
        PreparedStatement preparedStatement = con.prepareStatement(
                    "select view, type, room.room_number, amount, day from room join roomprice "
                            + "on room.room_number = roomprice.room_number order by room_number");

        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        List<Room> roomList = new ArrayList<>();

        while (result.next()) {
            Room room = new Room();
            
            room.setView(result.getString("view"));
            room.setType(result.getString("type"));
            room.setRoomNumber(result.getInt("room_number"));
            room.setAmount(result.getFloat("amount"));
            room.setDay(result.getDate("day"));
            
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
                    room.setAmount(new Float(100.0));
                    room.setStartDate(new SimpleDateFormat("MM/dd/yyyy" ).parse("01/01/1900"));
                    room.setEndDate(new SimpleDateFormat("MM/dd/yyyy" ).parse("01/01/2900" ));
                    //store all data into a List
                    System.out.println("ADDING ROOM");
                    roomList.add(room);
                }
            }
        }
        
        result.close();
        con.close();
        
        return roomList;
    }
    
    public boolean roomPriceExists(Integer roomNumber, Date day) throws SQLException {
        Connection con = dbConnect.getConnection();
        
        PreparedStatement preparedStatement = con.prepareStatement(
                    "select count(*) count from roomprice where room_number = ? and day = ?");

        preparedStatement.setInt(1, roomNumber);
        preparedStatement.setDate(2, new java.sql.Date(day.getTime()));
        
        //get customer data from database
        ResultSet result = preparedStatement.executeQuery();
        
        result.next();
        
        return result.getInt("count") == 1;
    }
    
    @PostConstruct
    public void init() {
        try {
            this.roomList = generateRoomList();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        
        this.selectedRoom = new Room();
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
    
    public Room findRoom(Integer roomNumber) throws RoomNotFound {
        for (Room room : this.roomList) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        
        throw new RoomNotFound();
    }
    
    public void select(Room r) {
        this.selectedRoom = r;
    }
    
    public String update() throws SQLException, ParseException, RoomNotFound {
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        Date tempDate = this.selectedRoom.getStartDate();
        Date finalDate = this.selectedRoom.getEndDate();
        
        PreparedStatement updatePreparedStatement = con.prepareStatement(
                    "update roomprice set amount = ? where day = ? and room_number = ?");
        
        PreparedStatement insertPreparedStatement = con.prepareStatement(
                    "insert into roomprice(room_number, amount, day) values(?, ?, ?)");
        
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(finalDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        finalDate = cal.getTime();
        
        cal.setTime(tempDate);
        
        while (! tempDate.equals(finalDate)) {
            
            if (roomPriceExists(this.selectedRoom.getRoomNumber(), tempDate)) {
                updatePreparedStatement.setFloat(1, this.selectedRoom.getAmount());
                updatePreparedStatement.setDate(2, new java.sql.Date(tempDate.getTime()));
                updatePreparedStatement.setInt(3, this.selectedRoom.getRoomNumber());

                updatePreparedStatement.executeUpdate();
                
                System.out.println("Executing update.");
            }
            else {
                insertPreparedStatement.setInt(1, this.selectedRoom.getRoomNumber());
                insertPreparedStatement.setFloat(2, this.selectedRoom.getAmount());
                insertPreparedStatement.setDate(3, new java.sql.Date(tempDate.getTime()));

                insertPreparedStatement.executeUpdate();
            }
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
            tempDate = cal.getTime();
            
            System.out.println("Temp Date: " + tempDate);
            
            con.commit();
        }
        
        this.roomList = generateRoomList();
        
        con.close();
        
        return "roompricing";
    }
    
    public String delete() throws SQLException, ParseException, RoomNotFound {
        Room roomObj = findRoom(this.selectedRoom.getRoomNumber());
        
        Date tempDate = this.selectedRoom.getStartDate();
        Date finalDate = this.selectedRoom.getEndDate();
        
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        con.setAutoCommit(false);
        
        PreparedStatement preparedStatement = con.prepareStatement(
                    "delete from roomprice where day = ? and room_number = ?");
        
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(finalDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        finalDate = cal.getTime();
        
        cal.setTime(tempDate);
        
        while (! tempDate.equals(finalDate)) {
            
            if (roomPriceExists(this.selectedRoom.getRoomNumber(), tempDate)) {
                preparedStatement.setDate(1, new java.sql.Date(tempDate.getTime()));
                preparedStatement.setInt(2, this.selectedRoom.getRoomNumber());

                preparedStatement.executeUpdate();
                
                System.out.println("Executing update.");
            }
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
            tempDate = cal.getTime();
            
            System.out.println("Temp Date: " + tempDate);
            
            con.commit();
        }
        
        this.selectedRoom = new Room();
        this.roomList = generateRoomList();
        
        con.close();
        
        return "roompricing";
    }
}

class RoomNotFound extends Exception {
}
