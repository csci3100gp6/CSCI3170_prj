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

    // test function, removed in future
    public void test(){
        System.out.println("hi");            
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

    // implements printStatements
    public void printStatements(){
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
        this.connect_to_db();
        Statement stmt = null;
        ResultSet rs = null;
        // unfinished, 4 more tables to go.
        String sql = "CREATE TABLE Driver (id int NOT NULL PRIMARY KEY"
        +", name varchar(255),  vehicle_id varchar(255));";

        try{
            this.con.createStatement().executeUpdate(sql);
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally{
            if (rs != null) {
                try {
                    rs.close();
                } 
                catch (SQLException sqlEx) {
                }
                rs = null;
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
    }

    // deletes
    public void delete(){
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "DROP TABLE Student;";
        try {
            this.con.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
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
        ;
    }

    // check
    public void check(){
        ;
    }

    // go back
    public void go_back(){
        return;
    }

}