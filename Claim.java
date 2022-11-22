import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Claim {
    Policy policy = new Policy();
    Scanner s = new Scanner(System.in);
    int claimNo;
    String dateOfAccident, searchPolicy, accidentAdd, accidentDesc, damageDesc, cost;

    public int claimNoGenerator() { // method that generates the claim no using random function
        Random random = new Random(); // instantiation of object Random
        Integer claimNoGen = random.nextInt(99999); // setting limit of random numbers to 99999
        claimNo = claimNoGen;
        System.out.println("\nYour claim ID is : C" + claimNo + "\n");
        System.out.println("------------------------------------------------");
        return claimNo;
    }

    public void accidentDate() { // method that validates the input of user is in correct date for accident date.
        int repeatInput = 0;

        while (!(repeatInput == 2)) {
            System.out.print("Date of accident [YYYY-MM-DD]: ");
            dateOfAccident = s.next();

            if (policy.dateIsValid(dateOfAccident) == 2) {
                repeatInput = 2;
            } else {
                System.out.println("\nNOTICE: Invalid date. Please try again!\n");
            }
        }
    }

    public void claimInfo() { // method prompts the user with all the claim informations.

        accidentDate();
        s.nextLine();
        System.out.print("Address where accident happened: ");
        accidentAdd = s.nextLine();
        System.out.print("Description of accident");
        accidentDesc = s.nextLine();
        System.out.print("Description of damage to vehicle");
        damageDesc = s.nextLine();
        System.out.print("Estimated cost of repairs ");
        cost = s.nextLine();

    }

    public void searchPolicyNo() { // method that search a specific policy inputted by the user.

        boolean repeatInput = false;
        try {

            Connection con = DriverManager.getConnection( // connects to the mysqldatabase
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            do {

                System.out.println("\n+=========================================+");
                System.out.print("Enter policy number: ");
                searchPolicy = s.next();
                System.out.println("\n+==========================================");

                PreparedStatement stmt = con.prepareStatement(searchPolicy);
                ResultSet rs2 = stmt
                        .executeQuery(
                                "select policy_no from policy_pas_temp where policy_no = '" + searchPolicy + "'");

                if (rs2.next()) {

                    repeatInput = true;
                    System.out.println("FILING OF ACCIDENT CLAIM");
                    claimNoGenerator();

                    claimInfo();
                    saveClaimToDB();

                } else {
                    System.out.println("Policy number is not recognized. Please try again!");
                    repeatInput = false;

                }

            } while (repeatInput == false);

        } catch (Exception e) {

        }
    }

    public void saveClaimToDB() { // method that saves the claim information in the data base.
        try {
            String claimNumber = "C" + claimNo;
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            PreparedStatement preparedStmt = con.prepareStatement(
                    "INSERT into claims_temp (claim_no, date_of_accident, address_of_accident, accident_desc, damage_desc, cost_of_repair) VALUES ('"
                            + claimNumber + "', '" + dateOfAccident + "' , '" + accidentAdd + "' , '" + accidentDesc
                            + "' , '" + damageDesc
                            + "' , '" + cost + "')");

            preparedStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchClaim() { // method that seacrh a specific claim.
        System.out.println("----------------------------------------------------");
        System.out.println("================== SEARCH A CLAIM ==================");
        System.out.println("----------------------------------------------------");
        System.out.print("Enter Claim number: ");
        String searchClaimNo = s.nextLine();

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String searchClaimNum = "select * from claims_temp where claim_no = '" + searchClaimNo + "'";
            PreparedStatement preparedStmt = con.prepareStatement(searchClaimNum);

            ResultSet rs2 = preparedStmt.executeQuery(searchClaimNum);
            while (rs2.next()) {
                System.out.println(
                        "-------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("|%s %20s %24s %24s %24s %25s|\n", "Claim No.", "Date of accident",
                        "Address of Accident",
                        "Accident Description", "Damage Description", "Cost of Repair");
                System.out.printf("|%s %20s %24s %24s %24s %28s|\n", rs2.getString("claim_no"),
                        rs2.getString("date_of_accident"), rs2.getString("address_of_accident"),
                        rs2.getString("accident_desc"),
                        rs2.getString("damage_desc"), rs2.getString("cost_of_repair"));

                System.out.println(
                        "-------------------------------------------------------------------------------------------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}