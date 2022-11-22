import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CustomerAccount {
    // Policy policy = new Policy();

    Scanner s = new Scanner(System.in);
    String firstName, lastName, address;

    public void newCustomerAccount() { // method that prompts the user to enter customer details for creating account.

        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("==================CREATE NEW ACCOUNT=================");
        System.out.println("----------------------------------------------------");
        System.out.print("First Name: ");
        String firstName = s.nextLine();
        System.out.print("Last Name: ");
        String lastName = s.nextLine();
        System.out.print("Address: ");
        String address = s.nextLine();

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String customerAccount = "INSERT into customer_account_temp (first_name, last_name , address) VALUES ('"
                    + firstName + "', '" + lastName + "', '" + address + "')"; // saves all the user inputs in data
                                                                               // base.
            PreparedStatement preparedStmt = con.prepareStatement(customerAccount);
            preparedStmt.executeUpdate();

            ResultSet rs2 = preparedStmt
                    .executeQuery("select account_no from customer_account_temp order by account_no desc limit 1");
            while (rs2.next()) {
                System.out.println();
                System.out.println("+-----------------------------------------------------+");
                System.out.println("|                 NEW ACCOUNT CREATED!                |");
                System.out.printf("|%s %31s| \n", "Your account number: ", rs2.getString("account_no"));
                System.out.printf("|%s %46s| \n", "Name: ", firstName + " " + lastName);
                System.out.printf("|%s %43s|\n", "Address: ", address);
                System.out.println("+-----------------------------------------------------+\n");

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void searchCustomerAccount() { // method that search a specific customer account.

        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("===========SEARCH FOR A CUSTOMER ACCOUNT============");
        System.out.println("----------------------------------------------------");
        System.out.print("Enter first name: ");
        String Fname = s.nextLine();
        System.out.print("Enter last name: ");
        String LName = s.nextLine();

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            String searchName = "select account_no from customer_account_temp where first_name = '" + Fname
                    + "' AND  last_name = '" + LName + "'";
            PreparedStatement preparedStmt = con.prepareStatement(searchName);

            ResultSet rs2 = preparedStmt.executeQuery(searchName);
            if (rs2.next()) {

                String accountNo = rs2.getString("account_no");

                String datum = "select * from policy_pas_temp where account_no = '" + accountNo + "'";
                ResultSet rs3 = preparedStmt.executeQuery(datum);

                System.out.println("\n| Account #  \t\t Policy Owned \t\tPolicy Holders|");

                while (rs3.next()) {

                    System.out.printf("|%s %31s %24s| \n", rs3.getString("account_no"), rs3.getString("policy_no"),
                            rs3.getString("policy_holder"));
                }

            } else {
                System.out.println("\nNo account found under this name. Sorry!");
            }
        } catch (Exception e) {
            System.out.println("No account found under this name. Sorry!");
            System.out.println(e);
        }

    }
}
