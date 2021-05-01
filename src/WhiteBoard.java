import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-04-30 21:22
 **/
public class WhiteBoard extends JPanel {
    private static volatile ClientDrawMessage message = new ClientDrawMessage();
    private ArrayList<AbstractShape> shapes = new ArrayList<>();

    public static ClientDrawMessage getMessage() {
        return message;
    }

    public WhiteBoard() {
        setLayout(new FlowLayout());
        setVisible(true);
        init();

    }

    private void init(){
        JFrame mainWindow = new JFrame("White Board");
        mainWindow.setSize(1100, 700);
        // 窗体设置居中
        mainWindow.setLocationRelativeTo(null);
        // 设置窗体关闭
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // 设置窗体边界布局
        mainWindow.setLayout(new BorderLayout());
        mainWindow.add(this, BorderLayout.CENTER);

        addShapePanel(mainWindow);
       /* JPanel colorSelection = new JPanel();
        colorSelection.setPreferredSize(new Dimension(100, 700));
        mainWindow.add(colorSelection, BorderLayout.WEST);*/


        this.setPreferredSize(new Dimension(1000, 700));
        this.setBackground(Color.gray);

        DrawActionListener listener = new DrawActionListener();
        mainWindow.setVisible(true);

        Graphics g = this.getGraphics();

        listener.setGraphics(g);
        listener.setShapes(shapes);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addKeyListener(listener);

    }

    private void addShapePanel(JFrame mainWindow) {
        //adding the panel for selecting the shape to draw
        JPanel shapeSelection = new JPanel();
        shapeSelection.setPreferredSize(new Dimension(100, 700));
        mainWindow.add(shapeSelection, BorderLayout.WEST);
        String[] shapeNames = {"Line","Circle","Oval","Rectangle","Text"};
        for (String shapeName : shapeNames){
            JButton button = new JButton(shapeName);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    WhiteBoard.message.setShape(shapeName);
                    requestFocus();
                }
            });
            shapeSelection.add(button);

        }
        JButton colorButton = new JButton("Colors");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(mainWindow, "Color Chooser", Color.BLACK);
                WhiteBoard.message.setColor(color);
            }
        });
        shapeSelection.add(colorButton);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (AbstractShape shape : shapes){
            shape.draw(g);
        }
    }


    public static void main(String[] args) {
        new WhiteBoard();
    }
}
