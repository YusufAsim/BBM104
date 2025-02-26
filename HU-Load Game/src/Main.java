import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import java.util.Locale;
import java.util.Random;

import javafx.scene.control.ScrollPane.ScrollBarPolicy;

/**
 * Main class that contains the basic components of the game.
 */
public class Main extends Application {

    /**
     * Main method that starts the game.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "US"));
        launch(args); // Launching the game
    }

    private MineMachine machine;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HU-load");

        machine = placeDrillRandomly();
        Rectangle skyNode = new Rectangle(800, 105, Color.BLUE);
        Rectangle ground = new Rectangle(800, 650, Color.BROWN.deriveColor(0.5, 1, 0.25, 0.8569));
        Group root = new Group();
        GeneralInfoDisplay statusDisplay = new GeneralInfoDisplay();
        ScrollPane scrollPane = new ScrollPane();
        Block[][] blocks = new Block[16][13];
        Scene scene = new Scene(scrollPane, 820, 750);

        // Setting the position of the sky and the ground
        ground.setY(105);
        ground.setX(0);
        scrollPane.setContent(root);
        scrollPane.setPrefSize(800, 750);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);


        // Adding the nodes to the root
        root.getChildren().add(skyNode);
        root.getChildren().add(ground);
        generateBlocksRandom(blocks, root);

        root.getChildren().add(machine.getView());

        primaryStage.setScene(scene);

        // key event assignment
        scene.setOnKeyPressed(event -> machine.moveTo(event, blocks, root));

        // when the up key is released, the machine will fall
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP) {
                machine.setIsUpKeyPressed(false);
            }
        });

        // Animation timer to handle the gravity and constant fuel consumption
        AnimationTimer animationTimer= new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!machine.getIsUpKeyPressed()) {
                    try {
                        machine.gravity(blocks);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                machine.consumeFuel(root);
                statusDisplay.updateLabelsCurrent(machine);
            }
        };
        animationTimer.start();

        // Adding the status display to the root
        root.getChildren().add(statusDisplay.getVbox());



        // limiting the size of the window
        primaryStage.setMaxWidth(820);
        primaryStage.setMaxHeight(750);
        primaryStage.show();

    }


    /**
     * This method is used to count the number of blocks in the game.
     * @param blocks the blocks in the game
     * @param blockName the name of the block
     * @return the number of blocks
     */
    private int numberOfBlock (Block[][] blocks, String blockName) {
        int count = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 13; j++) {
                if (blocks[i][j] != null && blocks[i][j].getName().equals(blockName)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * This method is used to create blocks in the game.
     * @param blocks the blocks in the game
     * @param i the x coordinate of the block
     * @param j the y coordinate of the block
     * @param blockName the name of the block
     * @param root the root of the game that contains all the nodes
     */
    private void blockCreator(Block[][] blocks, int i, int j, String blockName, Group root) {
        switch(blockName) {
            case "Obstacle":
                blocks[i][j] = new Obstacle();
                break;
            case "TopSoil":
                blocks[i][j] = new TopSoil();
                break;
            case "valuable_einsteinium":
                blocks[i][j] = new ValuableEinsteinium();
                break;
            case "valuable_goldium":
                blocks[i][j] = new ValuableGoldium();
                break;
            case "valuable_ironium":
                blocks[i][j] = new ValuableIronium();
                break;
            case "Lava":
                blocks[i][j] = new Lava("Lava");
                break;
            default:
                blocks[i][j] = new Soil();
                break;
        }
        blocks[i][j].getImageView().setX(i * 50);
        blocks[i][j].getImageView().setY((j * 50) + 100);// passing the sky
        root.getChildren().add(blocks[i][j].getImageView());
    }

    /**
     * This method is used to place the drill randomly in the game on the grass.
     * @return the drill object
     */
    private MineMachine placeDrillRandomly(){
        Random rand = new Random();
        int x = rand.nextInt(16) * 50; // Generate a random number between 0 and 16, then multiply by 50 to get the x coordinate
        return  new MineMachine("assets/drill/drill_01.png", x, 50);
    }


    /**
     * This method is used to generate blocks randomly in the game.
     * @param blocks the blocks in the game
     * @param root the root of the game that contains all the nodes
     */
    private void generateBlocksRandom(Block[][] blocks, Group root){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 13; j++) {
                if ((i == 0 || i == 15) && (j != 0 && j != 12)) {
                    blockCreator(blocks, i, j, "Obstacle", root);
                } else if (j == 12) {
                    blockCreator(blocks, i, j, "Obstacle", root);
                } else if (j == 0) {
                    blockCreator(blocks, i, j, "TopSoil", root);
                }else{
                    if (Math.random() < 0.05 && numberOfBlock(blocks, "valuable_einsteinium") < numberOfBlock(blocks, "Soil") - 3) {
                        blockCreator(blocks, i, j, "valuable_einsteinium", root);
                    } else if (Math.random() < 0.05 && numberOfBlock(blocks, "valuable_goldium") < numberOfBlock(blocks, "Soil")- 3) {
                        blockCreator(blocks, i, j, "valuable_goldium", root);
                    } else if (Math.random() < 0.05 && numberOfBlock(blocks, "valuable_ironium") < numberOfBlock(blocks, "Soil")- 3){
                        blockCreator(blocks, i, j, "valuable_ironium", root);
                    } else if (Math.random() < 0.05 && numberOfBlock(blocks, "Lava") < numberOfBlock(blocks, "Soil") - 3){
                        blockCreator(blocks, i, j, "Lava", root);
                    } else if (Math.random() < 0.05 && numberOfBlock(blocks, "Obstacle") < numberOfBlock(blocks, "Soil") - 3) {
                        blockCreator(blocks, i, j, "Obstacle", root);
                    } else {
                        blockCreator(blocks, i, j, "Soil", root);
                    }
                }
            }
        }
    }
}






