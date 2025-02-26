/**
 * Premium class inherits from Voyage class.
 * it overrides the ZReport method to return the report of the premium type voyage.
 */
public class Premium extends Voyage{
    private double refundCutRate;// double using because of precision in calculations.
    /**
     * This method returns the refund cut rate.
     * @return The refund cut rate.
     */
    public double getRefundCutRate() {
        return refundCutRate;
    }

    private float premiumFee = 0;
    /**
     * The constructor of the Premium class.
     * @param ID The ID of the voyage.
     * @param departureCity The departure city of the voyage.
     * @param arrivalCity The arrival city of the voyage.
     * @param aRowNumber The number of rows in the premium voyage.
     * @param regularPrice The regular price of the voyage.
     * @param refundCut The refund cut rate.
     * @param premiumFee The premium fee.
     */
    public Premium(int ID, String departureCity, String arrivalCity, int aRowNumber, double regularPrice, int refundCut, float premiumFee){
        super(ID, departureCity, arrivalCity, aRowNumber, regularPrice);
        this.refundCutRate = refundCut;
        this.premiumFee = premiumFee;
        for (int i=0; i < aRowNumber*3; i++){
            this.getSeats().add("*");// creating empty seats
        }
    }

    /**
     * This method returns a Z report for the premium voyage.
     * @return The Z report inside a string.
     */
    public String ZReport(){
        String report = "Voyage " + getID()+ "\n" + getDepartureCity() + "-" + getArrivalCity();
        for(int i=0; i < this.getSeats().size(); i++){
            if (i%3==0){
                report += "\n";
            }else{
                report += " ";
            }

            if (i%3==1){
                report += "| ";
            }
            report += this.getSeats().get(i);
        }
        report += String.format("\nRevenue: %.2f",getRevenue());
        return report;
    }
    /**
     * This method calculates the premium price.
     * @return The premium price.
     */
    public double calculatePremiumPrice(){
        return this.getRegularPrice()*(1+(this.premiumFee)/100);
    }
}
