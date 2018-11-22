package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;


public interface IModel {

    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    Maze getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();

    void saveFile(File file);

    void LoadMaze(File file);

    void solveMaze();

    Solution getSolution();

    void showSulolution(Solution s);
}
