/**
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;

/**
 * The Radiology enum defines the types of available imaging services.
 */
public enum Radiology {
    /**type of imaging appointment*/
    CATSCAN,
    /**type of imaging appointment*/
    ULTRASOUND,
    /**type of imaging appointment*/
    XRAY;

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