/**
 * Represents an abstract healthcare provider with a location.
 * Extends Person and requires subclasses to implement a rating method.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

/**abstract class that holds information about a given provider including location and rate*/
public abstract class Provider extends Person {
    /**given provider's location*/
    private Location location;

    /**
     * Constructs a Provider with the specified profile and location.
     * @param profile The profile of the provider
     * @param location The location of the provider
     */
    public Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;

    }

    /**
     * Returns the location of the provider.
     * @return The provider's location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Abstract method for subclasses to define their rating logic.
     * @return The rating of the provider
     */
    public abstract int rate();

    /**
     * Returns a string representation of the provider, including their profile and location.
     * @return A string describing the provider
     */
    @Override
    public String toString() {
        return this.getProfile().toString() + ", " + this.location.toString();
    }

    /**
     * Checks if this provider is equal to another object.
     * @param o The object to compare with
     * @return true if this provider is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Provider other = (Provider) o;
        return this.getProfile().equals(other.getProfile()) &&
                this.location.equals(other.location);
    }

}