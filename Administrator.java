// Administrator is a user
import java.sql.*;

public class Administrator implements User{
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;
    private Connection con;

    // constructor    
    public Administrator(){
        this.dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db18";
        this.dbUsername = "Group18";
        this.dbPassword = "soa3170";
    }

    // Connect to database
    public void connect_to_db(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }
        catch(ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }
        catch(SQLException e){
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
    public void print_statements(){
        System.out.println("Administrator, what would you like to do?");
        System.out.println("1. Create tables");
        System.out.println("2. Delete tables");
        System.out.println("3. Load data");
        System.out.println("4. Check data");
        System.out.println("5. Go back");
        System.out.println("Please enter [1-5].");
    }

    // creates
    public void create(){
        Statement stmt = null;
        ResultSet result = null;
        // 5 create statements in a roll.
        String[] sql = {"CREATE TABLE driver (id int NOT NULL PRIMARY KEY, name varchar(30),  vehicle_id varchar(6));"
        , "CREATE TABLE vehicle (id varchar(6) NOT NULL PRIMARY KEY, model varchar(30), model_year SMALLINT, seats int);"
        , "CREATE TABLE passenger (id int NOT NULL PRIMARY KEY, name varchar(30));"
        , "CREATE TABLE request (id int NOT NULL AUTO_INCREMENT, passenger_id int, model_year varchar(255), model varchar(255), passengers varchar(255), taken BOOL NOT NULL, PRIMARY KEY(id));"
        , "CREATE TABLE trip(id int NOT NULL PRIMARY KEY, driver_id int, passenger_id int, start DATETIME, end DATETIME, fee int, rating int);"};

        try{
            for (int i = 0; i<5; i++){
                this.con.createStatement().executeUpdate(sql[i]);
            }
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally{
            if (result != null) {
                try {
                    result.close();
                } 
                catch (SQLException sqlEx) {
                }
                result = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } 
                catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        System.out.println("Processing...Done! Tables are created!");
    }

    // deletes 
    public void delete(){
        Statement stmt = null;
        ResultSet result = null;
        String[] sql = {"DROP TABLE driver;"
        , "DROP TABLE vehicle;"
        , "DROP TABLE passenger;"
        , "DROP TABLE request;"
        , "DROP TABLE trip;"};

        try {
            for (int i = 0 ; i<5 ; i++){
                this.con.createStatement().executeUpdate(sql[i]);
            }
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

    // load
    public void load(){
        Statement stmt = null;
        ResultSet result = null;
        String sql = "INSERT INTO driver (id, name, vehicle_id) values (21, 'driver_a', 23);";
        String sql2 = "INSERT INTO vehicle (id, model, model_year, seats) values (27, 'tesla', '2018', 273);";
        // + "SELECT COUNT(*) FROM passenger;"
        // + "SELECT COUNT(*) FROM request;"
        // + "SELECT COUNT(*) FROM trip;";

        try {
            this.con.createStatement().execute(sql);
            this.con.createStatement().execute(sql2);
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

    // check
    public void check(){
        Statement stmt = null;
        ResultSet result = null;
        String[] sql = {"SELECT COUNT(*) FROM driver;"
        , "SELECT COUNT(*) FROM vehicle;" 
        , "SELECT COUNT(*) FROM passenger;" 
        , "SELECT COUNT(*) FROM request;"
        ,"SELECT COUNT(*) FROM trip;"};

        String[] table_names = {"driver", "vehicle", "passenger", "request", "trip"};
        try {
            System.out.println("Number of Records in each table:");
            for (int i=0; i<5;i++){
                result = this.con.createStatement().executeQuery(sql[i]);
                while (result.next()) {
                    System.out.println(table_names[i] + ": " + result.getInt(1));
                }
                result.close();
            }   
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


}