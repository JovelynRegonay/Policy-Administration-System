import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class Vehicle extends Policy {
    public static RatingEngine ratingEngine = new RatingEngine();
    String make, model, type, fuel, color, year, inputNum, price, numOfVehicle;
    public static double doublePrice;
    public static int intYear;
    static double premium;
    double doubleInput;

    public void vehicleInfo() { // method that gets all the information about vehicle from the user.

        s.nextLine();
        System.out.print("Vehicle make (Toyota, Ford, BMW, etc. ): ");
        make = s.nextLine();
        System.out.print("Vehicle model: ");
        model = s.nextLine();
        vehicleYear();
        System.out.print("Vehicle Type (4-door sedan, 2-door sports car, SUV, or truck): ");
        type = s.nextLine();
        s.nextLine();
        System.out.print("Vehicle Fuel Type (Diesel, Electric, Petrol): ");
        fuel = s.nextLine();
        vehiclePrice();
        System.out.print("Vehicle color: ");
        s.nextLine();
        color = s.nextLine();

    }

    public boolean numIsValid(String inputNum, int validMin, int validMax) { // integer validation for integer inputs
        boolean valid = false;
        if (inputNum.equals("")) {
            valid = false;
        } else {
            try {
                double doubleInput = Double.parseDouble(inputNum);
                double doublevalidMin = Double.valueOf(validMin);
                double doublevalidMax = Double.valueOf(validMax);

                if (doubleInput >= doublevalidMin && doubleInput <= doublevalidMax) {
                    this.doubleInput = doubleInput;
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("NOTICE!!: INVALID INPUT! Please input within the range!\n");
                }
            } catch (Exception e) {
                valid = false;
            }
        }
        return valid;
    }

    public void vehicleYear() { // method that validates if the input of user is in correct format for vehicle
                                // year.
        boolean methodResult = false;

        while (methodResult == false) {

            System.out.print("Vehicle Year [YYYY]: ");
            year = s.next();

            int yearNow = LocalDate.now().getYear();

            if (numIsValid(year, yearNow - 40, yearNow) == true) {
                intYear = Integer.parseInt(year);
                methodResult = true;
            } else {
                methodResult = false;
                System.out.println("NOTICE: INVALID INPUT! Please try again!\n");
            }
        }

    }

    public void vehiclePrice() { // method that validates if the inputted vehicle price is valid.

        boolean methodResult = false;

        while (methodResult == false) {

            System.out.print("Vehicle Purchase Price: ");
            price = s.next();

            if (numIsValid(price, 1, 999999999) == true) {
                doublePrice = Double.parseDouble(price);

                methodResult = true;
            } else {

                methodResult = false;
                System.out.println("NOTICE: INVALID INPUT! Please try again!\n");
            }
        }

    }

    public void numOfVehicleInput() { // method that validates if the inputted number of vehicle is valid.

        System.out.println("+-----------------------------------------------------+\n");
        System.out.println("Do you want to get a qoutation?");
        System.out.println("Enter 1 if YES, enter any key if NO.");

        boolean methodResult = false;
        String choice = s.next();

        if (choice.equals("1")) {

            while (methodResult == false) {

                System.out
                        .print("\nHow many vehicle do you wish to enroll in this policy? NOTE: Maximum of 99 vehicles only: ");
                numOfVehicle = s.next();

                if ((numIsValid(numOfVehicle, 1, 99)) == true) {
                    int intNumOfVehicle = Integer.parseInt(numOfVehicle);
                    methodResult = true;

                    for (int i = 1; i <= intNumOfVehicle; i++) {

                        System.out.println("+-----------------------------------------------------+");
                        System.out.println("                    Vehicle #" + (i));
                        System.out.println("+-----------------------------------------------------+");
                        vehicleInfo();
                        ratingEngine.premiumComputation();
                        premium += ratingEngine.calculatedPremium;
                        saveVehicleToDB();

                    }

                    System.out.println("\n+-----------------------------------------------------+");
                    System.out.println("TOTAL PREMIUM CHARGE : \t $" + String.format("%,.2f", premium));
                    System.out.println("+-----------------------------------------------------+");

                } else {

                    methodResult = false;
                }
            }

        } else {

            System.out.println("Transaction cancelled. Thank you!");
            PASDriver.menu();
        }

    }

    public void saveVehicleToDB() { // method that saves the vehicle inputs to DB

        int policyNum = Policy.policyNo;

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String vehicle = "INSERT into vehicle_temp (make, model , year, type, fuel_type, purchase_price, color, premium_charge, policy_no, account_no) VALUES ('"
                    + make + "', '" + model + "', '" + intYear + "', '" + type + "' , '" + fuel + "' , '" + doublePrice
                    + "' , '"
                    + color + "' , '" + premium + "', '" + policyNum + "'  , '" + getAccountNo() + "' )";

            PreparedStatement preparedStmt = con.prepareStatement(vehicle);
            preparedStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void deleteVehiclesOnDB() { // method that deletes vehicle from DB

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String deleteVehicle = "DELETE FROM vehicle_temp where policy_no = '" + policyNo + "'";
            PreparedStatement preparedStmt = con.prepareStatement(deleteVehicle);
            preparedStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
