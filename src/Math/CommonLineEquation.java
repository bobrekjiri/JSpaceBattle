package Math;

import org.newdawn.slick.geom.Vector2f;

public final class CommonLineEquation {

    public float a;
    public float b;
    public float c;

    public CommonLineEquation(Vector2f point, Vector2f normalVector) {
        a = normalVector.x;
        b = normalVector.y;
        c = normalVector.x * point.x + normalVector.y * point.y;
    }

    public CommonLineEquation(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Vector2f commonPoint(CommonLineEquation other) {
        float y;

        // ax+by+c=0
        // dx+ey+f=0

        // solving system of two equations with two unknowns by addition method
        // we need to create -a in first index of second equation to use this
        // method
        // d * (-a / d) = -a

        // that will work only if d is not 0

        if (other.a != 0) {

            float coeficient = -a / other.a;
            CommonLineEquation other2 = other.multiply(coeficient);

            // x's are substracted so we can just leave y on left side and move
            // everything else to right
            // y = (-c-f)/(b+e)

            y = (-c - other2.c) / (b + other2.b);

        } else {
            // otherwise its bit simplier cause
            // 0x+by+c=0 => y = -c/b

            y = -other.c / other.b;
        }

        // when we have y getting x is simple
        // x = (-by-c)/a

        float x = (-b * y - c) / a;

        // and we are done
        return new Vector2f(x, y);

    }

    public CommonLineEquation multiply(float multiplier) {
        return new CommonLineEquation(this.a * multiplier, this.b * multiplier, this.c * multiplier);
    }

    @Override
    public String toString() {

        return String.format("%.2fx%+.2fy%+.2f", a, b, c);
    }
}
