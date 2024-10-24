/**
 * Represents locations associated with healthcare providers.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;
public enum Location {

    BRIDGEWATER("Somerset", "08807"),
    EDISON("Middlesex", "08817"),
    CLARK("Union", "07066"),
    PRINCETON("Mercer", "08542"),
    PISCATAWAY("Middlesex", "08854"),
    MORRISTOWN("Morris", "07960");

    private final String county;
    private final String zip;


    /**
     * Constructs a Location enum instance.
     * @param county The county of the location
     * @param zip    The zip code of the location
     */
    private Location(String county, String zip){
        this.county = county;
        this.zip = zip;
    }

    /**
     * Returns the county of this location.
     * @return The county
     */
    public String getCounty() {
        return county;
    }

    /**
     * Returns the zip code of this location.
     * @return The zip code
     */
    public String getZip(){
        return zip;
    }

    /**
     * Returns a string representation of the location.
     * @return A formatted string describing the location
     */
    @Override
    public String toString(){
        return this.name() +", "+ this.county + " " + this.zip;
    }
}