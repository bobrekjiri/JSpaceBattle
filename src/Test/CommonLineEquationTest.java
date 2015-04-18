package test;

import static org.junit.Assert.assertEquals;
import math.CommonLineEquation;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

public class CommonLineEquationTest {

    private CommonLineEquation equation1;
    private CommonLineEquation equation2;
    private CommonLineEquation equation3;

    @Before
    public void setUp() {
        equation1 = new CommonLineEquation(-1, 3, -5);
        equation2 = new CommonLineEquation(2, 3, -17);

        equation3 = new CommonLineEquation(new Vector2f(1, 2), new Vector2f(-1, 3));
    }

    @Test
    public void testCommonPoint() {
        Vector2f commonPoint = equation1.commonPoint(equation2);

        assertEquals("Unexpected common point X value", 4, commonPoint.x, 0.001);
        assertEquals("Unexpected common point Y value", 3, commonPoint.y, 0.001);

        assertEquals("Unexpected common point X value", 4, commonPoint.x, 0.001);
        assertEquals("Unexpected common point Y value", 3, commonPoint.y, 0.001);
    }

    @Test
    public void testPointAndVectorConstructor() {

        assertEquals("Unexpected A coeficient value", -1, equation3.a, 0.001);
        assertEquals("Unexpected B coeficient value", 3, equation3.b, 0.001);
        assertEquals("Unexpected C coeficient value", -5, equation3.c, 0.001);
    }

}
