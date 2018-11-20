// class driver
import java.sql.*;
import java.util.*;

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
        System.out.println("Driver, what would you like to do?");
        System.out.println("1. Take a request");
        System.out.println("2. Finish a trip");
        System.out.println("3. Check driver rating");
        System.out.println("4. Go back");
        System.out.println("Please enter [1-4].");
    }

    // take a request
    public void take_a_request(){
        Scanner input = new Scanner(System.in);
        ResultSet result = null;
        PreparedStatement p = null;
        
        int driver_id;
        int request_id;
        // String passenger_name = new String();
        // String no_of_passenger = new String();
      
        System.out.println("Please enter your ID.");
        driver_id = input.nextInt();

        String sql = "SELECT R.id, P.name, R.passenger " 
        + "FROM trip T request R driver D vehicle V Passenger P " 
        + "WHERE P.id = trip.passenger_id " 
        + "AND T.driver_id = D.driver_id "
        + "AND D.driver_id = ? "
        + "AND D.vehicle = V.id "
        + "AND T.end <> null "
        + "AND V.model like R.model "
        + "AND V.seats >= R.seats "
        + "AND V.model_year > R.model_year";

        try {
            System.out.println("a");
            System.out.println(this.con);
            p = this.con.prepareStatement(sql);
            System.out.println("b");
            p.setInt(1, driver_id);
            result = p.executeQuery();
            ResultSetMetaData data = result.getMetaData();
            int no_of_columns = data.getColumnCount();
            int[] valid_request_id = new int[no_of_columns];
            // pointer to the valid request array
            int pointer_valid_request = 0;
            
            System.out.println("Request ID, Passenger name, Passengers");
            while (result.next()) {
                int temp;
                for (int i = 0; i<no_of_columns;i++){
                    temp = result.getInt(i+1);
                    if (i == 0){
                        valid_request_id[pointer_valid_request] = temp;
                        pointer_valid_request ++;
                        System.out.print(temp);
                    }
                    else {
                        System.out.print(", " + temp);                        
                    }
                }
                System.out.println();
            }
            result.close();


            boolean valid = false;
            do {
                System.out.println("Please enter the request ID.");
                request_id = input.nextInt();
                for (int i=0; i< no_of_columns; i++){
                    if (request_id == valid_request_id[i]){
                        valid = true;
                    }
                }
                if (valid){
                    sql = "Update request R SET R.taken = TRUE WHERE R.id = ?";
                    try{
                        p = this.con.prepareStatement(sql);
                        p.setInt(1, request_id);
                        p.executeUpdate();
                    }
                    catch(SQLException e){
                        ;
                    }
                    try {
                        p = this.con.prepareStatement("INSERT INTO trip (driver_id, passenger_id) FROM SELECT (d.driver_id) AS driver_id, (p.passenger_id) AS passenger_id FROM driver D request R"
                        + "WHERE D.driver_id = ? and R.id = ?");
                        p.setInt(1, driver_id);
                        p.setInt(2, request_id);
                        p.executeUpdate();
                    }
                    catch (SQLException e){
                        ;
                    }

                    try{
                        int last_id = -1;
                        result = this.con.createStatement().executeQuery("SELECT LAST_INSERT_ID;");
                        while(result.next()){
                            last_id = result.getInt(1);
                        }

                        sql = "SELECT T.id, P.name, T.start FROM passenger P, trip T WHERE T.id = ?, P.id = T.passenger_id;";
                        p = this.con.prepareStatement(sql);
                        p.setInt(1, last_id);
                        result = p.executeQuery();
                        while(result.next()){
                            int temp;
                            for (int i = 0; i<no_of_columns;i++){
                                temp = result.getInt(i+1);
                                if (i == 0){
                                    valid_request_id[pointer_valid_request] = temp;
                                    pointer_valid_request++;
                                    System.out.print(temp);
                                }
                                else {
                                    System.out.print(", " + temp);
                                }                        
                            }
                            System.out.println();
                        }
                
                    }
                    catch(SQLException e){
                        ;
                    }
                }
                else{
                    System.out.println("Invalid ID. Please enter again.");
                }
       
            } while(!valid); 
            System.out.println("no bug");
        }
        catch (SQLException e){
            ;
        }

    }

    // finish a trip 
    public void finish_a_trip(){
        ;
    }

    // check driver rating
    public void check_driver_rating(){
        ;
    }

  
}