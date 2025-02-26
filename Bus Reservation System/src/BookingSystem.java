import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.io.File;
/**
 * This class representing a booking system for all kind of voyages.
 * It includes methods for initializing voyages, selling and refunding tickets,
 * and printing z report according to commands in input file.
 */
public class BookingSystem {

    /**
     * The main method of the BookingSystem class.
     * It reads command line arguments, checks for errors associated command line arguments.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        commandLineError(args);
        String[] lines = FileInput.readFile(args[0], true, true);
        ArrayList<Voyage> voyages = new ArrayList<Voyage>();// storing voyage objects , using arraylist because of dynamic feature
        bookingSystemProcess1(args[1], lines, voyages);
        try {
            RandomAccessFile file = new RandomAccessFile(args[1], "rw");
            long length = file.length();

            if (length > 0) {
                file.setLength(length - 1); // excluding last line
            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method performs the commands in input file.
     * @param outputPath The path to the output file.
     * @param inputContextLines The lines of the input file.
     * @param voyages The list of voyages.
     */
    public static void bookingSystemProcess1(String outputPath, String[] inputContextLines, ArrayList<Voyage> voyages){
        for (String line : inputContextLines) {
            String[] separatedElements = line.split("\t");
            FileOutput.writeToFile(outputPath, "COMMAND: " + line, true, true);
            if (commandError(outputPath, separatedElements)) {
                continue;
            }
            switch (separatedElements[0]) {
                case "INIT_VOYAGE":
                    initVoyageHandler(outputPath, separatedElements, voyages);
                    break;
                case "Z_REPORT":
                    zReportHandler(outputPath, separatedElements, voyages);
                    break;
                case "PRINT_VOYAGE":
                    printVoyageHandler(outputPath, separatedElements, voyages);
                    break;
                case "SELL_TICKET":
                    sellTicketHandler(outputPath, separatedElements, voyages);
                    break;
                case "REFUND_TICKET":
                    refundTicketHandler(outputPath, separatedElements, voyages);
                    break;
                case "CANCEL_VOYAGE":
                    cancelVoyageHandler(outputPath, separatedElements, voyages);
                    break;
                default:
                    break;
            }
            if (line == inputContextLines[inputContextLines.length-1]){// if the last line is not Z_REPORT, printing Z_REPORT
                if (!separatedElements[0].equals("Z_REPORT")) {
                    FileOutput.writeToFile(outputPath, "Z Report:", true, true);
                    voyages.sort(Comparator.comparing(Voyage::getID));
                    FileOutput.writeToFile(outputPath, "----------------", true, true);
                    if (voyages.size() == 0) {
                        FileOutput.writeToFile(outputPath, "No Voyages Available!", true, true);
                        FileOutput.writeToFile(outputPath, "----------------", true, true);
                        continue;
                    }
                    for (Voyage voyage : voyages) {
                        FileOutput.writeToFile(outputPath, voyage.ZReport(), true, true);
                        FileOutput.writeToFile(outputPath, "----------------", true, true);
                    }
                }
            }
        }
    }

    /**
     * This method initialize a voyage object.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void initVoyageHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (initVoyageFormatError(outputPath, separatedElements)) {
            return;// in case there is an error about elements of the command
        }
        if (voyageError(outputPath, Integer.parseInt(separatedElements[2]), voyages)) {
            return;
        }
        if (separatedElements[1].equals("Premium")) {
            Premium premium = new Premium(Integer.parseInt(separatedElements[2]), separatedElements[3], separatedElements[4], Integer.parseInt(separatedElements[5]), Float.parseFloat(separatedElements[6]), Integer.parseInt(separatedElements[7]), Integer.parseInt(separatedElements[8]));
            voyages.add(premium);
            FileOutput.writeToFile(outputPath, String.format("Voyage %s was initialized as a premium (1+2) voyage from %s to %s with %.2f TL" +
                            " priced %d regular seats and %.2f TL priced %d premium seats." +
                            " Note that refunds will be %.0f%s less than the paid amount.",
                    separatedElements[2], separatedElements[3], separatedElements[4], premium.getRegularPrice(), 2 * premium.getaRowNumber(), premium.calculatePremiumPrice(), premium.getaRowNumber(), premium.getRefundCutRate(), "%"), true, true);
        }
        if (separatedElements[1].equals("Standard")) {
            Standard standard = new Standard(Integer.parseInt(separatedElements[2]), separatedElements[3], separatedElements[4], Integer.parseInt(separatedElements[5]), Float.parseFloat(separatedElements[6]), Integer.parseInt(separatedElements[7]));
            voyages.add(standard);
            FileOutput.writeToFile(outputPath, String.format("Voyage %s was initialized as a standard (2+2) voyage from %s to %s with %.2f TL" +
                            " priced %d regular seats. Note that refunds will be %.0f%s less than the paid amount.",
                    separatedElements[2], separatedElements[3], separatedElements[4], standard.getRegularPrice(), 4 * standard.getaRowNumber(), standard.getRefundCutRate(), "%"), true, true);

        }
        if (separatedElements[1].equals("Minibus")) {
            Minibus minibus = new Minibus(Integer.parseInt(separatedElements[2]), separatedElements[3], separatedElements[4], Integer.parseInt(separatedElements[5]), Float.parseFloat(separatedElements[6]));
            voyages.add(minibus);
            FileOutput.writeToFile(outputPath, String.format("Voyage %s was initialized as a minibus (2) voyage from %s to %s with %.2f TL" +
                            " priced %d regular seats. Note that minibus tickets are not refundable.",
                    separatedElements[2], separatedElements[3], separatedElements[4], minibus.getRegularPrice(), 2 * minibus.getaRowNumber()), true, true);
        }
    }
    /**
     * This method generates a Z report for all voyages.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void zReportHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (ZReportFormatError(outputPath, separatedElements)) {
            return;
        }
        FileOutput.writeToFile(outputPath, "Z Report:", true, true);
        voyages.sort(Comparator.comparing(Voyage::getID));// sorting the voyages according to their IDs ascending order
        FileOutput.writeToFile(outputPath, "----------------", true, true);
        if (voyages.size() == 0) {
            FileOutput.writeToFile(outputPath, "No Voyages Available!", true, true);
            FileOutput.writeToFile(outputPath, "----------------", true, true);
            return;
        }
        for (Voyage voyage : voyages) {
            FileOutput.writeToFile(outputPath, voyage.ZReport(), true, true);
            FileOutput.writeToFile(outputPath, "----------------", true, true);
        }
    }
    /**
     * This method refunds tickets for a voyage.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void printVoyageHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (printVoyageFormatError(outputPath, separatedElements)) {
            return;
        }
        if (voyageIDError(outputPath, Integer.parseInt(separatedElements[1]), voyages)) {
            return;
        }
        for (Voyage voyage : voyages) {
            if (voyage.getID() == Integer.parseInt(separatedElements[1])) {
                FileOutput.writeToFile(outputPath, voyage.ZReport(), true, true);
            }
        }
    }

    /**
     * This method sells tickets for a voyage.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void sellTicketHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (sellTicketFormatError(outputPath, separatedElements)) {
            return;
        }
        if (voyageIDError(outputPath, Integer.parseInt(separatedElements[1]), voyages)) {
            return;
        }
        int voyageID = Integer.parseInt(separatedElements[1]);
        String[] numbers = separatedElements[2].split("_");
        int[] seatNumbers = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            seatNumbers[i] = Integer.parseInt(numbers[i]);
        }
        for (Voyage voyage : voyages) {
            if (voyage.getID() == voyageID) {
                double previousRevenue = voyage.getRevenue();
                if (seatNumberErrors(outputPath, seatNumbers, voyage)) {// checking for possible errors in seat numbers
                    break;
                }
                if (ticketSeller(voyage, seatNumbers)) {
                    FileOutput.writeToFile(outputPath, String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                            separatedElements[2].replace("_", "-"), voyageID, voyage.getDepartureCity(),
                            voyage.getArrivalCity(), voyage.getRevenue() - previousRevenue), true, true);
                } else {
                    FileOutput.writeToFile(outputPath, "ERROR: One or more seats already sold!", true, true);
                }
            }
        }
    }
    /**
     * This method refunds tickets for a voyage.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void refundTicketHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (refundTicketFormatError(outputPath, separatedElements)) {
            return;
        }
        if (voyageIDError(outputPath, Integer.parseInt(separatedElements[1]), voyages)) {
            return;
        }
        int voyageID = Integer.parseInt(separatedElements[1]);
        String[] numbers = separatedElements[2].split("_");
        int[] seatNumbers = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            seatNumbers[i] = Integer.parseInt(numbers[i]);
        }
        for (Voyage voyage : voyages) {
            if (voyage.getID() == voyageID) {
                double previousRevenue = voyage.getRevenue();
                if (seatNumberErrors(outputPath, seatNumbers, voyage)) {
                    break;
                }
                if (ticketRefund(voyage, seatNumbers)) {// true in case the refund is successful
                    FileOutput.writeToFile(outputPath, String.format("Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                            separatedElements[2].replace("_", "-"), voyageID, voyage.getDepartureCity(),
                            voyage.getArrivalCity(), previousRevenue - voyage.getRevenue()), true, true);
                } else {
                    if (voyage instanceof Minibus) {
                        FileOutput.writeToFile(outputPath, "ERROR: Minibus tickets are not refundable!", true, true);
                    } else {//in case there is an empty seat
                        FileOutput.writeToFile(outputPath, "ERROR: One or more seats are already empty!", true, true);
                    }
                }
            }
        }
    }
    /**
     * This method cancels a voyage.
     * @param outputPath The path of the output file.
     * @param separatedElements The elements of the command.
     * @param voyages The list of voyages.
     */
    public static void cancelVoyageHandler(String outputPath, String[] separatedElements, ArrayList<Voyage> voyages) {
        if (cancelVoyageFormatError(outputPath, separatedElements)) {
            return;
        }
        if (voyageIDError(outputPath, Integer.parseInt(separatedElements[1]), voyages)) {
            return;
        }
        int voyageID = Integer.parseInt(separatedElements[1]);
        for (Voyage voyage : voyages) {
            if (voyage.getID() == voyageID) {
                if (!cancelledVoyageRefund(voyage)) {// true in case the voyage is Minibus
                    FileOutput.writeToFile(outputPath, "ERROR: Minibus tickets are not refundable!", true, true);
                    voyages.remove(voyage);
                    break;
                }
                FileOutput.writeToFile(outputPath, String.format("Voyage %d was successfully cancelled!", voyageID), true, true);
                FileOutput.writeToFile(outputPath, "Voyage details can be found below:", true, true);
                FileOutput.writeToFile(outputPath, voyage.ZReport(), true, true);
                voyages.remove(voyage);
                break;
            }
        }
    }
    /**
     * This method sells tickets for a voyage.
     * @param voyage The voyage.
     * @param seatNumbers The seat numbers.
     * @return true if the tickets were successfully sold, false otherwise.
     */
    public static boolean ticketSeller(Voyage voyage, int[] seatNumbers) {
        for (int seatNumber : seatNumbers) {
            if (voyage.getSeats().get(seatNumber - 1).equals("X")) {
                return false;
            }
        }

        for (int seatNumber : seatNumbers) {
            voyage.getSeats().set(seatNumber - 1, "X");// setting the seat as sold
            if (voyage instanceof Premium) {// controlling the type of the voyage
                Premium premium = (Premium) voyage;
                if (seatNumber % 3 == 1) {// controlling the premium type of seat
                    voyage.setRevenue(voyage.getRevenue() + premium.calculatePremiumPrice());
                } else {// calculating the revenues
                    voyage.setRevenue(voyage.getRevenue() + premium.getRegularPrice());
                }
            }
            if (voyage instanceof Standard) {
                Standard standard = (Standard) voyage;
                voyage.setRevenue(voyage.getRevenue() + standard.getRegularPrice());
            }
            if (voyage instanceof Minibus) {
                Minibus minibus = (Minibus) voyage;
                voyage.setRevenue(voyage.getRevenue() + minibus.getRegularPrice());
            }
        }
        return true;
    }
    /**
     * This method refunds tickets for a voyage.
     * @param voyage The voyage.
     * @param seatNumbers The seat numbers.
     * @return true if the tickets were successfully refunded, false otherwise.
     */
    public static boolean ticketRefund(Voyage voyage, int[] seatNumbers) {
        for (int seatNumber : seatNumbers) {
            if (voyage.getSeats().get(seatNumber - 1).equals("*")) {
                return false;
            }
        }
        if (voyage instanceof Premium) {//control the type of the voyage
            Premium premium = (Premium) voyage;//casting the voyage to Premium
            for (int seatNumber : seatNumbers) {
                voyage.getSeats().set(seatNumber - 1, "*");
                if (seatNumber % 3 == 1) {
                    voyage.setRevenue(voyage.getRevenue() - premium.calculatePremiumPrice() * ((100 - premium.getRefundCutRate()) / 100));
                } else {
                    voyage.setRevenue(voyage.getRevenue() - premium.getRegularPrice() * ((100 - premium.getRefundCutRate()) / 100));
                }
            }
        }
        if (voyage instanceof Standard) {
            Standard standard = (Standard) voyage;
            for (int seatNumber : seatNumbers) {
                voyage.getSeats().set(seatNumber - 1, "*");// setting the seat as empty again
                voyage.setRevenue(voyage.getRevenue() - standard.getRegularPrice() * ((100 - standard.getRefundCutRate()) / 100));
            }
        }

        if (voyage instanceof Minibus) {
            return false;
        }
        return true;
    }
    /**
     * This method refunds tickets for a cancelled voyage.
     * @param voyage The voyage.
     * @return true if the tickets were successfully refunded, false otherwise.
     */
    public static boolean cancelledVoyageRefund(Voyage voyage) {
        if (voyage instanceof Minibus) {
            return false;
        }

        if (voyage instanceof Premium) {
            Premium premium = (Premium) voyage;
            for (int i = 0; i < premium.getSeats().size(); i++) {
                if (premium.getSeats().get(i).equals("X")) {
                    if (i % 3 == 0) {
                        voyage.setRevenue(voyage.getRevenue() - premium.calculatePremiumPrice());
                    } else {
                        voyage.setRevenue(voyage.getRevenue() - premium.getRegularPrice());
                    }
                }
            }
        }
        if (voyage instanceof Standard) {
            Standard standard = (Standard) voyage;
            for (int i = 0; i < standard.getSeats().size(); i++) {
                if (standard.getSeats().get(i).equals("X")) {// if the seat is sold
                    voyage.setRevenue(voyage.getRevenue() - standard.getRegularPrice());
                }
            }
        }
        return true;
    }
    /**
     * This method checks for errors associated with INIT_VOYAGE command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if there is an error, false otherwise.
     */
    public static boolean initVoyageFormatError(String outputPath, String[] elementsOfCommand) {
        ArrayList<String> validVoyageTypes = new ArrayList<>(Arrays.asList("Premium", "Standard", "Minibus"));
        if (!validVoyageTypes.contains(elementsOfCommand[1])) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
            return true;
        }
        if (elementsOfCommand.length != 9 && elementsOfCommand[1].equals("Premium")) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
            return true;
        }
        if (elementsOfCommand.length != 8 && elementsOfCommand[1].equals("Standard")) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
            return true;
        }
        if (elementsOfCommand.length != 7 && elementsOfCommand[1].equals("Minibus")) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
            return true;
        }
        if ((Integer.parseInt(elementsOfCommand[2]) <= 0) || (Double.parseDouble(elementsOfCommand[2]) != Integer.parseInt(elementsOfCommand[2]))) {
            FileOutput.writeToFile(outputPath, "ERROR: " + elementsOfCommand[2] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return true;
        }
        if ((Integer.parseInt(elementsOfCommand[5]) <= 0) || (Double.parseDouble(elementsOfCommand[5]) != Integer.parseInt(elementsOfCommand[5]))) {
            FileOutput.writeToFile(outputPath, "ERROR: " + elementsOfCommand[5] + " is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
            return true;
        }
        if ((Float.parseFloat(elementsOfCommand[6]) <= 0)) {
            FileOutput.writeToFile(outputPath, "ERROR: " + elementsOfCommand[6] + " is not a positive number, price must be a positive number!", true, true);
            return true;
        }
        if (elementsOfCommand[1].equals("Premium") || elementsOfCommand[1].equals("Standard")) {
            if (Double.parseDouble(elementsOfCommand[7]) < 0 || Double.parseDouble(elementsOfCommand[7]) > 100) {
                FileOutput.writeToFile(outputPath, "ERROR: " + elementsOfCommand[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                return true;
            }
        }
        if (elementsOfCommand[1].equals("Premium")) {
            if (Double.parseDouble(elementsOfCommand[8]) < 0 || Double.parseDouble(elementsOfCommand[8]) > 100) {
                FileOutput.writeToFile(outputPath, "ERROR: " + elementsOfCommand[8] + " is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                return true;
            }
        }
        return false;
    }
    /**
     * This method checks for errors in the command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if there is an error, false otherwise.
     */
    public static boolean commandError(String outputPath, String[] elementsOfCommand) {
        ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("INIT_VOYAGE", "Z_REPORT", "PRINT_VOYAGE", "SELL_TICKET", "REFUND_TICKET", "CANCEL_VOYAGE"));
        if (!validCommands.contains(elementsOfCommand[0])) {
            FileOutput.writeToFile(outputPath, "ERROR: There is no command namely " + elementsOfCommand[0]+"!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors in the SELL_TICKET command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if an error exists, false otherwise.
     */
    public static boolean sellTicketFormatError(String outputPath, String[] elementsOfCommand) {
        if (elementsOfCommand.length != 3) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors in the CANCEL_VOYAGE command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if an error exists, false otherwise.
     */
    public static boolean cancelVoyageFormatError(String outputPath, String[] elementsOfCommand) {
        if (elementsOfCommand.length != 2) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
            return true;
        }
        if (Double.parseDouble(elementsOfCommand[1])!=Integer.parseInt(elementsOfCommand[1]) || Integer.parseInt(elementsOfCommand[1])<=0){
            FileOutput.writeToFile(outputPath, "ERROR: "+elementsOfCommand[1]+" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return true;

        }

        return false;
    }
    /**
     * This method checks for errors in the Z_REPORT command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if there is an error, false otherwise.
     */
    public static boolean ZReportFormatError(String outputPath, String[] elementsOfCommand) {
        if (elementsOfCommand.length != 1) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors in the PRINT_VOYAGE command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if there is an error, false otherwise.
     */
    public static boolean printVoyageFormatError(String outputPath, String[] elementsOfCommand) {
        if (elementsOfCommand.length != 2) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
            return true;
        }
        if (Integer.parseInt(elementsOfCommand[1])<=0||Double.parseDouble(elementsOfCommand[1])!=Integer.parseInt(elementsOfCommand[1])){
            FileOutput.writeToFile(outputPath, "ERROR: "+elementsOfCommand[1]+" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors in the REFUND_TICKET command.
     * @param outputPath The path of the output file.
     * @param elementsOfCommand The elements of the command.
     * @return true if there is an error, false otherwise.
     */
    public static boolean refundTicketFormatError(String outputPath, String[] elementsOfCommand) {
        if (elementsOfCommand.length != 3) {
            FileOutput.writeToFile(outputPath, "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors in the voyage.
     * @param outputPath The path of the output file.
     * @param voyageID The ID of the voyage.
     * @param voyages The list of voyages.
     * @return true if there is an error, false otherwise.
     */
    public static boolean voyageError(String outputPath, int voyageID, ArrayList<Voyage> voyages) {
        for (Voyage voyage : voyages) {
            if (voyage.getID() == voyageID) {
                FileOutput.writeToFile(outputPath, "ERROR: There is already a voyage with ID of " + voyageID + "!", true, true);
                return true;
            }
        }
        return false;
    }
    /**
     * This method checks for errors in the voyage ID.
     * @param outputPath The path of the output file.
     * @param voyageID The ID of the voyage.
     * @param voyages The list of voyages.
     * @return true if error exists, false otherwise.
     */
    public static boolean voyageIDError(String outputPath, int voyageID, ArrayList<Voyage> voyages) {
        for (Voyage voyage : voyages) {
            if (voyage.getID() == voyageID) {
                return false;
            }
        }
        FileOutput.writeToFile(outputPath, "ERROR: There is no voyage with ID of " + voyageID + "!", true, true);
        return true;
    }
    /**
     * This method checks for errors in the seat numbers.
     * @param outputPath The path of the output file.
     * @param seatNumbers The seat numbers.
     * @param voyage The voyage.
     * @return true if error exists, false otherwise.
     */
    public static boolean seatNumberErrors(String outputPath, int[] seatNumbers, Voyage voyage) {
        for (int seatNumber : seatNumbers) {
            if ((seatNumber <= 0)) {
                FileOutput.writeToFile(outputPath, "ERROR: "+seatNumber+" is not a positive integer, seat number must be a positive integer!", true, true);
                return true;
            }
        }
        if ((seatNumbers.length == 1)&&(voyage.getSeats().size()<seatNumbers[0])){
            FileOutput.writeToFile(outputPath, "ERROR: There is no such a seat!", true, true);
            return true;
        }
        return false;
    }
    /**
     * This method checks for errors about command line arguments.
     * @param arguments The command line arguments.
     */
    public static void commandLineError(String[] arguments){
        if (arguments.length!=2){// in case there is number of command line arguments different form 2
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            System.exit(0);
        }else {
            File inputFile = new File(arguments[0]);
            if (!inputFile.exists()) {// input file does not exist
                System.out.println("ERROR: This program cannot read from the \"" +arguments[0] + "\", the file does not exist. Program is going to terminate!");
                System.exit(0);
            }
            File outputFile = new File(arguments[1]);
            if (!outputFile.canWrite()) {// output file cannot be written
                System.out.println("ERROR: This program cannot write to the \"" + arguments[1] + "\", please check the permissions to write that directory. Program is going to terminate!");
                System.exit(0);
            }
        }
    }
}