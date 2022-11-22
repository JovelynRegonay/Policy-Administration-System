import java.time.LocalDate;

public class RatingEngine extends Vehicle {

    double calculatedPremium, priceFactor;
    int dlx, ageOfVehicle;
    int yearNow = LocalDate.now().getYear();
    PolicyHolder policyHolder = new PolicyHolder();
    String DLDate = PolicyHolder.DLDateIssuedDB;

    public void premiumComputation() { // method that computes the premium for every vehicle

        DLDate = PolicyHolder.DLDateIssuedDB;
        splitDate(DLDate);

        calculatedPremium = ((doublePrice * priceFactor()) + ((doublePrice / 100) / dlx));
        System.out
                .println("\nCalculated premium of this vehicle = $" + String.format("%.2f", calculatedPremium) + "\n");

    }

    public int splitDate(String input) { // method that splits the drivers license date issued.

        int index1 = input.indexOf('-');
        String DLyear = input.substring(0, index1);
        int intDLYear = Integer.parseInt(DLyear);

        dlx = yearNow - intDLYear;

        if (dlx == 0) {
            dlx = 1;
        }

        return dlx;

    }

    public double priceFactor() { // method that computes the price factor of a vehicle depending on age of
                                  // vehicle

        ageOfVehicle = yearNow - intYear;

        if (ageOfVehicle < 1 || ageOfVehicle == 0) {
            priceFactor = 0.01;
        } else if (ageOfVehicle < 3) {
            priceFactor = 0.008;
        } else if (ageOfVehicle < 5) {
            priceFactor = 0.007;
        } else if (ageOfVehicle < 10) {
            priceFactor = 0.006;
        } else if (ageOfVehicle < 15) {
            priceFactor = 0.004;
        } else if (ageOfVehicle < 20) {
            priceFactor = 0.002;
        } else if (ageOfVehicle < 40) {
            priceFactor = 0.001;
        } else {
            System.out.println("Vehicle is not eligible for this insurance.");
        }

        return priceFactor;
    }

}
