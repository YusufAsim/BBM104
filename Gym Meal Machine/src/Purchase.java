import java.util.ArrayList;
/**
 * This class represents a purchase case object in GMM machine.
 * It contains methods to check nutrition value, calorie and slot number exceptions, and to make a purchase.
 */
public class Purchase {
    private String money;
    private String choice;
    private String value;
    /**
     * Constructor that needs to be entered all instance variables for the Purchase class.
     *
     * @param money The amount of money which client has.
     * @param choice The choice of the user (PROTEIN, CARB, FAT, CALORIE, NUMBER).
     * @param value The value according to the choice of the user.
     */
    public Purchase(String money, String choice, String value) {
        this.money = money;
        this.choice = choice;
        this.value = value;
    }

    public String getMoney() {
        return money;
    }

    public String getChoice() {
        return choice;
    }

    public String getValue() {
        return value;
    }
    /**
     * This method is used to calculate the cumulative money from the input string that contains different types of banknotes.
     * It splits the input string from the space character and sums up the values.
     *
     * @param input The input string that contains different type of banknotes.
     * @return The calculated cumulative money.
     */
    public static int cumulativeMoney(String input,String outputPath) {
        String[] moneys = input.split("\\s");
        int result = 0;
        if (validMoneyChecker(moneys) == -1){
            FileOutput.writeToFile(outputPath,"INFO: Invalid money type.",true,true);
        }
        for (String money : moneys) {
            if (money.equals("1")||money.equals("5")||money.equals("10")||money.equals("20")||money.equals("50")||money.equals("100")||money.equals("200")){
                result += Integer.parseInt(money);
            }

        }
        return result;
    }

    /**
     * This method checks the suitability of the money types that entered by the user.
     * @param moneys the money types that entered by the user.
     * @return -1 if all the money's type are not valid,1 otherwise.
     */
    private static int validMoneyChecker(String[] moneys){
        for (String money : moneys){
            if (money.equals("1")||money.equals("5")||money.equals("10")||money.equals("20")||money.equals("50")||money.equals("100")||money.equals("200")){
                continue;
            }else{
                return -1;
            }
        }
        return 1;
    }
    /**
     * This method processes the purchase based on the user's choice.
     * It checks the user's choice and then calls the appropriate method to process the purchase.
     *
     * @param money The amount of money the user has.
     * @param choice The choice of the user (PROTEIN, CARB, FAT, CALORIE, NUMBER).
     * @param value The value according to the choice of the user.
     * @param slotList The list of slots.
     * @param outputPath The output path to write content.
     */
    public  void purchaseProcess(int money, String choice, int value, ArrayList<Slot> slotList, String outputPath) {
        switch (choice) {
            case "PROTEIN":
                InfoChecker(slotList, value, money, 0, outputPath);
                break;
            case "CARB":
                InfoChecker(slotList, value, money, 1, outputPath);
                break;
            case "FAT":
                InfoChecker(slotList, value, money, 2, outputPath);
                break;
            case "CALORIE":
                calorieInfoChecker(slotList, value, money, outputPath);
                break;
            case "NUMBER":
                numberInfoChecker(slotList, value, money, outputPath);
                break;
            default:
        }
    }
    /**
     * This method checks if a product with the given nutrition information exists in the slot list.
     * If a valid product exists and the user has enough money, a purchase happens ,and it updates the slot list.
     * If a valid product does not exist or the user does not have enough money, it writes an info message into the output and returns back the user's money.
     *
     * @param slotList The list of slots.
     * @param value The amount of nutrition's value in the product.
     * @param money The amount of money the user has.
     * @param nutritionIndex The index of the nutrition value in the list of nutrition values(0 for protein, 1 for carb, 2 for fat).
     * @param outputPath The output path.
     * @return -1 after the purchase process.
     */
    private static int InfoChecker(ArrayList<Slot> slotList, int value, int money, int nutritionIndex, String outputPath) {
        for (Slot slot : slotList) {
            if (slot.getProductName().equals("___")) {
                continue;
            }
            if (value + 5 >= Slot.nutritionValuesSeparator(slot.getNutritionValues())[nutritionIndex] &&
                    value - 5 <= Slot.nutritionValuesSeparator(slot.getNutritionValues())[nutritionIndex]) {// checking if the nutrition value is in the true range.
                if (money < Integer.parseInt(slot.getPrice())) { // checking if money is enough to buy.
                    FileOutput.writeToFile(outputPath, "INFO: Insufficient money, try again with more money.", true, true);
                    FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
                    return -1;
                }
                FileOutput.writeToFile(outputPath, String.format("PURCHASE: You have bought one %s", slot.getProductName()), true, true);
                FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money - Integer.parseInt(slot.getPrice())), true, true);
                slot.setNumber(slot.getNumber() - 1);
                if (slot.getNumber() == 0) { // when all products are sold in the slot.
                    slot.setProductName("___");
                    slot.setNutritionValues("0");
                }
                return 1;
            }
        }
        FileOutput.writeToFile(outputPath, "INFO: Product not found, your money will be returned.", true, true);
        FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
        return -1;
    }
    /**
     * This method checks if a product with the given calorie information exists in the slot list.
     * If a valid product exists and the user has enough money, a purchase happens, and it updates the slot list.
     * If a valid product does not exist or the user does not have enough money, it writes an info message into the output and returns back the user's money.
     *
     * @param slotList The list of slots.
     * @param value The amount of calorie value that wanted by the user.
     * @param money The amount of money the user has.
     * @param outputPath The output path.
     * @return -1 after the purchase process.
     */
    private static int calorieInfoChecker(ArrayList<Slot> slotList, int value, int money, String outputPath) {
        for (Slot slot : slotList) {
            if (slot.getProductName().equals("___")) {
                continue;
            }
            if (value + 5 >= Slot.calorieCalculator(slot.getNutritionValues()) &&
                    value - 5 <= Slot.calorieCalculator(slot.getNutritionValues())) {// checking if the nutrition value is in the true range.
                if (money < Integer.parseInt(slot.getPrice())) {
                    FileOutput.writeToFile(outputPath, "INFO: Insufficient money, try again with more money.", true, true);
                    FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
                    return -1;
                }
                FileOutput.writeToFile(outputPath, String.format("PURCHASE: You have bought one %s", slot.getProductName()), true, true);
                FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money - Integer.parseInt(slot.getPrice())), true, true);
                slot.setNumber(slot.getNumber() - 1);
                if (slot.getNumber() == 0) { // when all products are sold in the slot.
                    slot.setProductName("___");
                    slot.setNutritionValues("0");
                }
                return 1;
            }
        }
        FileOutput.writeToFile(outputPath, "INFO: Product not found, your money will be returned.", true, true);
        FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
        return -1;
    }
    /**
     * This method checks if a product with the given slot number exists in the slot list.
     * If a valid product exists and the user has enough money, a purchase happens, and it updates the slot list.
     * If a valid product does not exist or the user does not have enough money, it writes an info message into the output and returns back the user's money.
     *
     * @param slotList The list of slots to.
     * @param value The value of slot number that entered by the user.
     * @param money The amount of money the user has.
     * @param outputPath The output path.
     * @return -1 after the purchase process.
     */
    private static int numberInfoChecker(ArrayList<Slot> slotList, int value, int money, String outputPath) {
        if (slotList.size() < value) {
            FileOutput.writeToFile(outputPath, "INFO: Number cannot be accepted. Please try again with another number.", true, true);
            FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
            return -1;
        } else {
            if (slotList.get(value).getProductName().equals("___")) {
                FileOutput.writeToFile(outputPath, "INFO: This slot is empty, your money will be returned.", true, true);
                FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
                return -1;
            } else {
                if (money < Integer.parseInt(slotList.get(value).getPrice())) { // checking if money is enough to buy.
                    FileOutput.writeToFile(outputPath, "INFO: Insufficient money, try again with more money.", true, true);
                    FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money), true, true);
                    return -1;
                }
            }
        }
        FileOutput.writeToFile(outputPath, String.format("PURCHASE: You have bought one %s", slotList.get(value).getProductName()), true, true);
        FileOutput.writeToFile(outputPath, String.format("RETURN: Returning your change: %d TL", money - Integer.parseInt(slotList.get(value).getPrice())), true, true);
        slotList.get(value).setNumber(slotList.get(value).getNumber() - 1);
        if (slotList.get(value).getNumber() == 0) { // when all products are sold in the slot.
            slotList.get(value).setProductName("___");
            slotList.get(value).setNutritionValues("0");
        }
        return 1;
    }
}