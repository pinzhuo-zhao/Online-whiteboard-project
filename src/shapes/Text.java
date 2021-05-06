package shapes;

import java.awt.*;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-01 21:15
 **/
public class Text extends AbstractShape {

    public Text(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, x2, y2, color);
    }

    public Text(int x1, int y1, int x2, int y2, Color color, String text) {
        super(x1, y1, x2, y2, color, text);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.drawString(text, x1, y1);
    }
}
