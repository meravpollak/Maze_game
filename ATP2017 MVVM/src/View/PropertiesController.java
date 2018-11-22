package View;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Noah on 6/21/2017.
 */
public class PropertiesController implements Initializable{
    public Canvas canvas_characterType;
    public ComboBox ComboBox_character;
    private View view;
    public javafx.scene.control.ComboBox ComboBox_Wall;
    public Canvas canvas_walltype;
    String wallPath;
    String character;

    public PropertiesController() {

    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        ComboBox_Wall.getItems().addAll("wall1","wall2","wall3","wall4");
        ComboBox_Wall.getSelectionModel().selectFirst();
        ComboBox_character.getItems().addAll("character1","character2","character3","character4");
        ComboBox_character.getSelectionModel().selectFirst();
        drawOnCanvas(canvas_walltype,ComboBox_Wall.getValue().toString());
        drowCharacterOnCanvas(canvas_characterType,ComboBox_character.getValue().toString());

    }

    public void drawOnCanvas(Canvas canvas_walltype, String s) {
        try {
            String str ="resources/Images/"+s+".jpg";
            canvas_walltype.getGraphicsContext2D().drawImage(new Image(new FileInputStream("resources/Images/white.jpg")),0,0,100,100);
            canvas_walltype.getGraphicsContext2D().drawImage(new Image(new FileInputStream(str)),0,0,100,100);
            wallPath=str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void drowCharacterOnCanvas(Canvas canvas_characterType,String s){
        try {
            String str ="resources/Images/"+s+".jpg";
            canvas_characterType.getGraphicsContext2D().drawImage(new Image(new FileInputStream("resources/Images/white.jpg")),0,0,100,100);
            canvas_characterType.getGraphicsContext2D().drawImage(new Image(new FileInputStream(str)),0,0,100,100);
            character=str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void comboBoxWallSelctionChanged(ActionEvent actionEvent) {
        drawOnCanvas(canvas_walltype,ComboBox_Wall.getValue().toString());
    }


    public void setProperties(ActionEvent actionEvent) {
        ((Stage) canvas_walltype.getScene().getWindow()).close();
        ((Stage) ComboBox_character.getScene().getWindow()).close();
        view.setProperties(wallPath,character);
    }


    public void setView(View view) {
        this.view = view;

    }

    public void comboBoxCharacterSelctionChanged(ActionEvent actionEvent) {
        drowCharacterOnCanvas(canvas_characterType,ComboBox_character.getValue().toString());
    }
}
