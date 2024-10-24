/**
 * This class supports comparison and provides static methods for creating predefined time slots.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;
import java.sql.Time;

public class Timeslot implements Comparable<Timeslot>{
    private int hour;
    private int minute;

    /**
     * Constructs a Timeslot with the specified hour and minute.
     * @param hour   The hour of the timeslot (0-23)
     * @param minute The minute of the timeslot (0-59)
     */
    private Timeslot(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Getter that returns the hour of the timeslot.
     * @return The hour as an integer
     */
    public int getHour() {
        return hour;
    }

    /**
     * Getter that returns the minute of the timeslot.
     * @return The minute as an integer
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Compares this Timeslot to the specified object for equality.
     * @param o the object to compare this Timeslot against
     * @return true if the given object is equal to this Timeslot, false otherwise
     */
    @Override
    public boolean equals(Object o ) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Timeslot other = (Timeslot) o;
        return this.hour == other.hour && this.minute == other.minute;
    }

    /**
     * Returns a string representation of the Timeslot in 12-hour format.
     * @return a string representation of the Timeslot
     */
    @Override
    public String toString() {
        int displayHour = hour % 12;
        if (displayHour == 0) {
            displayHour = 12;
        }
        String period = (hour < 12) ? "AM" : "PM";
        return String.format("%d:%02d %s", displayHour, minute, period);
    }

    /**
     * Compares this Timeslot with another Timeslot.
     * @param o the Timeslot to be compared
     * @return a negative integer, zero, or a positive integer based on the comparison
     */
    @Override
    public int compareTo(Timeslot o) {
        if (this.hour < o.hour){
            return -1;
        } else if (this.hour > o.hour) {
            return 1;
        }else {
            return Integer.compare(this.minute, o.minute);
        }
    }

    /**
     * Creates a Timeslot representing 9:00 AM.
     * @return a Timeslot object for 9:00 AM
     */
    public static Timeslot slot1() {
        return new Timeslot(9, 0);
    }

    /**
     * Creates a Timeslot representing 10:45 AM.
     * @return a Timeslot object for 10:45 AM
     */
    public static Timeslot slot2() {
        return new Timeslot(9, 30);
    }

    /**
     * Creates a Timeslot representing 11:15 AM.
     * @return a Timeslot object for 11:15 AM
     */
    public static Timeslot slot3() {
        return new Timeslot(10, 0);
    }

    /**
     * Creates a Timeslot representing 1:30 PM.
     * @return a Timeslot object for 1:30 PM
     */
    public static Timeslot slot4() {
        return new Timeslot(10, 30);
    }

    /**
     * Creates a Timeslot representing 3:00 PM.
     * @return a Timeslot object for 3:00 PM
     */
    public static Timeslot slot5() {
        return new Timeslot(11, 0);
    }

    /**
     * Creates a Timeslot representing 4:15 PM.
     * @return a Timeslot object for 4:15 PM
     */
    public static Timeslot slot6() {
        return new Timeslot(11, 30);
    }

    //Afternoon slots
    public static Timeslot slot7() {
        return new Timeslot(14, 0);
    }
    public static Timeslot slot8() {
        return new Timeslot(14, 30);
    }
    public static Timeslot slot9() {
        return new Timeslot(15, 0);
    }
    public static Timeslot slot10() {
        return new Timeslot(15, 30);
    }
    public static Timeslot slot11() {
        return new Timeslot(16, 0);
    }
    public static Timeslot slot12() {
        return new Timeslot(16, 30);
    }
}
