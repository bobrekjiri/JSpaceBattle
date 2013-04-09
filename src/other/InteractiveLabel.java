package other;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class InteractiveLabel extends Label {
    private Color mouseOver, disabled;
    private Rectangle rectangle;
    private boolean isEnabled, isMouseOver;

    public InteractiveLabel(String text, Point position, Font font, Rectangle rectangle,
            boolean isShadowed, boolean isCentered) {
        super(text, position, font, isShadowed);
        isEnabled = true;
        mouseOver = Color.blue;
        disabled = Color.gray;

        if (isCentered) {
            position.x -= rectangle.width / 2;
            position.y -= rectangle.height / 2;
        }
        this.rectangle = new Rectangle(position.x, position.y, rectangle.width, rectangle.height);

    }

    public void setColors(Color normal, Color shadow, Color mouseOver, Color disabled) {
        super.setColors(normal, shadow);
        this.mouseOver = mouseOver;
        this.disabled = disabled;
    }

    public void render(Graphics g) {
        g.setFont(font);
        g.setColor((!isEnabled) ? disabled : ((isMouseOver) ? mouseOver : normal));
        g.drawString(text, position.x, position.y);
    }

    public void setEnabled(boolean value) {
        isEnabled = value;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsMouseOver(Point mouse) {
        isMouseOver = rectangle.contains(mouse) && isEnabled;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }
}
