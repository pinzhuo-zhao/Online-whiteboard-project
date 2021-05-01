package shapes;

import java.awt.*;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-01 16:48
 **/
public abstract class AbstractShape {
    int x1,y1,x2,y2;
    Color color;
    String text;

    public AbstractShape(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public AbstractShape(int x1, int y1, int x2, int y2, Color color, String text) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.text = text;
    }

    public abstract void draw(Graphics graphics);

}
