// class Passenger
import java.sql.*;

public class Passenger implements User{
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;
    private Connection con;

    // constructor
    public Passenger(){
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
        System.out.println("Passenger, what would you like to do?");
        System.out.println("1. Request a ride");
        System.out.println("2. Check trip records");
        System.out.println("3. Rate a trip");
        System.out.println("4. Go back");
        System.out.println("Please enter [1-4].");
    }

    // request a ride
    public void request_a_ride(){
        ;
    }

    // check a trip
    public void check_a_trip(){
        ;
    }

    // rate a trip
    public void rate_a_trip(){
        ;
    }

    // go back
    // public void go_back(){
        // return;
    // }
}