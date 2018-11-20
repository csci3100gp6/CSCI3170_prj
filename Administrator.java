// Administrator is a user
import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

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

    // a method to insert a record into the database
    private void insert_into_db(String inputs, String table_name){
        PreparedStatement p = null;
        String[] elements = inputs.split(",");
        switch(table_name){
            case "drivers.csv":
                try {
                    p = this.con.prepareStatement("INSERT INTO driver (id, name, vehicle_id) values (?, ?, ?);");
                    p.setInt(1, Integer.parseInt(elements[0]));
                    p.setString(2, elements[1]);
                    p.setString(3, elements[2]);
                }
                catch (SQLException e){
                    ;
                }
                break;
            case "vehicles.csv":
                try {
                    p = this.con.prepareStatement("INSERT INTO vehicle (id, model, model_year, seats) values (?, ?, ?, ?);");
                    p.setString(1, elements[0]);
                    p.setString(2, elements[1]);
                    p.setInt(3, Integer.parseInt(elements[2]));
                    p.setInt(4, Integer.parseInt(elements[3]));
                } catch (SQLException e) {
                    ;
                }
                break;
            case "passengers.csv":
                try {
                    p = this.con.prepareStatement("INSERT INTO passenger (id, name) values (?, ?);");
                    p.setInt(1, Integer.parseInt(elements[0]));
                    p.setString(2, elements[1]);
                } catch (SQLException e) {
                    ;
                }
                break;
            case "trips.csv":
                try {
                    p = this.con.prepareStatement("INSERT INTO trip (id, driver_id, passenger_id, start, end, fee, rating) values (?, ?, ?, ?, ?, ?, ?);");
                    for (int j=0;j<7;j++){
                        System.out.print(elements[j] + ",");
                    }
                    System.out.println();
                    p.setInt(1, Integer.parseInt(elements[0]));
                    p.setInt(2, Integer.parseInt(elements[1]));
                    p.setInt(3, Integer.parseInt(elements[2]));
                    p.setTimestamp(4, Timestamp.valueOf(elements[3]));
                    p.setTimestamp(5, Timestamp.valueOf(elements[4]));
                    p.setInt(6, Integer.parseInt(elements[5]));
                    p.setInt(7, Integer.parseInt(elements[6]));

                } catch (SQLException e) {
                    ;
                }   
                break;
            default:
                System.out.println("Sth unexpected happens!");
                break;
        }
        // String sql = "";
        try {
            p.executeUpdate();
            // System
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
        
            if (p != null) {
                try {
                    p.close();
                } catch (SQLException sqlEx) {
                }
                p = null;
            }
        }
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
        , "CREATE TABLE trip(id int NOT NULL AUTO_INCREMENT, driver_id int, passenger_id int, start DATETIME DEFAULT CURRENT_TIMESTAMP, end DATETIME DEFAULT NULL, fee int DEFAULT NULL, rating int DEFAULT NULL, PRIMARY KEY(id));"};

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
        Scanner input = new Scanner(System.in);
        Statement stmt = null;
        ResultSet result = null;
        String path = new String();
        String filelocation = new String();

        String temp = new String();

        // ask user for the input path
        System.out.println("Please enter the folder path.");
        path = input.nextLine();
        path = "./" + path;
        String[] file = {"drivers.csv","vehicles.csv","passengers.csv","trips.csv"};
        for (int i = 0; i<4;i++){
            filelocation = path + "/" + file[i];
            try{
                System.out.println(filelocation);
                Scanner inputfile = new Scanner(new File(filelocation));
                // inputfile.useDelimiter(",");
                while (inputfile.hasNextLine()){
                    temp = inputfile.nextLine();
                    System.out.println(temp);
                    insert_into_db(temp, file[i]);
                }
                inputfile.close();
            }
            catch (FileNotFoundException e){
                System.out.println("file not found");
            }


        }


    }

    // check
    public void check(){
        // System.out.println(this.con);
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