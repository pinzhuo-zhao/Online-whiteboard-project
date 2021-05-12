import shapes.AbstractShape;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 17:21
 **/
public class CreateWhiteBoard {

    private static ObjectOutputStream oos = null;
    private static  ObjectInputStream ois  = null;
    private static DataOutputStream dos  = null;
    private static DataInputStream dis   = null;

    /** the flag to check if the client is connected to the server or not **/
    private static volatile Boolean connect = false;

    public static void main(String[] args) {
        Socket client = null;
        String usernameFromInput = null;
        try {
            String ipFromInput = args[0];
            String portFromInput = args[1];
            usernameFromInput = args[2];
//            String ipFromInput = "localhost";
//            String portFromInput = "9999";
//            usernameFromInput = "Manager";
            int port = Integer.parseInt(portFromInput);
            client = new Socket(ipFromInput,port);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number for port");
//            e.printStackTrace();
        } catch (UnknownHostException | ConnectException e) {
            System.out.println("No server found! Please check your entries for IP and port again");
//            e.printStackTrace();

        } catch (IllegalArgumentException e){
            System.out.println("Port out of range, it should be 1024 - 49151, please try again");
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        WhiteBoard whiteBoard = null;
        if (client != null){
            connect = true;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());

                whiteBoard = new WhiteBoard(oos,"Manager-" + usernameFromInput);
                //sending username to the server

                dos.writeUTF(usernameFromInput);
                //send an identifier to make server know that this socket is the manager
                dos.writeBoolean(true);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        while (connect){
            try {
                Object readObject = ois.readUnshared();
                if (readObject instanceof LinkedList) {
                    LinkedList<AbstractShape>  shapes = (LinkedList) readObject;
                    whiteBoard.setShapes(shapes);
                    whiteBoard.paint(whiteBoard.getGraphics());
                } else if (readObject instanceof StringBuffer){
                    StringBuffer buffer = (StringBuffer) readObject;
                    String s = buffer.toString();
                    String[] split = s.split(",");
                    /**
                     * set the ActionListener for the user list to implement the kick function
                     *
                     */
                    JList userList = whiteBoard.getUserList();
                    userList.setListData(split);
                    userList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String selectedUser = (String) userList.getSelectedValue();
                            String[] idAndName = selectedUser.split("\\.");
                            String id = idAndName[0];
                            userList.clearSelection();
                            if (!id.equals("1")) {
                                int i = JOptionPane.showConfirmDialog(null, "Confirm to Kick " + selectedUser + " ?", "Kick User", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (i == JOptionPane.YES_OPTION) {
                                    ClientMessage kick = new ClientMessage("kick",id);
                                    try {
                                        oos.writeObject(kick);
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                } else {

                                }
                            }
                        }



                    });

                } else if (readObject instanceof ClientMessage){
                    ClientMessage clientMessage = (ClientMessage) readObject;
                    if (clientMessage.getPrefix().equals("request")) {
                        int i = JOptionPane.showConfirmDialog(null, clientMessage.getMessage() + " wants to share your white board.", "New User", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (i == JOptionPane.YES_OPTION) {
                            ClientMessage accept = new ClientMessage("accept", clientMessage.getMessage());
                            oos.writeObject(accept);
                        } else {
                            ClientMessage reject = new ClientMessage("reject", clientMessage.getMessage());
                            oos.writeObject(reject);
                        }
                    } else if (clientMessage.getPrefix().equals("chat")){
                        whiteBoard.getChatArea().append("\n");
                        whiteBoard.getChatArea().append(clientMessage.getMessage());
                    }
                }


            }
            catch (SocketException e) {
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }


    }

}