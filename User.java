import java.sql.*;

public interface User{
    public void connect_to_db();
    public void print_statements();
    public void close_connection();
}