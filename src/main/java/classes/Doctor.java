/**
 * This class encapsulates the doctor's specialty and their National Provider
 * @author Jasmine Justin
 * @author Jimena Reyes
 */
package classes;

/**Class to hold information about Doctors, a subclass of Provider
 * @author Jimena Reyes
 * @author Jasmine Justin*/
public class Doctor extends Provider {
    /**encapsulate the rate per visit based on speciality*/
    private Specialty specialty;
    /**National Provider Identification unique to the doctor*/
    private String npl;

    /**
     * Constructs a Doctor with the specified profile, location, specialty, and NPI.
     * @param profile   The profile of the doctor
     * @param location  The practice location of the doctor
     * @param specialty The specialty of the doctor
     * @param npl       The National Provider Identification number for the doctor
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npl) {
        super(profile, location);
        this.specialty = specialty;
        this.npl = npl;
    }

    /**
     * Returns the specialty of the doctor.
     * @return The specialty of the doctor
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Returns the National Provider Identification (NPI) of the doctor.
     * @return The NPI of the doctor
     */
    public String getNpl() {
        return npl;
    }

    /**
     * Calculates the rate for the doctor based on their specialty.
     * @return The charge associated with the doctor's specialty
     */
    @Override
    public int rate() {
        return (int)specialty.getCharge();
    }

    /**
     * Compares this doctor to another object for equality.
     * @param o The object to compare with
     * @return true if the specified object is equal to this doctor; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Doctor other = (Doctor) o;
        return super.equals(other) &&
                this.specialty.equals(other.specialty) &&
                this.npl.equals(other.npl);

    }

    /**
     * Returns a string representation of the doctor, including their profile, specialty, and NPI.
     * @return A string describing the doctor
     */
    @Override
    public String toString() {
        return super.toString() + " [" + this.specialty.toString() + " #" +
                npl + "]";
    }
}