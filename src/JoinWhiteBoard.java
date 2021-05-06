import shapes.AbstractShape;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-06 00:16
 **/
public class JoinWhiteBoard {

    /** the flag to check if the client is connected to the server or not **/
    private static volatile Boolean connect = false;

    public static void main(String[] args) {
        Socket client = null;
        String usernameFromInput = null;
        try {
//            String ipFromInput = args[0];
//            String portFromInput = args[1];
//            String usernameFromInput = args[2];
            String ipFromInput = "localhost";
            String portFromInput = "9999";
            usernameFromInput = "User";
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
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        WhiteBoard whiteBoard = null;
        if (client != null){
            connect = true;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                DataInputStream dis = new DataInputStream(client.getInputStream());
                //sending username to the server

                dos.writeUTF(usernameFromInput);
                dos.writeBoolean(false);
                System.out.println("Please wait for the manager to confirm your access request");
                String permitMessage = dis.readUTF();
                System.out.println(permitMessage);
                if (permitMessage.equals("permitted")) {
                    whiteBoard = new WhiteBoard(oos);
                }
                else if (permitMessage.equals("rejected")) {
                    System.out.println("Sorry, you are not allowed to access");
                    System.exit(0);
                }

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
                }
                else if (readObject instanceof StringBuffer){
                    StringBuffer buffer = (StringBuffer) readObject;
                    String s = buffer.toString();
                    String[] split = s.split(",");
                    whiteBoard.getUserList().setListData(split);
                }
                else if (readObject instanceof String){
                    String message = ((String) readObject);
                    if (message.equals("quit")) {
                        JOptionPane.showMessageDialog(null,"The manager has disconnected, please close the app","Notification",JOptionPane.WARNING_MESSAGE);

                    }
                }


            }
            catch (StreamCorruptedException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }


    }
}
