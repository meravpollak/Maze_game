package View;

import Server.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by MERAV on 26/06/2017.
 */

public class MazePropertiesController  implements Initializable {

    public javafx.scene.control.ComboBox ComboBox_CreateMaze;
    public javafx.scene.control.ComboBox ComboBox_serachAlgo;
    private View view;
    String creat="";
    String algoSearch="";

    public MazePropertiesController(){}

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        ComboBox_CreateMaze.getItems().addAll("MyMazeGenerator","SimpleMazeGenerator");
        ComboBox_CreateMaze.getSelectionModel().selectFirst();
        ComboBox_serachAlgo.getItems().addAll("DepthFirstSearch", "BreadthFirstSearch", "BestFirstSearch");
        ComboBox_serachAlgo.getSelectionModel().selectFirst();
    }

    public void setView(View view) {
        this.view = view;

    }

    public void setProperties(ActionEvent actionEvent) {
        ((Stage) ComboBox_CreateMaze.getScene().getWindow()).close();
        ((Stage) ComboBox_serachAlgo.getScene().getWindow()).close();

            try {
                java.util.Properties ourprop = new java.util.Properties();
                InputStream input = new FileInputStream("config.properties");
                ourprop.load(input);
                input.close();
                if(creat != "")
                    ourprop.setProperty("algoBuildMaze",this.creat);
                if(algoSearch != "")
                    ourprop.setProperty("algoSearch",this.algoSearch);
                OutputStream output = new FileOutputStream("config.properties");
                ourprop.store(output, null);
                ourprop.clear();
                output.close();

            } catch (FileNotFoundException var11) {
                var11.printStackTrace();
            } catch (IOException var12) {
                var12.printStackTrace();
            }


       // view.setPropertiesofMaze(creat,algoSearch);
    }

    public void comboBoxSearchAlgoSelctionChanged(ActionEvent actionEvent) {
        algoSearch=ComboBox_serachAlgo.getValue().toString();
    }
    public void comboBoxCreateSelctionChanged(ActionEvent actionEvent) {
        creat=ComboBox_CreateMaze.getValue().toString();
    }





}