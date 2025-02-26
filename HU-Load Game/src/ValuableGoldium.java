import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ValuableGoldium is a valuable block that can be collected by the player
 * It implements the Valuable interface which has the methods getValue and getWeight.
 * It has a value of 250 and a weight of 20.
 */
public class ValuableGoldium extends Block implements Valuable{
    public ValuableGoldium() {
        super("valuable_goldium");
    }

    /**
     * This method returns the value of the valuable block.
     * @return the value of the valuable block
     */
    @Override
    public int getValue() {
        return 250;
    }

    /**
     * This mehtod returns the weight of the valuable block.
     * @return the weight of the valuable block
     */
    @Override
    public int getWeight() {
        return 20;
    }
    Image image = new Image("assets/underground/valuable_goldium.png");// getting the image of the valuable block
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
