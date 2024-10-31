/**
 * This class provides methods to sort based on different attributes.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package util;
import classes.Appointment;
import classes.*;
import classes.Provider;
import java.util.Comparator;


/**class used to sort a generic list in different ways using different keys*/
public class Sort {
    /**
     * Sorts a list of appointments based on the specified key.
     * @param list the list of appointments to sort
     * @param key the sorting criteria
     * @throws IllegalArgumentException if the sorting key is invalid
     */
    public static void appointment(List<Appointment> list, char key){
        switch (key) {
            case'd': //sort by date
                bubbleSort(list, (a, b) -> a.getDate().compareTo(b.getDate()));
                break;
            case 't':
                bubbleSort(list, (a, b) -> a.getTimeslot().compareTo(b.getTimeslot()));
                break;
            case 'r':
                bubbleSort(list, (a, b) -> a.getProvider().compareTo(b.getProvider()));
                break;
            case 'p':
                bubbleSort(list, (a, b) -> a.getPatient().compareTo(b.getPatient()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting key " + key);
        }
    }

    /**
     * Sorts a list of providers based on their profiles.
     * @param list the list of providers to sort
     */
    public static void provider(List<Provider> list) {
        bubbleSort(list, (p1, p2) -> p1.getProfile().compareTo(p2.getProfile()));

    }

    /**
     * Sorts a list of imaging appointments by county, date, and time.
     * @param list the list of imaging appointments to sort
     */
    public static void sortedByDate(List<Appointment> list) {
        // Sort by date, time, and provider's name in one method
        bubbleSort(list, (a, b) -> {
            // First compare by appointment date
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }

            // If dates are the same, compare by timeslot
            int timeComparison = a.getTimeslot().compareTo(b.getTimeslot());
            if (timeComparison != 0) {
                return timeComparison;
            }

            // If date and timeslot are the same, compare by provider's name
            return a.getProvider().getProfile().compareTo(b.getProvider().getProfile());
        });
    }

    /**
     * Sorts a list of appointments by patient profile, appointment date, and timeslot.
     * @param list the list of appointments to be sorted.
     */
    public static void sortByPatient(List<Appointment> list) {
        bubbleSort(list, (a, b) -> {
            // First compare by patient profile (first name, last name, dob)
            int profileComparison = a.getPatient().getProfile().compareTo(b.getPatient().getProfile());
            if (profileComparison != 0) {
                return profileComparison;
            }

            // If patient profiles are the same, compare by appointment date
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }

            // If dates are the same, compare by timeslot
            return a.getTimeslot().compareTo(b.getTimeslot());
        });
    }

    /**
     * Sorts a list of appointments by provider's county, appointment date, and timeslot.
     * @param list the list of appointments to be sorted.
     */
    public static void sortByCounty(List<Appointment> list) {
        bubbleSort(list, (a, b) -> {
            // First, compare by the provider's location (county)
            int countyComparison = a.getProvider().getLocation().getCounty().compareTo(b.getProvider().getLocation().getCounty());
            if (countyComparison != 0) {
                return countyComparison;
            }

            // If counties are the same, compare by appointment date
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }

            // If dates are the same, compare by timeslot
            return a.getTimeslot().compareTo(b.getTimeslot());
        });
    }

    /**
     * Sorts a list of imaging appointments by provider's county, appointment date, and timeslot.
     * @param list the list of imaging appointments to be sorted.
     */
    public static void imagingAppointment(List<Imaging> list) {
        bubbleSort(list, (a, b) -> {
            // Get provider locations (ensure they're not null)
            Location locationA = a.getProvider().getLocation();
            Location locationB = b.getProvider().getLocation();

            // Compare counties from the provider's location
            int countyComparison = locationA.getCounty().compareTo(locationB.getCounty());
            if (countyComparison != 0) {
                return countyComparison;
            }

            // If counties are the same, compare by appointment date
            Date dateA = a.getDate();
            Date dateB = b.getDate();
            int dateComparison = dateA.compareTo(dateB);
            if (dateComparison != 0) {
                return dateComparison;
            }

            // If date is the same, compare by timeslot
            Timeslot timeSlotA = a.getTimeslot();
            Timeslot timeSlotB = b.getTimeslot();
            return timeSlotA.compareTo(timeSlotB);
        });
    }

    /**
     * Sorts a list of office appointments by provider's county, appointment date, and timeslot.
     * @param list the list of office appointments to be sorted.
     */
    public static void officeAppointment(List<Appointment> list) {
        bubbleSort(list, (a, b) -> {
            // Compare provider's location (assume provider is a Doctor in office appointments)
            int countyComparison = a.getProvider().getLocation().getCounty().compareTo(b.getProvider().getLocation().getCounty());
            if (countyComparison != 0) {
                return countyComparison;
            }

            // Compare by date if counties are the same
            int dateComparison = a.getDate().compareTo(b.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }

            // Compare by timeslot if date is the same
            return a.getTimeslot().compareTo(b.getTimeslot());
        });
    }

    /**
     * Sorts a list of patients by their profile (first name, last name, and DOB).
     * @param list the list of patients to sort
     */
    public static void patient(List<Person> list) {
        bubbleSort(list, (a, b) -> {
            // Compare by last name first
            int lastNameComparison = a.getProfile().getLastName().compareTo(b.getProfile().getLastName());
            if (lastNameComparison != 0) {
                return lastNameComparison;
            }

            // If last names are the same, compare by first name
            int firstNameComparison = a.getProfile().getFirstName().compareTo(b.getProfile().getFirstName());
            if (firstNameComparison != 0) {
                return firstNameComparison;
            }

            // If first names are also the same, compare by date of birth
            return a.getProfile().getDob().compareTo(b.getProfile().getDob());
        });
    }

    /**
     * A private helper method that performs bubble sort on the provided list.
     * @param list the list to be sorted
     * @param comparator the comparator used to compare list elements
     * @param <E> the type of elements in the list
     */
    private static <E> void bubbleSort(List<E> list, Comparator<E> comparator) {
        int n = list.size();
        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n -1 - i; j++) {
                if(comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    E temp = list.get(j);
                    list.set(j, list.get(j +1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}