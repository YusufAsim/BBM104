import java.util.ArrayList;
/**
 * This class represents the loading step of GMM slot machine.
 * It contains methods to create a list of slots, check if the machine is full,
 * check if a slot for a given product already exists and is not full,
 * and separate a product info string into an array of strings.
 */
public class SlotMachine {
    /**
     * This method creates a list of slots from the given input content.
     * If a slot for a product already exists and is not full, it increases the number of that product in the slot.
     * If a slot for a product does not exist and there is space, it creates a new slot for that product.
     * If there is no space for a new product, it writes an info message to the output.
     * If the machine is full, it writes an info message to the output and also breaks the loop.
     * If there are less than 24 slots, it fills the other slots with empty slots at the end.
     *
     * @param inputContent The input content.
     * @param output The output path.
     * @return The list of created slots.
     */
    public static  ArrayList<Slot> slotListCreator(String[] inputContent, String output) {
        ArrayList<Slot> slotList = new ArrayList<>();
        for (String s : inputContent) {
            String[] infoProduct = stringSeparator(s);
            if (slotList.isEmpty()) {
                Slot slot = new Slot(infoProduct[0], infoProduct[1], infoProduct[2]);
                slot.setNumber(1);
                slotList.add(slot);
                continue;
            }
            if (isFoundSlot(infoProduct[0], slotList)) {
                for (Slot slot : slotList) {
                    if (slot.getProductName().equals(infoProduct[0])&& slot.getNumber()<10) {//if the product is found and the slot is not full
                        slot.setNumber(slot.getNumber()+1);
                        break;
                    }
                }
            }else{
                if (slotList.size()<24){
                    Slot slot = new Slot(infoProduct[0], infoProduct[1], infoProduct[2]);
                    slot.setNumber(1);
                    slotList.add(slot);
                }else {
                    FileOutput.writeToFile(output, "INFO: There is no available place to put " + infoProduct[0],true,true);
                    if (fill(slotList) == 1){
                        continue;
                        }
                    else if (fill(slotList)==-1) {
                        FileOutput.writeToFile(output,"INFO: The machine is full!",true,true);
                        break;
                    }
                }
            }
        }
        if (slotList.size()<24){
            for(int i = 24 - slotList.size();i>0;i--){
                Slot slot = new Slot();
                slotList.add(slot);
            }
        }
        return slotList;
    }
    /**
     * This method is used to check if the machine is full or not.
     *
     * @param list The list of slots.
     * @return 1 if there is a slot that is not full, -1 if all slots are full.
     */
    private static int fill(ArrayList<Slot> list){
        for (Slot slot : list){
            if (slot.getNumber() == 10){
                continue;
            }else{
                return 1;
            }
        }
        return -1;
    }

    /**
     * This method is used to check if a slot for a given product is valid to place.
     * It iterates over the list of slots and if it finds a slot with the given product name that is not full, it returns true.
     * If no such slot is found such that, it returns false.
     *
     * @param element The product name.
     * @param list The list of slots.
     * @return true if a slot is available to place, false otherwise.
     */
    private static boolean isFoundSlot(String element, ArrayList<Slot> list) {
        for (Slot slot : list) {
            if (slot.getProductName().equals(element)&& slot.getNumber()<10) {
                return true;
            }
        }
        return false;
    }
    /**
     * This method separates a string of product information values into an array of strings.
     * It splits the string from tab characters in string argument.
     *
     * @param productInfo The product info string.
     * @return The separated product information values as an array of strings.
     */
    private static String[] stringSeparator(String productInfo) {
        String[] result = productInfo.split("\t");
        return result;
    }
}
