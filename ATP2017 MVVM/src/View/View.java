package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View implements Observer, IView {

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button btn_showSulotion;
    public javafx.scene.control.TextField txtfld_rowsChar;
    public javafx.scene.control.TextField txtfld_columnsChar;
    boolean init=false;
    java.lang.String generateCaind="MyMazeGenerator";
    java.lang.String algoCaind="DepthFirstSearch";

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == myViewModel) {
            displayMaze(myViewModel.getMaze());
            btn_generateMaze.setDisable(false);
        }
    }

    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = myViewModel.getCharacterPositionRow();
        int characterPositionColumn = myViewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
//        CharacterRow.set(characterPositionRow + "");
//        CharacterColumn.set(characterPositionColumn + "");
    }

    public void generateMaze() {
            int heigth = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());
            btn_generateMaze.setDisable(true);
            myViewModel.generateMaze(width, heigth);
            mazeDisplayer.setPrintSoulution(false);
            btn_solveMaze.setDisable(false);
            btn_showSulotion.setDisable(false);
            showPositionRow();
            showPositionColumn();

    }

    public void solveMaze() {
        threadPool.execute(() -> {
            myViewModel.solveMaze();
            Solution s = myViewModel.getSolution();
            //s.printSolutionPath();
            mazeDisplayer.drawSolution(s);
        });
    }
    public void showSulolution(ActionEvent actionEvent) {
        threadPool.execute(() -> {
            myViewModel.solveMaze();
            Solution s = myViewModel.getSolution();
            //s.printSolutionPath();
            myViewModel.showSulolution(s);
        });
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        if (mazeDisplayer.isGameOn()) {
            myViewModel.moveCharacter(keyEvent.getCode());
            showPositionRow();
            showPositionColumn();

        }
            keyEvent.consume();
    }

    //region String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();

    public StringProperty CharacterColumn = new SimpleStringProperty();

    public String getCharacterRow() {
        return CharacterRow.get();
    }

    public StringProperty characterRowProperty() {
        return CharacterRow;
    }

    public String getCharacterColumn() {
        return CharacterColumn.get();
    }

    public StringProperty characterColumnProperty() {
        return CharacterColumn;
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.setCellHeight(newSceneWidth);
                mazeDisplayer.setWidth((Double) newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.setCellWidth(newSceneHeight);
                mazeDisplayer.setHeight((Double) newSceneHeight-10);

            }
        });

    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void SaveMaze(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        //System.out.println(pic.getId());
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            myViewModel.saveFile(file);
        }
    }

    public void LoadMaze(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file.getName().substring(file.getName().length()-4).equals("maze")) {
            myViewModel.LoadMaze(file);
        }
        else{
            showAlert("file type must be 'maze'");
        }
    }
    public void Properties(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Board Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL); //Lock the window until it closes
            stage.show();
            PropertiesController prop = fxmlLoader.getController();
            prop.setView(this);


        } catch (Exception e) {

        }
    }

    public void setProperties(String wellPath,String character) {
        mazeDisplayer.setImageFileNameWall(wellPath);
        mazeDisplayer.setImageFileNameCharacter(character);
        mazeDisplayer.redraw();
    }


    public void showPositionRow( ){
        int row= myViewModel.getCharacterPositionRow();
        txtfld_rowsChar.setText(String.valueOf(row));
    }

    public void showPositionColumn( ){
        int column= myViewModel.getCharacterPositionColumn();
        txtfld_columnsChar.setText(String.valueOf(column));
    }

    public void MazeProperties(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Maze Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("MazeProperties.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL); //Lock the window until it closes
            stage.show();
            MazePropertiesController prop = fxmlLoader.getController();
            prop.setView(this);

        } catch (Exception e) {

        }
    }

    public void Instructions(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Instructions");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Instructions.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }
    public void Exit(){

    }


    //endregion

}
