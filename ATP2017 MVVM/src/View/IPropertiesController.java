package View;

import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;

public interface IPropertiesController  extends Observable {
    public void drawOnCanvas(Canvas canvas_walltype, String s);
    public void drowCharacterOnCanvas(Canvas canvas_characterType, String s);
}
