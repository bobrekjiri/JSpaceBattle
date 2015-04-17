package other;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class InteractiveLabel extends Label {
    private Point originalPosition;
    private Color mouseOver, disabled;
    private Rectangle rectangle;
    private boolean isEnabled, isMouseOver, isCentered;

    public InteractiveLabel(String text, Point position, Font font, boolean isShadowed,
            boolean isCentered) {
        super(text, position, font, isShadowed);
        isEnabled = true;
        mouseOver = Color.yellow;
        disabled = Color.gray;
        this.isCentered = isCentered;

        int width = font.getWidth(text);
        int height = font.getHeight(text);

        originalPosition = new Point(position);
        this.position.x = originalPosition.x;
        this.position.y = originalPosition.y;
        if (isCentered) {
            this.position.x = originalPosition.x - width / 2;
            this.position.y = originalPosition.y - height / 2;
        }
        this.rectangle = new Rectangle(this.position.x, this.position.y, width, height);

    }

    public void setColors(Color normal, Color shadow, Color mouseOver, Color disabled) {
        super.setColors(normal, shadow);
        this.mouseOver = mouseOver;
        this.disabled = disabled;
    }

    public void setDisabledColor(Color disabled) {
        this.disabled = disabled;
    }

    @Override
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

    @Override
    public void setText(String text) {
        super.setText(text);

        int width = font.getWidth(text);
        int height = font.getHeight(text);

        position.x = originalPosition.x;
        position.y = originalPosition.y;

        if (isCentered) {
            position.x = originalPosition.x - width / 2;
            position.y = originalPosition.y - height / 2;
        }
        rectangle = new Rectangle(position.x, position.y, width, height);
    }
}
