import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 16:23
 **/
public class ServerGUI extends JFrame {
    private JTextField portTextField;
    private JLabel portLabel = new JLabel("Port：");
    private JTextArea response = new JTextArea(9,25);
    private JButton launchButton =new JButton("Launch");
    private Box textAreaBox;

    public JTextArea getResponse() {
        return response;
    }

    public ServerGUI() {
        setLayout(new FlowLayout());
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
    }

    private void init() {
        portTextField = new JTextField(10);
        JPanel panel=new JPanel();
        panel.add(portLabel);
        panel.add(portTextField);

        launchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String port = portTextField.getText().trim();
                //check the port number to see if it is out of bound
                String regex = "^-?[0-9]+$";
                if (!port.matches(regex)) {
                    response.setText("please enter a valid number for port");
                    return;
                }
                if (Integer.parseInt(port) >49151 || Integer.parseInt(port) < 1024 ){
                    response.setText("Invalid Port! It should be 1024 - 49151");
                    return;
                }

                WhiteBoardServer.setPort(port);

            }

        });
        add(panel);
        textAreaBox = Box.createVerticalBox();
        textAreaBox.add(response);
        textAreaBox.add(launchButton);
        add(textAreaBox);

    }




}
