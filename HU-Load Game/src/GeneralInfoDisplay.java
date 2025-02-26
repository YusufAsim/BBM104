import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

/**
 * This class is responsible for displaying the general information of the Mining game on screen.
 */
public class GeneralInfoDisplay {
    // attributes are final because they are not supposed to be changed internally
    private final Label moneyLabel;
    private final Label fuelLabel;
    private final Label storageLabel;
    private final VBox vbox;

    public GeneralInfoDisplay() {
        moneyLabel = new Label();
        fuelLabel = new Label();
        storageLabel = new Label();

        Label[] labels = {moneyLabel, fuelLabel, storageLabel};
        for (Label label : labels) {
            label.setFont(new Font("Arial", 30));
            label.setTextFill(Color.WHITE);
        }

        vbox = new VBox();
        vbox.getChildren().addAll(moneyLabel, fuelLabel, storageLabel);
    }

    public VBox getVbox() {
        return vbox;
    }

    /**
     * This method updates the labels with the current values of the mine machine.
     * @param machine the mine machine object that contains the current values.
     */
    public void updateLabelsCurrent(MineMachine machine){
        fuelLabel.setText(String.format("fuel: %.3f",machine.getFuel()));
        storageLabel.setText("haul: " + (int) machine.getStorage());
        moneyLabel.setText("money: " + machine.getMoney());
    }

}