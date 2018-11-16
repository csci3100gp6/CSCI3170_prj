// class Passenger
import java.sql.*;
import java.util.*;

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
    
    // Disconnect the DB
    public void close_connection() {
        try {
            this.con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // implements printStatements
    public void print_statements() {
        System.out.println("Passenger, what would you like to do?");
        System.out.println("1. Request a ride");
        System.out.println("2. Check trip records");
        System.out.println("3. Rate a trip");
        System.out.println("4. Go back");
        System.out.println("Please enter [1-4].");
    }

    // request a ride
    public void request_a_ride(){
        Scanner input = new Scanner(System.in);
        int pid, no_of_passenger;
        int model_year=-1;
        String model_name = new String("");
        String sql = new String("SELECT COUNT(*) FROM vehicle V, driver D WHERE V.id == D.vehicle_id AND V.seats >= %s");
        
        System.out.println("Please enter your passenger ID.");
        pid = input.nextInt();

        System.out.println("Please enter the number of passengers.");
        no_of_passenger = input.nextInt();
        sql = String.format(sql, Integer.toString(no_of_passenger));

        System.out.println("Please enter the earlist model year. (Press enter to skip)");
        model_year = input.nextInt();

        System.out.println("Please enter the model. (Press enter to skip)");
        model_name = input.nextLine();

        // Users enter model name
        if (!model_name.equals("")){
            String cond = String.format(" AND V.model LIKE '%%s%'", model_name);
            sql += cond;
        }

        // User enter model year 
        if (model_year != -1) {
            String cond = String.format(" AND V.model_year >= %s", Integer.toString(model_year));
            sql += cond;
        }

        Statement stmt = null;
        ResultSet result = null;

        try {
            stmt = this.con.createStatement();
            result = stmt.executeQuery(sql);
            int driver_num = -1;

            if (!result.isBeforeFirst()){
                System.out.println("No record found. Please adjust the criteria.");
            }
            else {
                driver_num = result.getInt(1);
                System.out.println("Your request is placed. " + Integer.toString(driver_num) + " drivers are able to take the request.");
            }

            result.close();
        } 
        catch (SQLException e) {
            System.out.println(e);
        }
        finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException sqlEx) {
                }
                result = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    // check a trip
    public void check_a_trip(){
        // read input
        Scanner input = new Scanner(System.in);   
        int pid;
        String start_date = new String();
        String end_date = new String();
        System.out.println("Please enter your ID.");
        pid = input.nextInt();
        System.out.println("Please enter the start date.");
        start_date = input.nextLine();
        System.out.println("Please enter the end date.");
        end_date = input.nextLine();

        // execute query
        Statement stmt = null;
        ResultSet result = null;
        String sql = "SELECT * FROM trip T WHERE T.passenger_id = 'pid' AND T.start = 'start_date' AND T.end = 'end_date';";

        try {
            System.out.println("Number of Records in each table:");
            result = this.con.createStatement().executeQuery(sql);
            ResultSetMetaData data = result.getMetaData();
            while (result.next()){
                for (int j=0; j<data.getColumnCount();j++){
                    if (j>1){
                        System.out.print(", ");
                    }
                    System.out.print(result.getString(j));
                }
                System.out.println();
            }
            result.close();
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException sqlEx) {
                }
                result = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    // rate a trip
    public void rate_a_trip(){
        ;
    }

}