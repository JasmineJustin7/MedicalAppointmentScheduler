/**
 * Represents a patient in the healthcare system, extending the Person class.
 * Each patient has a profile and a record of their visits.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

/**subclass of person that holds patient information for appointments and medical record*/
public class Patient extends Person {
    /**stores a list of visits associated with one patient*/
    private Visit visit;

    /**
     * Constructs a Patient instance with the specified profile and visit record.
     * @param profile The profile of the patient
     * @param visit   The visit record of the patient
     */
    public Patient(Profile profile, Visit visit) {
        super(profile);
        this.visit = visit;
    }

    /**
     * Getter that returns the visit record of the patient.
     * @return The patient's visit record
     */
    public Profile getProfile(){
        return super.profile;
    }

    /**return specific appointment in linked list
     * @return appointment in linked list*/
    public Visit getVisit() {
        return this.visit;
    }

    /**
     * Sets a new visit record for the patient.
     * @param newVisit The new visit record to set
     */
    public void setVisit(Visit newVisit) {
        this.visit = newVisit;
    }

    /**
     * Returns a string representation of the patient, including their profile and visit details.
     * @return A string describing the patient
     */
    @Override
    public String toString() {
        return super.toString() + " " + visit.toString();
    }

}
