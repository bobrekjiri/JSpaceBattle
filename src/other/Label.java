package other;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class Label {
    protected Font font;
    protected Color normal, shadow;
    protected Point position;
    protected String text;
    protected boolean isShadowed;

    public Label(String text, Point position, Font font) {
        this(text, position, font, false);
    }

    public Label(String text, Point position, Font font, boolean isShadowed) {
        this.text = text;
        this.position = position;
        this.font = font;
        this.isShadowed = isShadowed;
        normal = Color.red;
        shadow = Color.darkGray;
    }

    public void setColors(Color normal) {
        this.normal = normal;
    }

    public void setColors(Color normal, Color shadow) {
        this.normal = normal;
        this.shadow = shadow;
    }

    public void render(Graphics g) {
        g.setFont(font);
        if (isShadowed) {
            g.setColor(shadow);
            g.drawString(text, position.x + 2, position.y + 2);
        }
        g.setColor(normal);
        g.drawString(text, position.x, position.y);
    }

    public void setText(String text) {
        this.text = text;
    }
}
