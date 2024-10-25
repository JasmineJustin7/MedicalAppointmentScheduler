/**
 * Class extends the Provider class, including rate per visit.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;
import util.Date;

public class Technician extends Provider {
    private int ratePerVisit;

    /**
     * Constructor to initialize a Technician with a Profile, Location, and rate per visit
     *
     * @param profile      The profile of the technician
     * @param location     The practice location of the technician
     * @param ratePerVisit The rate charged by the technician per visit
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Returns the rate per visit for the technician.
     *
     * @return The rate charged per visit
     */
    public int getRatePerVisit() {
        return ratePerVisit;
    }

    /**
     * Sets a new rate per visit for the technician
     *
     * @param ratePerVisit The new rate to be charged per visit
     * @throws IllegalArgumentException if ratePerVisit is negative
     */
    public void setRatePerVisit(int ratePerVisit) {
        if (ratePerVisit < 0) {
            throw new IllegalArgumentException
                    ("Rate per visit cannot be negative.");
        }
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Returns the technician's rate per visit.
     * Overrides the abstract method rate() in Provider class.
     *
     * @return The rate per visit as defined by the technician
     */
    @Override
    public int rate() {
        return ratePerVisit;
    }

    /**
     * Returns a string representation of the Technician object.
     *
     * @return A string describing the technician, including rate and location
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" [rate: $%.2f", (double) ratePerVisit) + "]";
    }

    /**
     * Compares this Technician object with the specific object for equality.
     *
     * @param o the object to compare with this Technician
     * @return true if the object is equal to this Technician, otherwise false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Technician other) {
            return ratePerVisit == other.ratePerVisit && super.equals(other);
        }
        return false;
    }

    /**
     * Returns a hash code value for Technician.
     * Hash code based on hashcode of super class and Technicians rate per visit.
     *
     * @return a hash code value for the Technician, with the rate per visit
     */
    /*
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + ratePerVisit;
    }

    /**
     * Compares this Technician object with the specified Person for order.
     *
     * @param other the Person to be compared
     * @return a negative integer, zero, or positive integer
     */
    @Override
    public int compareTo(Person other) {
        return super.compareTo(other);
    }

    //Test Code
    public static void main(String[] args) {
        // Create mock objects for testing
        Profile profile = new Profile("John", "Doe", new Date());
        Location location = Location.BRIDGEWATER; // Using enum constant

        // Test case for Technician creation
        Technician technician = new Technician(profile, location, 100);
        System.out.println(technician);  // Test toString()

        // Test case for rate per visit
        assert technician.getRatePerVisit() == 100 : "Rate per visit should be 100";

        // Test case for setting a new rate
        technician.setRatePerVisit(150);
        assert technician.getRatePerVisit() == 150 : "Rate per visit should be 150";

        // Test case for negative rate
        try {
            technician.setRatePerVisit(-50);
            System.out.println("Expected IllegalArgumentException for negative rate not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected IllegalArgumentException for negative rate.");
        }

        // Test case for equals() method
        Technician technician2 = new Technician(profile, location, 150);
        assert technician.equals(technician2) : "Technicians should be equal";

        // Test case for hashCode()
        assert technician.hashCode() == technician2.hashCode() : "Hash codes should be equal for equal technicians";

        System.out.println("All tests passed!");
    }
}
