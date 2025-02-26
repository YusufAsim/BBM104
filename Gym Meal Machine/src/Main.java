import java.util.ArrayList;
/**
 * This is the main class of the GMM machine project.
 * It contains methods to read the product input, create the slot list, and process the purchase, return and info part of output.
 */
public class Main {
    /**
     * This method is the main method of the project.
     * It reads the product input, creates the slot list, and processes the purchases since it is a driver method.
     *
     * @param args The command line arguments array. args[0] for the product input file path, args[1] for the purchase input file path, and args[2] for the output file path.
     */
    public static void main(String[] args) {
        String[] productInput = FileInput.readFile(args[0], true, true);
        ArrayList<Slot> slotList = SlotMachine.slotListCreator(productInput, args[2]);
        gmmOutputMaker(args[2],slotList,args[1]);
    }
    /**
     * This method shows the slot chart.
     * It writes the product name, calorie info, and number of each slot to the output file.
     *
     * @param slotList The list of slots.
     * @param outputPath The output path.
     */
    public static void showSlotChart(ArrayList<Slot> slotList, String outputPath){
        FileOutput.writeToFile(outputPath,"-----Gym Meal Machine-----",true,false);
        for (Slot slot : slotList) {
            if (slotList.indexOf(slot)%4 == 0 ){ // a row has 4 slot limitation.
                FileOutput.writeToFile(outputPath,"",true,true);
            }
            FileOutput.writeToFile(outputPath,String.format("%s(%.0f, %s)___",slot.getProductName(),
                    Slot.calorieCalculator(slot.getNutritionValues()),slot.getNumber()),true,false);
        }
        FileOutput.writeToFile(outputPath,"\n----------",true,true);
    }
    /**
     * This method creates the output part of GMM machine project.
     * It shows the slot chart, reads the purchase input, creates the purchase cases, processes the purchases, and shows the slot chart again.
     *
     * @param outputPath The output path.
     * @param slotList The list of slots.
     * @param inputPath The purchase input file path.
     */
    public static void gmmOutputMaker(String outputPath, ArrayList<Slot> slotList, String inputPath){
        showSlotChart(slotList, outputPath);
        String[] purchaseInput = FileInput.readFile(inputPath, true, true);
        ArrayList<Purchase> purchasesCases = new ArrayList<>();// to store purchase cases
        for (String input: purchaseInput) {
            Purchase purchase = new Purchase(inputSeparator(input)[1],inputSeparator(input)[2], inputSeparator(input)[3]);
            purchasesCases.add(purchase);
        }
        for (Purchase p: purchasesCases){
            FileOutput.writeToFile(outputPath, "INPUT: CASH" + "\t" +p.getMoney() + "\t" +p.getChoice() + "\t" +p.getValue(), true,true);
            p.purchaseProcess(Purchase.cumulativeMoney(p.getMoney(),outputPath),p.getChoice(),Integer.parseInt(p.getValue()), slotList, outputPath);
        }
        showSlotChart(slotList, outputPath);
    }
    /**
     * This method separates an input string into an array of strings.
     * It splits the string from tab characters inside it.
     *
     * @param input The input string.
     * @return The separated parts of input  inside an array of strings.
     */
    public static String[] inputSeparator(String input) {
        String[] split = input.split("\t");
        return split;
    }
}

