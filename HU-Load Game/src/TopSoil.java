import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class TopSoil extends Soil{
    private String name;
    public TopSoil() {
        this.name = "TopSoil";
    }

    Image image = new Image("assets/underground/top_01.png");
    ImageView imageView = new ImageView(image);

    public ImageView getImageView() {
        return imageView;
    }
}
