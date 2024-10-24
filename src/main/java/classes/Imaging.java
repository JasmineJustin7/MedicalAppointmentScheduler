/**
 * Represents an Imaging appointment, which extends the Appointment class.
 * This class encapsulates the imaging room assigned for the appointment.
 * @author Jasmine Justin
 * @author Jimena Reyes
 */
package classes;

import util.Date;

public class Imaging extends Appointment{
    private Radiology room;

    /**
     * Constructs an Imaging appointment with the specified date, timeslot, patient, provider, and room.
     * @param date      The date of the appointment
     * @param timeslot  The timeslot for the appointment
     * @param patient   The patient for the appointment
     * @param provider  The provider for the appointment
     * @param room      The radiology room assigned for the appointment
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room  = room;
    }

    /**
     * Getter that returns the radiology room assigned for the appointment.
     * @return The room assigned for the imaging appointment
     */
    public Radiology getRoom() {
        return room;
    }

    public Provider getProvider() {
        if (provider instanceof Provider) {
            return (Provider) provider;
        } else {
            throw new IllegalStateException("Provider is not a valid instance of Provider class.");
        }
    }


    /**
     * Returns a string representation of the Imaging appointment, including the imaging room details.
     * @return A string describing the imaging appointment and its room
     */
    @Override
    public String toString() {
        return super.toString() + "[" + getRoom()+ "]";
    }

    /**
     * Compares this Imaging appointment to another object for equality.
     * @param o The object to compare with
     * @return true if the specified object is equal to this Imaging appointment; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Imaging other = (Imaging) o;
        return super.equals(other) && this.room.equals(other.room);
    }
}
