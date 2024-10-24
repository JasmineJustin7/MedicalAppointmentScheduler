/**
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

/**
 * The Radiology enum defines the types of available imaging services.
 */
public enum Radiology {
    CATSCAN, ULTRASOUND, XRAY;

    /**
     * Returns a string representation of the Radiology type.
     *
     * @return The name of the radiology service
     */
    @Override
    public String toString() {
        return name();
    }
}