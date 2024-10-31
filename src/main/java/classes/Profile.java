/**
 * Implements Comparable to allow sorting based on last name, first name, and date of birth.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */

package classes;

import util.Date;

/**class to hold first name, last name and date of birth of a given person*/
public class Profile implements Comparable<Profile> {
    /**stores person's first name*/
    private String fname;
    /**stores person's last name*/
    private String lname;
    /**stores person's date of birth*/
    private Date dob;

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth.
     * @param fname The first name of the profile
     * @param lname The last name of the profile
     * @param dob   The date of birth of the profile
     */
    public Profile(String fname, String lname, Date dob){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Returns the first name of the profile.
     * @return The first name
     */
    public String getFirstName() {
        return this.fname;
    }

    /**
     * Returns the last name of the profile.
     * @return The last name
     */
    public  String getLastName() {
        return this.lname;

    }

    /**
     * Returns the date of birth of the profile as a string.
     * @return The date of birth
     */
    public String getDob() {
        return this.dob.toString();
    }

    /**
     * Compares this profile with another profile for order.
     * @param o The other profile to compare to
     * @return A negative integer, zero, or a positive integer
     */
    @Override
    public int compareTo(Profile o) {
        int lastnameComp = this.lname.compareTo(o.lname);
        if(lastnameComp != 0) {
            return lastnameComp;
        }
        int firstnameComp = this.fname.compareTo(o.fname);
        if(firstnameComp != 0)
            return firstnameComp;

        return this.dob.compareTo(o.dob);
    }

    /**
     * Checks if this profile is equal to another object.
     * @param o The object to compare with
     * @return true if this profile is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Profile other = (Profile) o;
        return this.fname.equalsIgnoreCase(other.fname) &&
                this.lname.equalsIgnoreCase(other.lname) &&
                this.dob.equals(other.dob);
    }


    /**
     * Returns a string representation of the profile, including first name, last name, and date of birth.
     * @return A string describing the profile
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob;
    }

    /**
     * Returns the current Profile instance.
     * @return The current Profile instance
     */
    public Profile getProfile() {
        return this;
    }
}
