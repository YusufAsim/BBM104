/**
 * Minibus class inherits from Voyage class.
 * it overrides the ZReport method to return the report of the minibus.
 */
public class Minibus extends Voyage{

    /**
     * The constructor of the Minibus subclass.
     * @param ID The ID of the minibus voyage.
     * @param departureCity The departure city of the minibus voyage.
     * @param arrivalCity The arrival city of the minibus voyage.
     * @param aRowNumber The number of seats on one row.
     * @param regularPrice The regular price of the minibus voyage.
     */
    public Minibus(int ID, String departureCity, String arrivalCity, int aRowNumber, float regularPrice){
        super(ID, departureCity, arrivalCity, aRowNumber, regularPrice);
        for (int i=0; i < aRowNumber*2; i++){
            this.getSeats().add("*");// creating empty seats
        }
    }

    /**
     * This method returns the Z report of the minibus voyage.
     * @return The Z report inside a string.
     */
    public String ZReport(){
        String report = "Voyage " + getID()+ "\n" + getDepartureCity() + "-" + getArrivalCity();
        for (int i=0; i < this.getSeats().size(); i++){
            if (i%2==0){
                report += "\n";
            }
            if (i%2==1){
                report += " ";// adding space between seats
            }
            report += this.getSeats().get(i);
        }
        report += String.format("\nRevenue: %.2f",getRevenue());
        return report;
    }
}
