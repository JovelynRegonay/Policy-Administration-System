
/*
 *  PASDriver.java
 *  @author JovelynRegonay
 *  @Description :  a simple Automobile Insurance Policy and Claims Administration system (PAS) will be created to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
                    An insurance company customer can buy one or more Automobile Policies. 
                    Before a customer can buy a policy, an account must be created for the customer. The customer’s account then contains all of the customers policies.
 *  
 *  Created Date : 08/01/2022			   
 *  
 *  @modified by JovelynRegonay
 */
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PASDriver {
    public static Scanner s = new Scanner(System.in);
    static CustomerAccount customerAccount = new CustomerAccount(); // instantiation of object customer account.
    static PolicyHolder policyHolder = new PolicyHolder(); // instantiation of object policy holder.
    static Policy policy = new Policy(); // instantiation of object policy.
    static Vehicle vehicle = new Vehicle(); // instantiation of object vehicle.
    static RatingEngine ratingEngine = new RatingEngine(); // instantiation of object rating engine.
    static Claim claim = new Claim(); // instantiation of object claim.

    public static void main(String[] args) throws Exception {

        try {
            Connection con = DriverManager.getConnection( // connects to the mysql database
                    "jdbc:mysql://localhost:3306/java_database", "root", "Jovganda143121");

            Statement stmt = con.createStatement();

            String claims_sql = "CREATE TABLE if not exists CLAIMS_NOW" +
                   "(claim_no INTEGER not NULL, " +
                   " date_of_accident VARCHAR(30), " + 
                   " address_of_accident VARCHAR(255), " + 
                   " accident_desc VARCHAR(45), " + 
                   " damage_desc VARCHAR(45)," +
                   " cost_of_repair VARCHAR(45))";
                   

            String customer_account_sql = "CREATE TABLE if not exists customer_account_now " +
            "(account_no int(4)" +
            " first_name VARCHAR(1000), " + 
            " last_name VARCHAR(1000), " + 
            " address VARCHAR(1000)) " ;
           
            String policy_holder_sql = "CREATE TABLE if not exists policy_holder_now " +
            "(first_name VARCHAR(1000), " + 
            " last_name VARCHAR(1000), " + 
            " date_of_birth VARCHAR(1000), " +
            " address VARCHAR(1000), " +
            " drivers_license VARCHAR(1000), " +
            " date_of_DL_first_issue VARCHAR(1000)) ";

            String policy_sql = "CREATE TABLE if not exists policy_now " +
            "(policy_no VARCHAR(1000), " + 
            " effective_date VARCHAR(1000), " + 
            " expiration_date VARCHAR(1000), " +
            " account_no VARCHAR(1000), " +
            " policy_holder VARCHAR(1000), " +
            " total_policy_premium VARCHAR(1000)) ";

            String vehicle_sql = "CREATE TABLE if not exists vehicle_now " +
            "(make VARCHAR(1000), " + 
            " model VARCHAR(1000), " + 
            " year VARCHAR(1000), " +
            " type VARCHAR(1000), " +
            " fuel_type VARCHAR(1000), " +
            " purchase_price VARCHAR(1000), " +
            " color VARCHAR(1000), " +
            " premium_charge VARCHAR(1000), " +
            " policy_no VARCHAR(1000), " +
            " account_no VARCHAR(1000)) ";

         stmt.executeUpdate(claims_sql);
         stmt.executeUpdate(customer_account_sql);
         stmt.executeUpdate(policy_holder_sql);
         stmt.executeUpdate(policy_sql);
         stmt.executeUpdate(vehicle_sql);
        }
               
            catch (Exception e) {
                e.printStackTrace();
            }

        menu();
    }

    public static void menu() { // display of menu options.

        int menu = 0;
        do {
            System.out.println();
            System.out.println("+-------------------------------------------------+");
            System.out.println("===POLICY AND CLAIMS ADMINISTRATION SYSTEM (PAS)===");
            System.out.println("+-------------------------------------------------+");
            System.out.println("[1] Create a new Customer Account.");
            System.out.println("[2] Get a policy quote and buy the policy.");
            System.out.println("[3] Cancel a specific policy");
            System.out.println("[4] File an accident claim against a policy.");
            System.out.println("[5] Search for a Customer account ");
            System.out.println("[6] Search for and display a specific policy");
            System.out.println("[7] Search for and display a specific claim");
            System.out.println("[8] Exit the PAS System\n");

            try {
                System.out.print("Choose an option: ");
                menu = s.nextInt();

                switch (menu) {

                    case 1:
                        customerAccount.newCustomerAccount(); // calling the method newCustomerAccount under customer
                                                              // account class
                        break;

                    case 2:
                        policy.compareAccNo(); // calling the method compareAccNo under policy class
                        policy.effectiveDate(); // calling the method effectiveDate under policy class
                        policy.expirationDate(); // calling the method expirationDate under policy class
                        System.out.println("Your Policy # is : " + policy.policyNoGenerator() + "\n"); // displaying
                                                                                                       // policy
                        // number using method
                        // policyNoGenerator
                        // under policy class
                        policyHolder.policyHolderInfo(); // calling the method policyHolderInfo under policyHolder class
                        vehicle.numOfVehicleInput(); // calling the method numOfVehicle under vehicle class

                        System.out.println("\nDo you want to purchase this policy?");
                        System.out.println("Enter 1 if YES, enter any key if NO.");

                        String choice = s.next(); // input of user.

                        if (choice.equals("1")) { // compares input of user to 1.
                            policy.savePolicyToDB(); // calling the method savePolicyToDB under policy class
                            policyHolder.savePolicyHolderToDB(); // calling the method savePolicyHolderToDB under
                                                                 // policyHolder class
                            policy.displayPolicy(); // calling the method displayPolicy under policy class

                        } else {
                            vehicle.deleteVehiclesOnDB(); // calling the method deleteVehiclesOnDB under vehicle class
                            System.out.println("\nTransanction cancelled. Thank you!");
                        }

                        break;

                    case 3:
                        policy.deletePolFromDB(); // calling the method deletePolFromDB under policy class
                        break;

                    case 4:
                        claim.searchPolicyNo(); // calling the method searchPolicyNo under claim class
                        break;

                    case 5:
                        customerAccount.searchCustomerAccount(); // calling the method searchCustomerAccount under
                                                                 // customerAccount class
                        break;

                    case 6:
                        policy.searchPolicy(); // calling the method searchPolicy under policy class
                        break;

                    case 7:
                        claim.searchClaim(); // calling the method searchClaim under claim class
                        break;

                    default:
                        System.out.println("Invalid input. Please try again!");

                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again!");
                s.next();
            }
        } while (menu != 8); // loop that will not end unless the menu is equal to 8.
    }
}