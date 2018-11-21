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
      
        System.out.println("Please enter your ID.");
        driver_id = input.nextInt();

        String sql = "SELECT COUNT(*) " + "FROM request R, driver D, vehicle V, passenger P "
                + "WHERE P.id = R.passenger_id " + "AND D.vehicle_id = V.id " + "AND V.seats>=R.passengers "
                + "AND (V.model_year>=R.model_year OR R.model_year IS NULL) "
                + "AND (V.model like CONCAT('%', R.model, '%') OR R.model IS NULL) " + "AND R.taken = 0 "
                + "AND D.id = ? " + "AND D.id NOT IN (SELECT T2.driver_id from trip T2 where T2.end IS NULL);";

        
        
        try {
            int no_of_columns = 0;
            p = this.con.prepareStatement(sql);
            p.setInt(1, driver_id);
            result = p.executeQuery();
            while(result.next()){
                no_of_columns =result.getInt(1);
            }
            sql = "SELECT R.id, P.name, R.passengers " 
            + "FROM request R, driver D, vehicle V, passenger P " 
            + "WHERE P.id = R.passenger_id " 
            + "AND D.vehicle_id = V.id "
            + "AND V.seats>=R.passengers "
            + "AND (V.model_year>=R.model_year OR R.model_year IS NULL) "
            + "AND (V.model like CONCAT('%', R.model, '%') OR R.model IS NULL) "
            + "AND R.taken = 0 "
            + "AND D.id = ? "
            + "AND D.id NOT IN (SELECT T2.driver_id from trip T2 where T2.end IS NULL);";
            p = this.con.prepareStatement(sql);
            p.setInt(1, driver_id);
            result = p.executeQuery();
            
            ResultSetMetaData data = result.getMetaData();
            int[] valid_request_id = new int[no_of_columns];
            // pointer to the valid request array
            int pointer_valid_request = 0;
            System.out.println(no_of_columns);
            if (no_of_columns>0){
                while (result.next()) {
                    System.out.println("Request ID, Passenger name, Passengers");
                    
                    int temp;
                    for (int i = 0; i<3;i++){
                        if (i == 0){
                            temp = result.getInt(i + 1);
                            valid_request_id[pointer_valid_request] = temp;
                            pointer_valid_request ++;
                            System.out.print(temp);
                        }
                        else if (i==1){
                            System.out.print(", " + result.getString(i+1));                        
                        }
                        else{
                            System.out.print(", " + result.getInt(i+1));
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
                            p = this.con.prepareStatement("INSERT INTO trip (driver_id, passenger_id, start) values (?, ?, ?);");
                            p.setInt(1, driver_id);
                            p.setInt(2, request_id);
                            p.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                            p.executeUpdate();
                        }
                        catch (SQLException e){
                            System.out.println(e);
                        }

                        try{
                            pointer_valid_request =0;
                            sql = "SELECT T.id, P.name, T.start FROM passenger P, trip T WHERE P.id = T.passenger_id AND T.driver_id = ? AND T.end IS NULL;";
                            p = this.con.prepareStatement(sql);
                            p.setInt(1, driver_id);
                            result = p.executeQuery();
                            while(result.next()){
                                System.out.println(result.getInt(1)+", " +result.getString(2) + ", " + result.getTimestamp(3));
                            }
                
                        }
                        catch(SQLException e){
                            System.out.println(e);
                        }
                    }
                    else{
                        System.out.println("Invalid ID.");
                        valid = true;
                    }
       
                } while(!valid);
            }
            else{
                System.out.println("No eligible request.");
            } 
        }
        catch (SQLException e){
            System.out.println(e);
        }

    }

    // finish a trip 
    public void finish_a_trip(){
        int driver_id;
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your ID.");
        driver_id = input.nextInt();
        PreparedStatement p= null;
        ResultSet result = null;
        char choice;
        int current_id=-1;
        String sql = "SELECT T.id, T.passenger_id, T.start FROM trip T WHERE T.driver_id = ? AND T.end IS NULL;";
        try{
            p = this.con.prepareStatement(sql);
            p.setInt(1, driver_id);
            result = p.executeQuery();
            if (result.next()){
                System.out.println("Trip ID, Passenger ID, Start");
                
     
                current_id = result.getInt(1);
                System.out.println(current_id+ ", "+ result.getInt(2) + ", " + result.getTimestamp(3));
                
                System.out.println("Do you wish to finish the trip? [y/n]");
                System.out.println("a");
                choice = input.next(".").charAt(0);
                System.out.println("b");

                if (choice == 'y'){
                    sql = "UPDATE trip SET end = CURRENT_TIMESTAMP(), fee = TIMESTAMPDIFF(MINUTE, start, end) WHERE id = ?;";
                    p = this.con.prepareStatement(sql);
                    p.setInt(1,current_id);
                    p.executeUpdate();

                    sql = "SELECT T.id, P.name, T.start, T.end, T.Fee FROM trip T, passenger P WHERE T.id = ? AND T.passenger_id = P.id";
                    p = this.con.prepareStatement(sql);
                    p.setInt(1, current_id);
                    result = p.executeQuery();
                    while (result.next()) {
                        System.out.println("Trip ID, Passenger name, Start, End, Fee");
                        System.out.println(result.getString(1) + ", " + result.getString(2) + ", "
                        + result.getTimestamp(3) + ", " + result.getTimestamp(4) + ", " + result.getInt(5));
                    }
                }
                else{
                    // do nothing
                    ;
                }
                
            }
            else{
                System.out.println("You do not have an unfinished trip!");
            }
            
        }
        catch (SQLException e){
            
            System.out.println(e);
        }
        
    }

    // check driver rating
    public void check_driver_rating(){
        int driver_id;
        boolean has_five_rating = false;
        System.out.println("Please enter your ID.");
        Scanner input = new Scanner(System.in);
        driver_id = input.nextInt();
        ResultSet result = null;
        PreparedStatement p = null;
        String sql = "SELECT COUNT(*) FROM trip T WHERE T.driver_id = ? AND T.rating <> 0";
        try{
            p = this.con.prepareStatement(sql);
            p.setInt(1, driver_id);
            result = p.executeQuery();
            if (result.next()){
                if (result.getInt(1) >= 5){
                    has_five_rating = true;
                }
            }

            if (has_five_rating){

                sql = "SELECT T.rating FROM trip T WHERE T.driver_id = ? AND T.rating <> 0 ORDER BY T.start DESC;";
                int total = 0;
                p = this.con.prepareStatement(sql);
                p.setInt(1, driver_id);
                result = p.executeQuery();
                int[] average = new int[5];
                for (int i=0; i<5;i++){
                    result.next();
                    total += result.getInt(1);
                    System.out.println(total);

                }
           
                System.out.println("Your driver rating is "+(float)total/5 +".");
            }
            else{
                System.out.println("Your rating is not yet determined.");
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

  
}