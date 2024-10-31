/**
 * Represents a person in the healthcare system with a profile.
 * This class implements Comparable to allow comparison based on profile.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

/**Person class is the parent class of Patient and Provider and provides the profiles of both*/
public class Person  implements Comparable<Person> {

    /**profile of a given person; first name, last name, date of birth*/
    protected Profile profile;

    /**
     * Constructs a Person instance with the specified profile.
     *
     * @param profile The profile of the person
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Returns the profile of the person.
     *
     * @return The person's profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Compares this person to another person based on their profiles.
     *
     * @param o The other person to compare with
     * @return A negative integer, zero, or a positive integer
     */
    @Override
    public int compareTo(Person o) {

        return this.profile.compareTo(o.profile);
    }

    /**
     * Checks if this person is equal to another object.
     *
     * @param o The object to compare with
     * @return true if the profiles are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Person other = (Person) o;
        return this.profile.equals(other.profile);
    }

    /**
     * Returns a string representation of the person, displaying their profile.
     *
     * @return A string describing the person
     */
    @Override
    public String toString() {
        return this.profile.toString();
    }

}
