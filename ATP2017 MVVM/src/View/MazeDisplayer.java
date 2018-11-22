package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.swing.text.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends Canvas {

    private Maze maze;
    private algorithms.mazeGenerators.Position enter;
    private algorithms.mazeGenerators.Position exit;
    private int characterPositionRow;
    private int characterPositionColumn;
    private boolean printSoulution;
    private boolean gameOn;
    private Solution solution;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    double cellHeight;
    double cellWidth;
    boolean init=false;

    public void setMaze(Maze maze) {
        if (!init||maze!=this.maze){
                cellHeight = (getHeight()-100) / maze.getMaze().length;
                cellWidth = getWidth() / maze.getMaze()[0].length;
                init=true;
        }
        this.maze = maze;
        enter=  maze.getStart();
        exit = maze.getEnd();
        characterPositionColumn=enter.getCol();
        characterPositionRow=enter.getRow();
        gameOn=true;
        redraw();
    }
    public void setCellHeight(Number getHeight){
        if(init){
            cellHeight = ((double)getHeight-220) / maze.getMaze().length;
            redraw();
        }
    }
    public void setCellWidth(Number getWidth){
        if (init){
            cellWidth = ((double)getWidth-100) / maze.getMaze()[0].length;
            redraw();
        }
    }
    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    public void redraw() {
        if (maze != null) {

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, cellWidth,cellHeight );

                //Draw Maze
                for (int i = 0; i < maze.getMaze().length; i++) {
                    for (int j = 0; j < maze.getMaze()[i].length; j++) {
                        if (maze.getMaze()[i][j] == 1&&!(i==enter.getRow()&&j==enter.getCol())&&!(i==exit.getRow()&&j==exit.getCol())) {
                            //
                            gc.drawImage(wallImage,i * cellHeight , j * cellWidth, cellHeight, cellWidth);
                        }
                        else if((i==enter.getRow()&&j==enter.getCol())){
                            gc.setFill(Color.RED);
                            gc.fillRect(i * cellHeight ,j * cellWidth, cellHeight, cellWidth);
                            gc.setStroke(Color.WHITE);
                            gc.strokeText("Start",i * cellHeight ,j * cellWidth);
                        }
                        else if((i==exit.getRow()&&j==exit.getCol())){
                            gc.setFill(Color.GREEN);
                            gc.fillRect(i * cellHeight ,j * cellWidth, cellHeight, cellWidth);
                            gc.setStroke(Color.WHITE);
                            gc.strokeText("Exit", i * cellHeight ,j * cellWidth);
                        }else{
                            gc.setFill(Color.WHITE);
                            gc.fillRect(i * cellHeight ,j * cellWidth, cellHeight, cellWidth);
                        }


                    }
                }
                        if(printSoulution){
                            gc.setFill(Color.GRAY);

                            for (int i=1;i<solution.getSolutionSteps()-1;i++) {

                                gc.fillRect(((MazeState) solution.getSolutionPath().get(i)).getPos().getRow() * cellHeight ,((MazeState) solution.getSolutionPath().get(i)).getPos().getCol() * cellWidth, cellHeight, cellWidth);
                            }
                        }

                        gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(0 ,maze.getCols() * cellWidth, cellHeight*maze.getRows()+cellWidth*3, cellWidth*20);
                        gc.fillRect(maze.getRows() * cellHeight,0, cellHeight*20, cellWidth*maze.getCols()*3);

                if (characterPositionColumn==maze.getEnd().getRow()&&characterPositionRow==maze.getEnd().getCol()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Well Done! you have found your way out of the maze!");
                            alert.show();
                            gameOn=false;
                        }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }


    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public void drawSolution(Solution s) {
        solution=s;
        printSoulution=true;
        redraw();

    }

    public boolean isGameOn() {
        return gameOn;
    }
    public boolean isPrintSoulution() {
        return printSoulution;
    }
    public void setPrintSoulution(boolean b) {
        printSoulution=b;
    }
    //endregion

}
