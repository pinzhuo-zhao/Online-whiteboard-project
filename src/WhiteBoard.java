import shapes.AbstractShape;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;

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
    private JTextField chatInput;
    private JTextArea chatArea;

    public JTextArea getChatArea() {
        return chatArea;
    }

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
        mainWindow.setSize(1000, 900);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        mainWindow.setLayout(new BorderLayout());
        addShapePanel(mainWindow);
        addUserList(mainWindow);
        addChatPanel(mainWindow);
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

    private void addChatPanel(JFrame mainWindow){
        JPanel chatPanel = new JPanel();
        chatPanel.setPreferredSize(new Dimension(600, 300));
        JLabel chatLabel = new JLabel("Chat Window");
        chatArea = new JTextArea(10,25);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatInput = new JTextField(20);
        JButton sendButton = new JButton("Send");
        Box inputBox = Box.createHorizontalBox();
        Box messageBox = Box.createVerticalBox();

        inputBox.add(chatInput);
        inputBox.add(sendButton);
        messageBox.add(chatLabel);
        messageBox.add(chatScrollPane);
        chatPanel.add(messageBox);
        chatPanel.add(inputBox);


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = chatInput.getText().trim();
                try {
                    out.writeUnshared(new ClientMessage("chat",input));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                chatInput.setText("");


            }
        });
        mainWindow.add(chatPanel, BorderLayout.SOUTH);



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
