import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ValuableIronium is a valuable block that can be collected by the player
 * It implements the Valuable interface which has the methods getValue and getWeight.
 * It has a value of 30 and a weight of 10.
 */
public class ValuableIronium extends Block implements Valuable {
    public ValuableIronium() {
        super("valuable_ironium");
    }

    /**
     * This method returns the value of the valuable block.
     * @return the value of the valuable block
     */
    @Override
    public int getValue() {
        return 30;
    }

    /**
     * This mehtod returns the weight of the valuable block.
     * @return the weight of the valuable block
     */
    @Override
    public int getWeight() {
        return 10;
    }
    Image image = new Image("assets/underground/valuable_ironium.png");// getting the image of the valuable block
    ImageView imageView = new ImageView(image);// getting the image view of the valuable block

    /**
     * Returns the image view of the valuable block.
     * @return the image view of the valuable block
     */
    @Override
    public ImageView getImageView() {
        return imageView;
    }

}
