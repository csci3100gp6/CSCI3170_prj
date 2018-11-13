// the driver program
import java.util.Scanner;

public class DriverProgram{
    // print the welcome statement and retrieve the user type
    public static void print_welcome_statement(){
        System.out.println("Welcome! Who are you?");
        System.out.println("1. Adminstrator");
        System.out.println("2. A passenger");
        System.out.println("3. A driver");
        System.out.println("4. None of the above");
        System.out.println("Please enter [1-4].");
        
    }

    public static void main(String[] args){
        boolean valid_input = true;

        // ask the user to input user_type: admin, passenger, or driver until valid input is located
        do {
            if (valid_input == false){
                System.out.println("[ERROR] Invalid Input.");
            }
            print_welcome_statement();
            String input = new Scanner(System.in);
            // catch exception if the input is not even an integer
            try{
                user_type = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                ;
            }
            valid_input = false;
        }
        while (user_type>0 && user_type<5);

        // create a new user according to the type
        switch(user_type){
            case 1: 
            
        }

    }
}