// class Passenger

public class Passenger implements User{
    // constructor
    public Passenger(){
        ;
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
    public void go_back(){
        return;
    }
}