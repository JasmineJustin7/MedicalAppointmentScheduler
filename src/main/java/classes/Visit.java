
package classes;
/**
 * Represents a visit in a linked list of appointments.
 * Each visit contains an appointment and a reference to the next visit in the list.
 * @author Jasmine Justin
 * @author Jimena Reyes
 */
public class Visit {
    /**appointment holds patient information and associated provider and timeslot information*/
    private Appointment appointment;
    /**reference to next appointment in linked list*/
    private Visit next;

    /**
     * Constructor to create a visit node with an appointment
     * @param appointment the appointment associated with this visit
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    /**
     * Getter for the appointment associated with this visit
     * @return the appointment associated with this visit
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Sets the appointment for this visit
     * @param appointment the appointment to set
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Getter for the next visit node in the list
     * @return the next visit node
     */
    public Visit getNext() {

        return next;
    }

    /**
     * Setter for the next visit node in the list
     * @param next the next visit node to set
     */
    public void setNext(Visit next) {
        this.next = next;
    }

    /**
     * Returns a string of the visit, including appointment details
     * @return a string representation of the visit
     */
    @Override
    public String toString() {
        return "Visit: " + appointment.toString();
    }
}