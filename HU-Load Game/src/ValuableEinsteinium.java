import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ValuableEinsteinium is a valuable block that can be collected by the player
 * It implements the Valuable interface which has the methods getValue and getWeight.
 * It has a value of 2000 and a weight of 40.
 */
public class ValuableEinsteinium extends Block implements Valuable {
    public ValuableEinsteinium() {
        super("valuable_einsteinium");
    }

    /**
     * This method returns the value of the valuable block.
     * @return the value of the valuable block
     */
    @Override
    public int getValue() {
        return 2000;
    }

    /**
     * This mehtod returns the weight of the valuable block.
     * @return the weight of the valuable block
     */
    @Override
    public int getWeight() {
        return 40;
    }


    Image image = new Image("assets/underground/valuable_einsteinium.png"); // getting the image of the valuable block
    ImageView imageView = new ImageView(image); // getting the image view of the valuable block

    /**
     * Returns the image view of the valuable block.
     * @return the image view of the valuable block
     */
    @Override
    public ImageView getImageView() {
        return imageView;
    }
}

