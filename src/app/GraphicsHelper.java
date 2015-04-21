package app;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class GraphicsHelper {

    private static GraphicsHelper instance;

    public static GraphicsHelper getInstance() {
        if (instance == null) {
            instance = new GraphicsHelper();
        }
        return instance;
    }

    public void drawLine(Graphics g, Vector2f point1, Vector2f point2) {
        g.drawLine(point1.x, point1.y, point2.x, point2.y);
    }

}
