// class driver
import java.sql.*;

public class Driver implements User{
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;
    private Connection con;
    // constructor 
    public Driver(){
        this.dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db18";
        this.dbUsername = "Group18";
        this.dbPassword = "soa3170";
    }
    
    // Connect to database
    public void connect_to_db() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("Connected successfully");
    }

    // implements printStatements
    public void printStatements() {
        System.out.println("Driver, what would you like to do?");
        System.out.println("1. Take a request");
        System.out.println("2. Finish a trip");
        System.out.println("3. Check driver rating");
        System.out.println("4. Go back");
        System.out.println("Please enter [1-4].");
    }

    // take a request
    public void take_a_request(){
        ;
    }

    // finish a trip 
    public void finish_a_trip(){
        ;
    }

    // check driver rating
    public void check_driver_rating(){
        ;
    }

    // go back
    public void go_back(){
        return;

    }
}