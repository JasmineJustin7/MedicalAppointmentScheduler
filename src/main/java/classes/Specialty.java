/**
 * Enum representing different medical specialties and their associated charges.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */

package classes;
public enum Specialty {

    FAMILY(250),
    ALLERGIST(300),
    PEDIATRICIAN(350);

    private final int charge;

    /**
     * Constructs a Specialty with the specified charge.
     * @param charge The charge associated with the specialty
     */
    private Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Returns the charge associated with the specialty.
     * @return The charge as a double
     */
    public double getCharge() {
        return charge;
    }

    /**
     * Returns the name of the specialty as a string.
     * @return The name of the specialty
     */
    @Override
    public String toString() {
        return this.name();
    }
}
