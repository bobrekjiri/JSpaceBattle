package other;

import org.newdawn.slick.Font;

public class Notification {
    public NotificationType type;
    public String value;
    public boolean isVisible;
    public int timer;
    private static Font font;
    private float offsetX;

    public Notification(NotificationType type, String value) {
        this.type = type;
        this.value = value;
        isVisible = false;
        timer = 0;
        offsetX = font.getWidth(value) / 2f;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        Notification.font = font;
    }
}
