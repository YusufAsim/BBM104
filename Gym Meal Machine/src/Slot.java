/**
 * This class represents a slot object in a GMM machine.
 */
public class Slot {
    private String productName = "___";
    private  String price = "0";
    private String nutritionValues = "0";
    private int number = 0;

    public String getPrice() {
        return price;
    }
    public int getNumber() {
        return number;
    }
    public String getProductName() {
        return productName;
    }
    public String getNutritionValues() {
        return nutritionValues;
    }


    public void setNumber(int number) {
        this.number = number;
    }
    public void setNutritionValues(String nutritionValues) {
        this.nutritionValues = nutritionValues;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Default constructor for the Slot class.
     */
    public Slot() {
    }

    /**
     * Constructor for the Slot class.
     *
     * @param productName The name of the product.
     * @param price The price of the product.
     * @param nutritionValues The nutrition values of the product.
     */
    public Slot(String productName, String price, String nutritionValues) {
        this.productName = productName;
        this.price = price;
        this.nutritionValues = nutritionValues;
    }

    /**
     * This method calculates the calories of a product by using its nutrition values.
     *
     * @param nutritionValues The nutrition values of the product.
     * @return The calculated calories of the product.
     */
    public static double calorieCalculator(String nutritionValues) {
        String[] valueArray = nutritionValues.split("\\s");
        if (valueArray.length == 3) {
            double protein = Double.parseDouble(valueArray[0]); // double var type not for loss of precision.
            double carbohydrate = Double.parseDouble(valueArray[1]);
            double fat = Double.parseDouble(valueArray[2]);
            return (4 * protein) + (4 * carbohydrate) + (9 * fat);
        } else {
            return 0;
        }
    }


    /**
     * This method is used to separate the nutrition values of a product into an array of integers.
     *
     * @param nutritionValues The nutrition values of the product.
     * @return The separated nutrition values inside an array of integers.
     */
    public  static int[] nutritionValuesSeparator(String nutritionValues){
        String[] separatedValues = nutritionValues.split("\\s");
        int[] values = new int[3];
        for (int i = 0; i<3; i++){
            values[i] = (int) Double.parseDouble(separatedValues[i]);
        }
        return values;
    }
}

