// Administrator is a user

public class Administrator implements User{
    // constructor
    public Administrator(){
        ;
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
    public void creates(){
        ;
    }

    // deletes
    public void delete(){
        ;
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