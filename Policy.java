import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Policy {
    Scanner s = new Scanner(System.in);

    LocalDate validDate, expDate, date;
    String effDate, delPolicy;
    public static Vehicle vehicle = new Vehicle();
    public static PolicyHolder policyHolder = new PolicyHolder();
    double premium = Vehicle.premium;
    static int policyNo;
    static String validAccNo;

    public int policyNoGenerator() { // method that generates a 6 digit policy number using random function.
        Random random = new Random();
        Integer policyNoGen = random.nextInt(999999);
        policyNo = policyNoGen;
        return policyNoGen;
    }

    public void effectiveDate() { // method that validates if the input of user is in correct format for effective
                                  // date.

        int repeatInput = 0;

        while (!(repeatInput == 3)) {
            System.out.print("\nEnter effective date [YYYY-MM-DD]: ");
            effDate = s.next();

            if (dateIsValid(effDate) == 3 || dateIsValid(effDate) == 1) { // validates if the date is present or future.
                repeatInput = 3;

            } else {

                System.out.println("\nNOTICE: Invalid date. Please try again!\n");
            }
        }

    }

    public String getAccountNo() { // method that gets the value of account number

        String accountNum = validAccNo;
        return accountNum;
    }

    public void expirationDate() { // method that validates if the input of user is in correct format for
                                   // expiration date.

        date = LocalDate.parse(effDate);
        expDate = date.plusMonths(6);
        System.out.println();

        System.out.println("\nThis policy will expire on: " + expDate + "\n");
    }

    public int dateIsValid(String inputDate) { // method that validates if the input of user is in correct format.

        int result = 0;
        if (inputDate.equals("")) {
            result = 0;
        } else {
            try {
                LocalDate validDate = LocalDate.parse(inputDate);
                LocalDate LocaldateNow = LocalDate.now();

                if (validDate.compareTo(LocaldateNow) == 0) { // to compare present
                    this.validDate = validDate;
                    result = 1;
                } else if (validDate.compareTo(LocaldateNow) < 0) { // to compare to past
                    this.validDate = validDate;
                    result = 2;
                } else if (validDate.compareTo(LocaldateNow) > 0) { // to compare to future
                    this.validDate = validDate;
                    result = 3;
                }
            } catch (Exception e) {
                result = 0;
            }
        }
        return result;
    }

    public String compareAccNo() { // method that checks if the account number inputted by the user is exsting in
                                   // database.

        boolean repeatInput = false;
        try {

            Connection con = DriverManager.getConnection( // connects to the mysqldatabase
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            do {

                System.out.println("\n+=========================================+");
                System.out.print("Enter account number: ");
                String accountNo = s.next();
                System.out.println("\n+==========================================");

                String accountNoQuery = "select account_no from customer_account_temp where account_no = '" + accountNo
                        + "'";

                PreparedStatement stmt = con.prepareStatement(accountNoQuery); // praparing query accountNoQuery
                ResultSet rs2 = stmt.executeQuery();

                if (rs2.next()) {
                    validAccNo = accountNo;
                    repeatInput = true;

                } else {
                    System.out.println("Account number is not recognized. Please try again!");
                    repeatInput = false;

                }
                // break;
            } while (repeatInput == false);

        } catch (Exception e) {

        }
        return validAccNo;
    }

    public void savePolicyToDB() { // method that saves the policy to DB.

        String polHolder = PolicyHolder.firstName + " " + PolicyHolder.lastName;
        premium = Vehicle.premium;

        try {

            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            PreparedStatement preparedStmt = con.prepareStatement(
                    "INSERT into policy_pas_temp (effective_date, expiration_date, account_no, policy_no, total_policy_premium, policy_holder) VALUES ('"
                            + effDate + "', '" + expDate + "' , '" + validAccNo + "' , '" + policyNo + "' , '" + premium
                            + "' , '" + polHolder + "' )");
            preparedStmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePolFromDB() { // method that deletes policy from DB

        boolean repeatInput = false;
        try {

            Connection con = DriverManager.getConnection( // connects to the mysqldatabase
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            do {

                System.out.println("\n+=========================================+");
                System.out.print("Enter policy number: ");
                delPolicy = s.next();
                System.out.println("\n+==========================================");

                String delPolicyQuery = "select expiration_date from policy_pas_temp where policy_no = '" + delPolicy
                        + "'";

                PreparedStatement stmt = con.prepareStatement(delPolicyQuery);
                ResultSet rs2 = stmt
                        .executeQuery(delPolicyQuery);

                if (rs2.next()) {
                    String expDateOfPol = rs2.getString("expiration_date");

                    System.out.printf("\nThis policy will expire on: " + expDateOfPol + "\n");
                    repeatInput = true;
                    System.out.println("[1] Change expiration date");
                    System.out.println("[2] Delete policy");
                    String choice = s.next();

                    if (choice.equals("1")) {

                        adjustedExpDate();

                    } else if (choice.equals("2")) {

                        String deletePolicy = "DELETE FROM policy_pas_temp where policy_no = '" + delPolicy + "'";
                        PreparedStatement preparedStmt = con.prepareStatement(deletePolicy);
                        preparedStmt.executeUpdate();

                        System.out.println("\nPolicy " + delPolicy + " is deleted succcessfully! ");

                    } else {
                        repeatInput = false;
                        System.out.println("Invalid input. Please try again!");
                    }

                } else {
                    System.out.println("Policy number is not recognized. Please try again!");
                    repeatInput = false;

                }

            } while (repeatInput == false);

        } catch (Exception e) {
        }
    }

    public void adjustedExpDate() { // method that updates the expiration date of a policy depending on the choice
                                    // of user.

        boolean repeatInput = false;

        while (repeatInput == false) {

            System.out.print("Target expiration date [YYYY-MM-DD]: ");
            String adjustedExpDate = s.next();

            if (dateIsValid(adjustedExpDate) == 3) { // validates if the date inputed is future date
                try {
                    Connection con = DriverManager.getConnection( // connects to the mysql database
                            "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");
                    String updateEffectDate = "UPDATE policy_pas_temp SET expiration_date = '" + adjustedExpDate + "' "
                            + "WHERE policy_no = '" + delPolicy + "';";

                    PreparedStatement preparedStmt = con.prepareStatement(updateEffectDate);
                    preparedStmt.executeUpdate();
                    System.out.println("\n Expiration date changed successfully!\n");
                    repeatInput = true;

                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                repeatInput = false;
                System.out.println("\nNOTICE: Invalid date. Please try again!\n");
            }
        }
    }

    public void searchPolicy() { // method that searches a specific policy
        System.out.println("----------------------------------------------------");
        System.out.println("================== SEARCH A POLICY =================");
        System.out.println("----------------------------------------------------");
        System.out.print("Enter Policy number: ");
        String searchPolicyNo = s.nextLine();

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String searchPolNo = "select * from policy_pas_temp where policy_no = '" + searchPolicyNo + "'";
            PreparedStatement preparedStmt = con.prepareStatement(searchPolNo);

            ResultSet rs2 = preparedStmt.executeQuery(searchPolNo);

            while (rs2.next()) {
                System.out.println(
                        "-------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("|%s %20s %24s %24s %24s %24s|\n", "Policy No.", "Account No.", "Effective Date",
                        "Expiration Date", "Policy Holders", "Total Premium");
                System.out.printf("|%s %20s %24s %24s %24s %28s|\n", rs2.getString("policy_no"),
                        rs2.getString("account_no"), rs2.getString("effective_date"),
                        rs2.getString("expiration_date"),
                        rs2.getString("policy_holder"), rs2.getString("total_policy_premium"));

                System.out.println(
                        "-------------------------------------------------------------------------------------------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void displayPolicy() { // method that displays a specific policy
        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String displayPolicy = "select * from policy_pas_temp where policy_no = '" + policyNo + "'";
            PreparedStatement preparedStmt = con.prepareStatement(displayPolicy);

            ResultSet rs2 = preparedStmt.executeQuery(displayPolicy);

            while (rs2.next()) {
                System.out.println(
                        "------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\t\tNEW POLICY CREATED!");
                System.out.printf("|%131s|\n", " ");
                System.out.printf("|%s %20s %24s %24s %24s %24s|\n", "Policy No.", "Account No.", "Effective Date",
                        "Expiration Date", "Policy Holders", "Total Premium");
                System.out.printf("|%s %20s %24s %24s %24s %28s|\n", rs2.getString("policy_no"),
                        rs2.getString("account_no"), rs2.getString("effective_date"),
                        rs2.getString("expiration_date"),
                        rs2.getString("policy_holder"), rs2.getString("total_policy_premium"));

                System.out.println(
                        "-------------------------------------------------------------------------------------------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}