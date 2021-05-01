package shapes;

import java.awt.*;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-01 21:13
 **/
public class Oval extends AbstractShape {
    public Oval(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, x2, y2, color);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }
}
