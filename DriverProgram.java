// the driver program
// import java.util.Scanner;
import java.util.*;
public class DriverProgram{
    // print the welcome statement and retrieve the user type
    public static void print_welcome_statement(){
        System.out.println("Welcome! Who are you?");
        System.out.println("1. Administrator");
        System.out.println("2. A passenger");
        System.out.println("3. A driver");
        System.out.println("4. None of the above");
        System.out.println("Please enter [1-4].");
    }
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);        
        boolean valid_input = true;
        int user_type = -1;
        print_welcome_statement();
        
        // ask the user to input user_type: admin, passenger, or driver until valid input is located
        do {
            if (valid_input == false){
                System.out.println("[ERROR] Invalid Input.");
                System.out.println("Please enter [1-4].");
            }
            // catch exception if the input is not even an integer
            try{
                user_type = input.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("exception catched");
            }
            finally {
                ;
            }
            
            valid_input = false;
            
        }
        while (!(user_type>0 && user_type<5));
        // close the scanner to avoid memory leak and reset valid_input bool var
        int choice = -1;
        valid_input = true;

        // create a new user according to the type
        switch(user_type){
            // administrator
            case 1: 
                Administrator u1 = new Administrator();
                u1.connect_to_db();
                do {
                    
                    System.out.println(valid_input);
                    if (valid_input == true){
                        u1.print_statements();
                    }
                    valid_input = true;
                    // catch exception if the input is not even an integer
                    try {
                        choice = input.nextInt();
                    } 
                    catch (InputMismatchException e) {
                        ;
                    }
                    
                    switch(choice){
                        // create table
                        case 1:
                            System.out.println("create");
                            u1.create();
                            break;
                        // Delete table
                        case 2:
                            System.out.println("delete");
                            u1.delete();
                            break;
                        // Load data
                        case 3:
                            System.out.println("loaded");
                            u1.load();
                            break;
                        // Check data
                        case 4:
                            System.out.println("check");
                            u1.check();
                            break;
                        // Go back
                        case 5:
                            u1.close_connection();
                            break;
                        // invalid input
                        default:
                            System.out.println("[ERROR] Invalid Input.");
                            System.out.println("Please enter [1-5].");
                            valid_input = false;
                            break;
                    }
                } while (choice != 5);
                break;

            // This user is a passenger
            case 2:
                Passenger u2 = new Passenger();
                u2.connect_to_db();
                do {
                    if (valid_input == true){
                        u2.print_statements();
                    }
                    valid_input = true;
                    
                    // catch exception for invalid input
                    try {
                        choice = input.nextInt();
                    } catch (NumberFormatException e) {
                        ;
                    }
                    
                    switch (choice) {
                        // request a ride
                        case 1:
                            // System.out.println("request a ride");
                            u2.request_a_ride();
                            break;
                        // check a trip
                        case 2:
                            // System.out.println("check a trip");
                            u2.check_a_trip();
                            break;
                        // rate a trip
                        case 3:
                            // System.out.println("rate a trip");
                            u2.rate_a_trip();
                            break;
                        case 4:
                            u2.close_connection();
                            break;
                        // invalid input
                        default:
                            System.out.println("[ERROR] Invalid Input.");
                            System.out.println("Please enter [1-4].");
                            valid_input = false;
                            break;
                    }
                } while (choice != 4);
                break;
            case 3:
                Driver u3 = new Driver();
                u3.connect_to_db();
                do {
                    if (valid_input == true){
                        u3.print_statements();
                    }
                    valid_input = true;
                // catch exception if the input is not even an integer
                    try {
                        choice = input.nextInt();
                    } catch (NumberFormatException e) {
                        ;
                    }
                    switch (choice) {
                    // take a request
                    case 1:
                        // System.out.println("take a request");
                        u3.take_a_request();
                        break;
                    // finish a trip
                    case 2:
                        // System.out.println("finish a trip");
                        u3.finish_a_trip();
                        break;
               
                    // check driver rating
                    case 3:
                        // System.out.println("check driver rating");
                        u3.check_driver_rating();
                        break;
                    case 4:
                        u3.close_connection();
                        break;
                    // invalid input
                    default:
                        System.out.println("[ERROR] Invalid Input.");
                        System.out.println("Please enter [1-4].");
                        valid_input = false;
                        break;
                    }
                } while (choice != 4);
                break;
            case 4:
                break;
        }

        // Avoid memory leak
        input.close();
        
    }
}