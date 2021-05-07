import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-04-30 21:22
 **/
public class WhiteBoard extends JPanel {
    private String title;
    private volatile Color color;
    private volatile String shapeType;
    private LinkedList<AbstractShape> shapes = new LinkedList<>();
    private ObjectOutputStream out;
    private JList userList = new JList();

    public void setUserList(JList userList) {
        this.userList = userList;
    }

    public JList getUserList() {
        return userList;
    }

    public Color getColor() {
        return color;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapes(LinkedList<AbstractShape> shapes) {
        this.shapes = shapes;
    }

    public LinkedList<AbstractShape> getShapes() {
        return shapes;
    }

    public WhiteBoard(ObjectOutputStream out,String title) {
        this.out = out;
        this.title = title;
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
        JFrame mainWindow = new JFrame(title);
        mainWindow.setSize(800, 600);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setLayout(new BorderLayout());
        addShapePanel(mainWindow);
        addUserList(mainWindow);
        //adding the white board panel to the main window
        mainWindow.add(this, BorderLayout.CENTER);


        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.lightGray);

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
//        shapeSelection.setBackground(Color.white);
        shapeSelection.setPreferredSize(new Dimension(100, 600));
        String[] shapeNames = {"Line", "Circle", "Oval", "Rectangle", "Text"};
        for (String shapeName : shapeNames) {
            JButton button = new JButton(shapeName);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    shapeType = shapeName;
                    requestFocus();
                }
            });
            shapeSelection.add(button);

        }
        //provide a JColorChooser for users to select color for drawing shapes
        JButton colorButton = new JButton("Colors");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(mainWindow, "Color Chooser", Color.BLACK);
                color = selectedColor;
            }
        });
        shapeSelection.add(colorButton);
        mainWindow.add(shapeSelection, BorderLayout.WEST);
    }

    private void addUserList(JFrame mainWindow){
        JPanel userListPanel = new JPanel();
        userListPanel.setPreferredSize(new Dimension(100, 600));
        JLabel users = new JLabel("Online Users");
        userListPanel.add(users, BorderLayout.NORTH);
        userListPanel.add(userList, BorderLayout.SOUTH);
        mainWindow.add(userListPanel, BorderLayout.EAST);


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (AbstractShape shape : shapes) {
            shape.draw(g);
        }
    }
    public void paintSingleShape(Graphics g, AbstractShape shape) {
        shape.draw(g);
    }

    public static void main(String[] args) {
        new WhiteBoard();
    }

}
