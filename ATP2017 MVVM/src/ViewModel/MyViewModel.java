package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {

    private IModel model;

    //private int characterPositionRow; //For Binding
    //private int characterPositionColumn; //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            //characterPositionRow = model.getCharacterPositionRow();
            //characterPositionColumn = model.getCharacterPositionColumn();
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int width, int height){model.generateMaze(width, height);}

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() { return model.getCharacterPositionColumn(); }

    public void saveFile(File file) {
        model.saveFile(file);
    }

    public void LoadMaze(File file) {model.LoadMaze(file);}

    public void solveMaze() {model.solveMaze();}

    public Solution getSolution() {
        return model.getSolution();
    }

    public void showSulolution(Solution s) {
        model.showSulolution(s);
    }
}
