/**
 * Enum representing different medical specialties and their associated charges.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */

package classes;
/**enum class that holds the different specialties a provider can specialize in*/
public enum Specialty {

    /**a type of specialty with a fixed rate of 250*/
    FAMILY(250),
    /**a type of specialty with a fixed rate of 300*/
    ALLERGIST(300),
    /**a type of specialty with a fixed rate of 350*/
    PEDIATRICIAN(350);

    /**charge denotes the rate of each specialty*/
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
