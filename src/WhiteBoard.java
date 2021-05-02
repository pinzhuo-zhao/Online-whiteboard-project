import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-04-30 21:22
 **/
public class WhiteBoard extends JPanel {
    private volatile Color color;
    private volatile String shape;
    private ArrayList<AbstractShape> shapes = new ArrayList<>();
    private ObjectOutputStream out;


    public Color getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public void setShapes(ArrayList<AbstractShape> shapes) {
        this.shapes = shapes;
    }

    public WhiteBoard(ObjectOutputStream out) {
        this.out = out;
        setLayout(new FlowLayout());
        setVisible(true);
        init();

    }
    public WhiteBoard() {
        setLayout(new FlowLayout());
        setVisible(true);
        init();

    }

    private void init() {
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

        Graphics graphics = this.getGraphics();
        listener.setGraphics(graphics);
        listener.setShapes(shapes);
        listener.setOut(out);
        listener.setWhiteBoard(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addKeyListener(listener);

    }

    private void addShapePanel(JFrame mainWindow) {
        //adding the panel for selecting the shape to draw
        JPanel shapeSelection = new JPanel();
        shapeSelection.setPreferredSize(new Dimension(100, 700));
        mainWindow.add(shapeSelection, BorderLayout.WEST);
        String[] shapeNames = {"Line", "Circle", "Oval", "Rectangle", "Text"};
        for (String shapeName : shapeNames) {
            JButton button = new JButton(shapeName);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    shape = shapeName;
                    requestFocus();
                }
            });
            shapeSelection.add(button);

        }
        JButton colorButton = new JButton("Colors");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(mainWindow, "Color Chooser", Color.BLACK);
                color = selectedColor;
            }
        });
        shapeSelection.add(colorButton);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (AbstractShape shape : shapes) {
            shape.draw(g);
        }
    }


}
