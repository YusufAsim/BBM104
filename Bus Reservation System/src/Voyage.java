import java.util.ArrayList;

/**
 * Voyage class is an abstract class representing a voyage in the Booking System.
 * It contains attributes and methods that are general to its subclasses.
 */
abstract class Voyage {
    /**
     * This method sets the revenue of the voyage.
     * @param revenue The fixed revenue of the voyage.
     */
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    /**
     * This method returns the seats of the voyage.
     * @return The seats of the voyage.
     */
    public ArrayList<String> getSeats() {
        return seats;
    }

    private ArrayList<String> seats = new ArrayList<String>(); // ArrayList is used to store the seats of the voyage. Easy to add and remove seats.
    private int ID;
    private String departureCity;
    private String arrivalCity;

    private int aRowNumber;
    private double regularPrice;// double using because of precision in calculations.
    /**
     * This method returns the regular price of the voyage.
     * @return The regular price of the voyage.
     */
    public double getRegularPrice() {
        return regularPrice;
    }

    private double revenue = 0;// double using because of precision in calculations.
    /**
     * This method returns the revenue of the voyage.
     * @return The revenue of the voyage.
     */
    public double getRevenue() {
        return revenue;
    }

    /**
     * This method returns the ID of the voyage.
     * @return The ID of the voyage.
     */
    public int getID() {
        return ID;
    }
    /**
     * This method returns the departure city of the voyage.
     * @return The departure city of the voyage.
     */
    public String getDepartureCity() {
        return departureCity;
    }

    /**
     * This method returns the arrival city of the voyage.
     * @return The arrival city of the voyage.
     */
    public String getArrivalCity() {
        return arrivalCity;
    }


    /**
     * The constructor of the Voyage class.
     * @param ID The ID of the voyage.
     * @param departureCity The departure city of the voyage.
     * @param arrivalCity The arrival city of the voyage.
     * @param aRowNumber The number of rows in the voyage.
     * @param regularPrice The regular price of the voyage.
     */
    public Voyage(int ID, String departureCity, String arrivalCity, int aRowNumber, double regularPrice){
        this.ID = ID;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.aRowNumber = aRowNumber;
        this.regularPrice = regularPrice;
    }
    /**
     * This method returns the number of rows in the voyage.
     * @return The number of rows in the voyage.
     */
    public int getaRowNumber() {
        return aRowNumber;
    }
    /**
     * This abstract method symbolize a Z report for implementation.
     */
    abstract String ZReport();
}
