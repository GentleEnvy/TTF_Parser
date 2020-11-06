package task_3.window;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import task_3.graphics.models.GraphicString;
import javafx.fxml.FXML;
import task_3.ttf_parser.Font;

import java.io.File;


public class Controller {
    public static final String DEFAULT_TTF = "TimesNewRoman";
    private static final String DEFAULT_TEXT = "Hello, World !";

    private final PixelPane pixelPane = new PixelPane();

    private Font font = new Font(new File(DEFAULT_TTF + ".ttf"));
    private final FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setInitialDirectory(new File("."));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "TTF files", "*.ttf"
        );
        fileChooser.getExtensionFilters().add(extFilter);
    }

    private GraphicString graphicString;
    private double angle;

    @FXML
    private HBox mainPane;
    @FXML
    private Button chooseFontButton;
    @FXML
    private TextField inputTextField;
    @FXML
    private Slider rotateSlider;
    @FXML
    private Label rotateInfoLabel;

    @FXML
    public void initialize() {
        mainPane.getChildren().add(pixelPane);
        angle = getAngle();
        pixelPane.render();
        chooseFontButton.setText(DEFAULT_TTF);

        inputTextField.textProperty().addListener(
                (__, oldString, newString) -> renderString(newString)
        );

        rotateSlider.valueProperty().addListener(
                (__, oldValue, newValue) -> {
                    angle = (double) newValue;
                    graphicString.setRotateOfDegrees(angle);
                    rotateInfoLabel.setText(String.valueOf(Math.round(angle)));
                    pixelPane.render();
                }
        );

        inputTextField.setText(DEFAULT_TEXT);
    }

    @FXML
    private void chooseFontEvent() {
        File ttfFile = fileChooser.showOpenDialog(new Stage());
        font = new Font(ttfFile);
        chooseFontButton.setText(ttfFile.getName().substring(
                0, ttfFile.getName().length() - 4)
        );
        renderString(graphicString.getString());
    }

    private double getAngle() {
        return rotateSlider.getValue();
    }

    private void renderString(String string) {
        pixelPane.removeGraphic(graphicString);
        graphicString = new GraphicString(string, font);
        graphicString.setRotateOfDegrees(angle);
        pixelPane.addGraphic(graphicString);
    }
}
