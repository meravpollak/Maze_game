package Model;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Properties;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public MyModel() {}

    public void startServers(){}

    public void stopServers() {}

    private Maze maze;
    private Position characterPosition;
    private int characterPositionRow;
    private int characterPositionColumn;
    private Solution solution;
    Properties ourProp =new Properties();


    @Override
   // void generateMaze(int width, int height, java.lang.String generateCaind);
    public void generateMaze(int width, int height) {
        //Generate maze
        characterPositionRow=0;
        characterPositionColumn=0;

        threadPool.execute(() -> {

            File file = new File("config.properties");
            int numofThreads=2;
            try {
                FileReader read = new FileReader(file);
                ourProp.load(read);
                String algorithmSearch = ourProp.getProperty("algoSearch");
                String algotithmBuild = ourProp.getProperty("algoBuildMaze");
                numofThreads = Integer.parseInt(ourProp.getProperty("numThread"));
                read.close();
            } catch (FileNotFoundException var11) {
                var11.printStackTrace();
            } catch (IOException var12) {
                var12.printStackTrace();
            }
            //generateRandomMaze(width,height);
            try {
                IMazeGenerator mazeGenerator = ourProp.getMazegenerator();
                maze = mazeGenerator.generate(height/*rows*/,width/*columns*/);
                characterPosition=maze.getMyPos();
                characterPositionRow=maze.getStart().getCol();
                characterPositionColumn=maze.getStart().getRow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        });
    }


    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        int[][] tmpMaze = maze.getMaze();
        tmpMaze[maze.getStart().getRow()][maze.getStart().getCol()]=0;
        tmpMaze[maze.getEnd().getRow()][maze.getEnd().getCol()]=0;
        switch (movement) {
            case UP:
                if (characterPositionRow>0&&tmpMaze[characterPositionColumn][characterPositionRow-1]==0||(characterPositionColumn==maze.getStart().getCol()&&characterPositionRow==1))
                        characterPositionRow--;
                break;
            case DOWN:
                if (characterPositionRow<maze.getRows()-1&&tmpMaze[characterPositionColumn][characterPositionRow+1]==0||(characterPositionColumn==maze.getEnd().getCol()&&characterPositionRow==maze.getRows()-1))
                if (tmpMaze[characterPositionColumn][characterPositionRow+1]==0)
                    characterPositionRow++;
                break;
            case RIGHT:
                if (tmpMaze[characterPositionColumn+1][characterPositionRow]==0)
                    characterPositionColumn++;
                break;
            case LEFT:
                if (tmpMaze[characterPositionColumn-1][characterPositionRow]==0)
                    characterPositionColumn--;
                break;
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    @Override
    public void saveFile(File file) {
        try {
            //ImageIO.write(, "maze", file);

            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(file.getPath()+".maze"));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    @Override
    public void LoadMaze(File file) {
        byte[] savedMazeBytes = new byte[65536];
        //List<Byte> savedMazeBytes = new ArrayList<Byte>();

        try {
            InputStream in = new MyDecompressorInputStream(new FileInputStream(file.getPath()));
            //savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        maze = new Maze(savedMazeBytes);
        characterPositionRow=maze.getStart().getCol();
        characterPositionColumn=maze.getStart().getRow();
        setChanged();
        notifyObservers();
    }

    @Override
    public void solveMaze() {
            solution = this.ourProp.getalgoSearch().solve(new SearchableMaze(maze));
            setChanged();
            notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void showSulolution(Solution s) {

        for (int i=s.getSolutionSteps()-1;i>0;i--) {
            characterPositionColumn=((MazeState) s.getSolutionPath().get(i)).getPos().getRow();
            characterPositionRow=((MazeState) s.getSolutionPath().get(i)).getPos().getCol();
            setChanged();
            notifyObservers();
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //gc.fillRect(((MazeState) solution.getSolutionPath().get(i)).getPos().getRow() * cellHeight ,((MazeState) solution.getSolutionPath().get(i)).getPos().getCol() * cellWidth, cellHeight, cellWidth);


    }


    public void setProperties() {

    }

}
