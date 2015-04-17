package Test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import Math.CommonLineEquation;

public class CommonLineEquationTest {

    private CommonLineEquation equation1;
    private CommonLineEquation equation2;

    @Before
    public void setUp() {
        equation1 = new CommonLineEquation(-1, 3, -5);
        equation2 = new CommonLineEquation(2, 3, -17);
    }

    @Test
    public void test() {
        Vector2f commonPoint = equation1.commonPoint(equation2);

        assertEquals("Expected X value is 4", 4, commonPoint.x, 0.001);
        assertEquals("Expected Y value is 3", 3, commonPoint.y, 0.001);
    }

}