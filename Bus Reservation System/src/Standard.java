/**
 * Standard class inherits from Voyage class.
 * it overrides the ZReport method to return the report of the standard type voyage.
 */
public class Standard extends Voyage{

    private double refundCutRate;// double using because of precision in calculations.
    /**
     * The constructor of the Standard class.
     * @param ID The ID of the voyage.
     * @param departureCity The departure city of the voyage.
     * @param arrivalCity The arrival city of the voyage.
     * @param aRowNumber The number of rows in the standard voyage.
     * @param regularPrice The regular price of the voyage.
     * @param refundCut The refund cut rate.
     */
    public Standard(int ID, String departureCity, String arrivalCity, int aRowNumber, double regularPrice, int refundCut){
        super(ID, departureCity, arrivalCity, aRowNumber, regularPrice);
        this.refundCutRate = refundCut;
        for (int i=0; i < aRowNumber*4; i++){
            this.getSeats().add("*");// creating empty seats
        }
    }
    /**
     * This method returns the refund cut rate.
     * @return The refund cut rate.
     */
    public double getRefundCutRate() {
        return refundCutRate;
    }
    /**
     * This method returns a Z report for the standard voyage.
     * @return The Z report inside a string.
     */
    public String ZReport(){
        String report = "Voyage " + getID()+ "\n" + getDepartureCity() + "-" + getArrivalCity();
        for (int i=0; i < this.getSeats().size(); i++){
            if (i%4==0){
                report += "\n";
            }else{
                report += " ";
            }
            if (i%4==2){
                report += "| ";
            }
            report += this.getSeats().get(i);
        }
        report += String.format("\nRevenue: %.2f",getRevenue());
        return report;
    }


}
