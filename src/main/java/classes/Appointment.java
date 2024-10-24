/**
 * This class implements the Comparable interface to allow sorting of appointments.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

import util.Date;

public class Appointment implements Comparable<Appointment> {

    protected Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Constructs an Appointment with the specified date, timeslot, patient, and provider.
     * @param date      the date of the appointment
     * @param timeslot  the timeslot of the appointment
     * @param patient   the patient associated with the appointment
     * @param provider  the provider associated with the appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider){
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Returns the date of the appointment.
     * @return the date of the appointment
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the timeslot of the appointment.
     * @return the timeslot of the appointment
     */
    public Timeslot getTimeslot(){
        return timeslot;
    }

    /**
     * Returns the patient associated with the appointment.
     * @return the patient associated with the appointment
     */
    public Person getPatient(){
        return patient;
    }

    /**
     * Returns the provider associated with the appointment.
     * @return the provider associated with the appointment
     */
    public Provider getProvider() {
        return (Provider) provider;
    }


    /**
     * Sets a new timeslot for the appointment.
     * @param newTimeslot the new timeslot to set for the appointment
     */
    public void setTimeslot(Timeslot newTimeslot) {
        this.timeslot = newTimeslot;
    }


    /**
     * Checks if this appointment is equal to another object.
     * @param o the object to compare with this appointment
     * @return true if the appointments are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Appointment other = (Appointment) o;

        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient) &&
                this.provider.equals(other.provider);
    }

    /**
     * Compares this appointment with another appointment for order.
     * @param o the appointment to compare with
     * @return a negative integer, zero, or a positive integer as this appointment is less than,
     *         equal to, or greater than the specified appointment
     */
    @Override
    public int compareTo(Appointment o) {
        // First compare by date
        int dateComparison = this.date.compareTo(o.date);
        if (dateComparison != 0)
            return dateComparison;
        //If dates are the same, compare by the timeslot
        return this.timeslot.compareTo(o.timeslot);
    }

    /**
     * Returns a string representation of the appointment.
     * @return a string representation of the appointment
     */
    @Override
    public String toString(){
        return this.date.toString() + " " + this.timeslot.toString() + " " +
                this.patient.toString() + " [" +
                this.provider.toString() + "]";
    }
}
