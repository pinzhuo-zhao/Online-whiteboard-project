import shapes.*;
import shapes.Rectangle;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-04-30 21:45
 **/
public class DrawActionListener extends MouseAdapter implements KeyListener {
    private int x1,y1,x2,y2;
    private Graphics graphics;
    private String text;
    private WhiteBoard whiteBoard;
    private LinkedList<AbstractShape> shapes;
    private ObjectOutputStream out;

    public void setWhiteBoard(WhiteBoard whiteBoard) {
        this.whiteBoard = whiteBoard;
    }

    public void setShapes(LinkedList<AbstractShape> shapes) {
        this.shapes = shapes;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
         x1 = e.getX();
         y1 = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
         x1 = e.getX();
         y1= e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
//        graphics.drawLine(x1, y1, x2, y2);
        graphics.setColor(whiteBoard.getColor());
        try {
            String shape = whiteBoard.getShapeType();
            if (shape.equals("Line")) {
                graphics.drawLine(x1, y1, x2, y2);
                Line line = new Line(x1, y1, x2, y2, whiteBoard.getColor());
                shapes.add(line);
                out.writeObject(line);
            } else if (shape.equals("Rectangle")) {
                graphics.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                Rectangle rectangle = new Rectangle(x1, y1, x2, y2, whiteBoard.getColor());
                shapes.add(rectangle);
                out.writeObject(rectangle);
            } else if (shape.equals("Oval")) {
                graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                Oval oval = new Oval(x1, y1, x2, y2, whiteBoard.getColor());
                shapes.add(oval);
                out.writeObject(oval);
            } else if (shape.equals("Circle")) {
                graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)),Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)));
                Circle circle = new Circle(x1, y1, x2, y2, whiteBoard.getColor());
                shapes.add(circle);
                out.writeObject(circle);
            }
        }catch(NullPointerException ex){
//            JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    /*    x2 = e.getX();
        y2 = e.getY();
//        graphics.drawLine(x1, y1, x2, y2);
        graphics.setColor(whiteBoard.getColor());
        try {
            String shape = whiteBoard.getShapeType();
            if (shape.equals("Line")) {
                whiteBoard.paint(graphics);
                graphics.drawLine(x1, y1, x2, y2);




            } else if (shape.equals("Rectangle")) {
                whiteBoard.paint(graphics);
                graphics.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));


            } else if (shape.equals("Oval")) {
                whiteBoard.paint(graphics);
                graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));


            } else if (shape.equals("Circle")) {
                whiteBoard.paint(graphics);
                //graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)),Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)));
                graphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(x2 - x1));
            }
        }catch(NullPointerException ex){
//            JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE);*/
    //    }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        text = String.valueOf(e.getKeyChar());
        graphics.setColor(whiteBoard.getColor());
        String shape = whiteBoard.getShapeType();
        if (shape.equals("Text")) {
//            graphics.drawString(text, x1, y1);
            Text inputText = new Text(x1,y1,x2,y2,whiteBoard.getColor(),text);
            shapes.add(inputText);
            try {
                out.writeObject(inputText);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            x1 += 10;
        }


    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
