import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class PolicyHolder {
    Policy policy = new Policy();
    Scanner s = new Scanner(System.in);
    String address, driversLicenseNo, dateOfBirth, inputDLDate;
    public static String DLDateIssuedDB, firstName, lastName;
    String name;

    public void policyHolderInfo() { // method that gets all the information for policy holder
        System.out.println("----------------------------------------------------");
        System.out.println("============POLICY HOLDER'S INFORMATION=============");
        System.out.println("----------------------------------------------------");

        s.nextLine();
        System.out.print("Enter Policy holder's first name: ");
        firstName = s.nextLine();
        System.out.print("Enter Policy holder's last name: ");
        lastName = s.nextLine();
        getDateOfBirth();
        s.nextLine();
        System.out.print("Address: ");
        address = s.nextLine();
        System.out.print("Enter driver's license number: ");
        driversLicenseNo = s.nextLine();
        System.out.print("Date of driver's license first issue [YYYY-MM-DD]: "); // string disector (first
        inputDLDate = s.next();
        DLDateIssuedDB = DLDateIssued(inputDLDate);

    }

    public void getDateOfBirth() { // method that validates if the input of user is in correct format for date of
                                   // birth
        int repeatInput = 0;

        while (!(repeatInput == 2)) {
            System.out.print("Date of birth [YYYY-MM-DD]: ");
            dateOfBirth = s.next();

            if (policy.dateIsValid(dateOfBirth) == 2) {
                repeatInput = 2;
            } else {
                System.out.println("\nNOTICE: Invalid date. Please try again!\n");
            }
        }
    }

    public String DLDateIssued(String date) { // method that validates if the input of user is in correct format for
                                              // drivers license date issued.

        boolean repeatInput = false;
        while (repeatInput == false) {
            try {
                if (policy.dateIsValid(date) == 1 || policy.dateIsValid(date) == 2) {
                    DLDateIssuedDB = inputDLDate;
                    repeatInput = true;
                } else {
                    repeatInput = false;
                    System.out.println("NOTICE: Invalid date. Please try again! \n");
                    System.out.print("Date of driver's license first issue [YYYY-MM-DD]: "); // string disector (first
                    inputDLDate = s.next();
                    date = inputDLDate;
                }
            } catch (Exception e) {
                repeatInput = false;

            }
        }
        return DLDateIssuedDB;
    }

    public void savePolicyHolderToDB() { // method that saves the policy holder to database.
        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String policyHolder = "INSERT into policy_holder_temp (first_name, last_name , date_of_birth, address, drivers_license, date_of_DL_first_issue) VALUES ('"
                    + firstName + "', '" + lastName + "', '" + dateOfBirth + "' , '" + address + "', '"
                    + driversLicenseNo
                    + "', '" + DLDateIssuedDB + "')";

            PreparedStatement preparedStmt = con.prepareStatement(policyHolder);
            preparedStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
