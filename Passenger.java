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
        String pid;
        String no_of_passenger = new String();
        String model_year = new String();
        String model_name = new String();
        String sql = new String("SELECT COUNT(*) FROM vehicle V, driver D WHERE V.id = D.vehicle_id AND V.seats >= %s");
        String sql_col = new String("(passenger_id, passengers, taken");
        String sql_value = new String("(%s, %s, FALSE");
        String sql_error = new String("SELECT COUNT(*) FROM passenger P WHERE P.id = %s;");
        
        System.out.println("Please enter your passenger ID.");
        pid = input.nextLine();
        sql_error = String.format(sql_error, pid);

        System.out.println("Please enter the number of passengers.");
        no_of_passenger = input.nextLine();
        sql = String.format(sql, no_of_passenger);
        sql_value = String.format(sql_value, pid, no_of_passenger);

        System.out.println("Please enter the earlist model year. (Press enter to skip)");
        model_year = input.nextLine();

        System.out.println("Please enter the model. (Press enter to skip)");
        model_name = input.nextLine();

        // Users enter model name
        if (!model_name.equals("")){
            String cond = " AND V.model LIKE '%" + model_name + "%'";
            String col = ", model";
            String value = ", '" + model_name + "'";
            sql += cond;
            sql_col += col;
            sql_value += value;
        }

        // User enter model year 
        if (!model_year.equals("")) {
            String cond = String.format(" AND V.model_year >= %s", model_year);
            String col = ", model_year";
            String value = ", " + model_year;
            sql += cond;
            sql_col += col;
            sql_value += value;
        }

        sql += ';';
        sql_col += ")";
        sql_value += ")";
        Statement stmt = null;
        ResultSet result = null;
        Statement stmt_err = null;
        ResultSet result_err = null;
        Statement stmt_insert = null;
        String sql_request = new String("INSERT INTO request " + sql_col + " VALUES " + sql_value + ";");

        try {   
            stmt_err = this.con.createStatement();
            result_err = stmt_err.executeQuery(sql_error);
            result_err.next();
            int valid_count = result_err.getInt(1);

            if (valid_count <= 0) {
                System.out.println("[ERROR] Passenger not found.");
            }
            else {
                stmt = this.con.createStatement();
                result = stmt.executeQuery(sql);
                result.next();
                int driver_num = result.getInt(1);

                if (driver_num == 0) {
                    System.out.println("No record found. Please adjust the criteria.");
                }
                else {
                    System.out.println("Your request is placed. " + Integer.toString(driver_num) + " drivers are able to take the request.");
                    stmt_insert = this.con.createStatement();
                    // System.out.println(sql_request);
                    int count = stmt_insert.executeUpdate(sql_request);
                    stmt_insert.close();
                }

                stmt.close();
                result.close();
            }

            result_err.close();
            stmt_err.close();
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
        String pid = new String();
        String start_date = new String();
        String end_date = new String();
        System.out.println("Please enter your ID.");
        pid = input.nextLine();
        System.out.println("Please enter the start date.");
        start_date = input.nextLine();
        System.out.println("Please enter the end date.");
        end_date = input.nextLine();

        // execute query
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Statement stmt_err = null;
        ResultSet result_err = null;
        start_date += " 00:00:00";
        end_date += " 00:00:00";

        String sql_error = new String("SELECT COUNT(*) FROM passenger P WHERE P.id = %s;");
        sql_error = String.format(sql_error, pid);

        String sql = "SELECT T.id, D.name, V.id, V.model, T.start, T.end, T.fee, T.rating " +
                     "FROM trip T, vehicle V, driver D WHERE T.driver_id = D.id AND D.vehicle_id = V.id " +
                     "AND T.passenger_id = ? AND T.start >= ? AND T.end <= ? " +
                     "ORDER BY T.start DESC;";

        try {
            stmt_err = this.con.createStatement();
            result_err = stmt_err.executeQuery(sql_error);
            result_err.next();
            int valid_count = result_err.getInt(1);

            if (valid_count <= 0) {
                System.out.println("[ERROR] Passenger not found.");
            }
            else {
                pstmt = this.con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(pid));
                pstmt.setTimestamp(2, Timestamp.valueOf(start_date));
                pstmt.setTimestamp(3, Timestamp.valueOf(end_date));
                result = pstmt.executeQuery();
                ResultSetMetaData rsmd = result.getMetaData();

                if (result.isBeforeFirst()) {

                    System.out.println("Trip ID, Driver Name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");

                    while (result.next()){
                        for (int j=1; j<=rsmd.getColumnCount();j++){
                            if (j>1){
                                System.out.print(", ");
                            }
                            System.out.print(result.getString(j));
                        }
                        System.out.println();
                    }
                }
                else {
                    System.out.println("No matched trip is found.");
                }

                pstmt.close();
                result.close();
            }
            stmt_err.close();
            result_err.close();
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
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException sqlEx) {
            }
            pstmt = null;
            }
        }
    }

    // rate a trip
    public void rate_a_trip(){
        Scanner input = new Scanner(System.in);
        String pid = new String();
        String tid = new String();
        String rating = new String();

        System.out.println("Please enter your ID.");
        pid = input.nextLine();

        System.out.println("Please enter the trip ID.");
        tid = input.nextLine();

        System.out.println("Please enter the rating.");
        rating = input.nextLine();

        String sql_error = new String("SELECT COUNT(*) FROM passenger P WHERE P.id = %s;");
        sql_error = String.format(sql_error, pid);
        
        if (Integer.parseInt(rating) > 5 || Integer.parseInt(rating) < 1) {
            System.out.println("The value of rating should be between 1 and 5.");
            return;
        }

        String sql_retrieve = "SELECT T.id, D.name, V.id, V.model, T.start, T.end, T.fee, T.rating " +
                              "FROM trip T, vehicle V, driver D WHERE T.driver_id = D.id AND D.vehicle_id = V.id " +
                              "AND T.passenger_id = ? AND T.id = ?;";

        String sql_update = "UPDATE trip T " + 
                            "SET T.rating = ? " +
                            "WHERE T.passenger_id = ? AND T.id = ?;";

        PreparedStatement pstmt_retrieve = null;
        PreparedStatement pstmt_update = null;
        Statement stmt_err = null;
        ResultSet result_err = null;
        int updateCount = -1;
        ResultSet result = null;

        try {
            stmt_err = this.con.createStatement();
            result_err = stmt_err.executeQuery(sql_error);
            result_err.next();
            int valid_count = result_err.getInt(1);

            if (valid_count <= 0) {
                System.out.println("[ERROR] Passenger not found.");
            }
            else {
                pstmt_update = this.con.prepareStatement(sql_update);
                pstmt_update.setInt(1, Integer.parseInt(rating));
                pstmt_update.setInt(2, Integer.parseInt(pid));
                pstmt_update.setInt(3, Integer.parseInt(tid));
                updateCount = pstmt_update.executeUpdate();
                pstmt_update.close();
            }
            stmt_err.close();
            result_err.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally {
            if (pstmt_update != null) {
                try {
                    pstmt_update.close();
                }
                catch (SQLException e) {
                    System.out.println(e);
                }
            }

            pstmt_update = null;
        }

        try {
            if (updateCount > 0) {
                pstmt_retrieve = this.con.prepareStatement(sql_retrieve);
                pstmt_retrieve.setInt(1, Integer.parseInt(pid));
                pstmt_retrieve.setInt(2, Integer.parseInt(tid));
                result = pstmt_retrieve.executeQuery();

                ResultSetMetaData rsmd = result.getMetaData();
                System.out.println("Trip ID, Driver Name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");

                while (result.next()){
                    for (int j=1; j<=rsmd.getColumnCount();j++){
                        if (j>1){
                            System.out.print(", ");
                        }
                        System.out.print(result.getString(j));
                    }
                    System.out.println();
                }
                pstmt_retrieve.close();
                result.close();
            }
            else {
                System.out.println("No matched trip is found.");
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally {
            if (result != null) {
                try {
                    result.close();
                }
                catch (SQLException e) {
                    System.out.println(e);
                }
            }
            result = null;

            if (pstmt_retrieve != null) {
                try {
                    pstmt_retrieve.close();
                }
                catch (SQLException e) {
                    System.out.println(e);
                }
            }

            pstmt_retrieve = null;
        }
    }

}










