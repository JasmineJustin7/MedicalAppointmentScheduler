/**
 * Represents locations associated with healthcare providers.
 * @author: Jasmine Justin
 * @author: Jimena Reyes
 */
package classes;
/**enum class that holds the locations, city, counties and zipcodes of the locations associated with clinic*/
public enum Location {

    /**Bridgewater location*/
    BRIDGEWATER("Somerset", "08807"),
    /**Edison location*/
    EDISON("Middlesex", "08817"),
    /**Clark location*/
    CLARK("Union", "07066"),
    /**Princeton location*/
    PRINCETON("Mercer", "08542"),
    /**Piscataway location*/
    PISCATAWAY("Middlesex", "08854"),
    /**Morristown location*/
    MORRISTOWN("Morris", "07960");

    /**String value that stores county of location*/
    private final String county;
    /**String value that stores zip of location*/
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