import java.awt.*;
import java.io.Serializable;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-01 14:30
 **/
public class ClientDrawMessage implements Serializable {
    private String shape;
    private Color color;
    private int x1,y1,x2,y2;
    private static final long serialVersionUID = 1L;

    public ClientDrawMessage() {
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public String getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
