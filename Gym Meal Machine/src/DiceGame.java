import java.util.ArrayList;

public class DiceGame {
    /**
     * This method is used as a driver method that contains first assignment statements and methods.
     * @param args Array of strings of command line arguments
     */
    public static void main(String[] args) {
        String[] inputContent = FileInput.readFile(args[0], false, false);
        String[] namesOfPlayers = inputContent[1].split(",");
        ArrayList<Player> playerList = playerIdentifier(namesOfPlayers);
        insideGame(inputContent, playerList, args[1]);
    }
    /**
     * This method checks if one of dice numbers equals to 1.
     * @param diceNumbers the numbers of dices thrown in the game in type of String.
     * @return true if there is aimed case, false if not
     */
    public static boolean isScoreNotChangedCase(String diceNumbers) {
        String[] stringArray = diceNumbers.split("-");
        int firstNumber = Integer.parseInt(stringArray[0]);// parsing process to gain numbers separately
        int secondNumber = Integer.parseInt(stringArray[1]);

        if ((firstNumber == 1 && secondNumber != 1) || (firstNumber != 1 && secondNumber == 1)) {
            return true;
        }
        return false;
    }

    /**
     * This method creates the objects of Player class into an ArrayList of Player.
     * @param namesOfPlayers the names of players in the order of input content
     * @return ArrayList including player objects
     */
    public static ArrayList<Player> playerIdentifier(String[] namesOfPlayers) {
        ArrayList<Player> players = new ArrayList<>(); // putting objects into specified arraylist
        for (String name : namesOfPlayers) {
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }

    /**
     * This method controls progress of the game, eliminates players according to their moves and writes
     * desired outputs by using FileOutput class.
     * @param content content of input file inside Array of String
     * @param playerList ArrayList including player objects
     * @param outputPath command line argument for output file
     */
    public static void insideGame(String[] content, ArrayList<Player> playerList, String outputPath) {
        for (int i = 2; i < content.length; ) {
            for (Player player : playerList) {
                if (i >= content.length) { //to prevent array range limit error
                    break;
                }
                if (!player.isActivePlayer()) { //checking player still playing game
                    continue;
                }
                if (content[i].equals("1-1")) {
                    FileOutput.writeToFile(outputPath, String.format("%s threw 1-1. Game over %s!",
                            player.getName(), player.getName()), true, true);
                    player.setActivePlayer(false);
                    i += 1;
                    continue;
                }
                if (content[i].equals("0-0")) {
                    FileOutput.writeToFile(outputPath, String.format("%s skipped the turn and %s’s score is %d.",
                            player.getName(), player.getName(), player.scoreCalculator(content[i])), true, true);
                    i += 1;
                    continue;
                }
                if (isScoreNotChangedCase(content[i])) {
                    FileOutput.writeToFile(outputPath, String.format("%s threw %s and %s’s score is %d.",
                            player.getName(), content[i], player.getName(), player.scoreCalculator(content[i])), true, true);
                    i += 1;
                    continue;
                }
                FileOutput.writeToFile(outputPath, String.format("%s threw %s and %s’s score is %d.",
                        player.getName(), content[i], player.getName(), player.scoreCalculator(content[i])), true, true);
                i += 1;
            }
        }
        for (Player player : playerList) {
            if (player.isActivePlayer()) {// to pick the one who won game
                FileOutput.writeToFile(outputPath, String.format("%s is the winner of the game with the score of %d. Congratulations %s!",
                        player.getName(), player.getScore(), player.getName()), true, false);
            }
        }
    }
}
