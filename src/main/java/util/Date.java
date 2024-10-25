package util;

import java.text.DecimalFormat;
import java.util.Calendar;

/**Represents a date with methods for validation and comparison.
 * This class implements Comparable<util.Date> to allow comparison of date instances.
 * @author Jasmine Justin
 * @author Jimena Reyes
 */
public class Date implements Comparable<Date> {
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    private int year;
    private int month;
    private int day;

    /**Default constructor that initializes the date to the current date.*/
    public Date() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    /**Parameterized constructor that takes a date string in the format 'yyyy-mm-dd'.
     * @param date A string representation of the date.*/
    /*public Date(String date) {
        String [] values = date.split("-");
        this.year = Integer.parseInt(values[0]);
        this.day = Integer.parseInt(values[2]);
        this.month = Integer.parseInt(values[1]);
    }*/
    /**Parameterized constructor that takes a date string in the format 'yyyy-mm-dd'.
     * @param date A string representation of the date.*/
    public Date(String date) {
        if(date.contains(String.valueOf("/"))){
            String [] values = date.split("/");
            this.year = Integer.parseInt(values[2]);
            this.day = Integer.parseInt(values[1]);
            this.month = Integer.parseInt(values[0]);
        }else{
            String [] values = date.split("-");
            this.year = Integer.parseInt(values[0]);
            this.day = Integer.parseInt(values[2]);
            this.month = Integer.parseInt(values[1]);
        }


    }

    /**Validates if the current date is a valid calendar date.
     * @return true if the date is valid; false otherwise.*/
    public boolean isValid() {
        if(this.month < Calendar.JANUARY + 1 || this.month > Calendar.DECEMBER + 1)
            return false;

        switch(this.month) {
            //Months with 31 days
            case Calendar.JANUARY + 1:
            case Calendar.MARCH + 1:
            case Calendar.MAY + 1:
            case Calendar.JULY + 1:
            case Calendar.AUGUST + 1:
            case Calendar.OCTOBER + 1:
            case Calendar.DECEMBER + 1:
                return this.day >= 1 && this.day <= 31;
            //Months with 30 days
            case Calendar.APRIL + 1:
            case Calendar.JUNE + 1:
            case Calendar.SEPTEMBER + 1:
            case Calendar.NOVEMBER + 1:
                return this.day >= 1 && this.day <= 30;
            case Calendar.FEBRUARY + 1:
                return this.day >= 1&& this.day <=(isLeap(this.year) ? 29 : 28);
            default:
                return false;
        }
    }

    /**
     * Determines if the specified year is a leap year.
     * @param year The year to check.
     * @return true if the year is a leap year; false otherwise.
     */
    private boolean isLeap(int year) {
        if(year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUATERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a util.Date instance representing today's date.
     * @return A util.Date object representing the current date.
     */
    public static Date today(){
        return new Date();
    }

    /**
     * Checks if the current date is not today or is in the future.
     * @return true if the date is after today; false otherwise.
     */
    public boolean isNotTodayOrBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (calendar.after(today)) {
            return true;
        }
        if (calendar.equals(today)) {
            return false;
        }
        return false;
    }


    /**
     * Checks if the current date is today or in the future.
     *
     * @return true if the date is today or after; false otherwise.
     */
    public boolean isTodayOrAfter() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // Set to start of the day
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar givenDate = Calendar.getInstance();
        givenDate.set(year, month - 1, day);

        return !givenDate.before(today);
    }

    /**
     * Checks if the current date falls on a weekend (Saturday or Sunday).
     *
     * @return true if the date is a weekend; false otherwise.
     */
    public boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /**
     * Checks if the current date is within six months from today.
     *
     * @return true if the date is within six months; false otherwise.
     */
    public boolean isWithin6Months() {
        Calendar today = Calendar.getInstance();
        Calendar sixMonthsLater = Calendar.getInstance();
        sixMonthsLater.setTime(today.getTime());
        sixMonthsLater.add(Calendar.MONTH, 6);

        Calendar appointmentDate = Calendar.getInstance();
        appointmentDate.set(year, month - 1, day);

        return appointmentDate.after(today) && appointmentDate.before(sixMonthsLater);
    }


    /**
     * Getter method that returns the year of the date.
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter that returns the month of the date.
     * @return The month.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day of the date.
     * @return The day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Compares this date to another date.
     *
     * @param o The date to compare against.
     * @return A negative integer, zero, or a positive integer as this date
     *         is earlier than, equal to, or later than the specified date.
     */
    @Override
    public int compareTo(Date o) {
        if(this.year < o.year)
            return -1;
        else if(this.year > o.year)
            return 1;
        if(this.month < o.month)
            return -1;
        else if(this.month > o.month)
            return 1;
        if(this.day < o.day)
            return -1;
        else if(this.day > o.day)
            return 1;
        //if days are the same
        return 0;
    }

    /**
     * Returns a string representation of the date in 'mm/dd/yyyy' format.
     *
     * @return A formatted string of the date.
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(month) + "/" + df.format(day) + "/" + year;
    }

    /**
     * Checks if this date is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the dates are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Date other = (Date)o;
        return this.year == other.year &&
                this.month == other.month &&
                this.day == other.day;
    }
}
