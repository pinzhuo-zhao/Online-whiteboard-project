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
 * @create: 2021-05-02 17:21
 **/
public class CreateWhiteBoard {



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
            usernameFromInput = "Manager";
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
        ObjectInputStream ois  = null;
        DataOutputStream dos  = null;
        DataInputStream dis   = null;

        WhiteBoard whiteBoard = null;
        if (client != null){
            connect = true;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());

                whiteBoard = new WhiteBoard(oos);
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
                     * 要做踢人功能的话，只在这里加listener,发消息给Server找到和ID对应的USER的socket,然后CLOSE
                     */
                    whiteBoard.getUserList().setListData(split);
                } else if (readObject instanceof String){
                    String requestedUsername = (String) readObject;
                    int i = JOptionPane.showConfirmDialog(null, requestedUsername + " wants to share your white board.", "New User", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (i == JOptionPane.YES_OPTION){
                        oos.writeUnshared("permit");
                    }
                    else{
                        oos.writeUnshared("reject");
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
 /*   private static void getWhiteBoardMessage(WhiteBoard whiteBoard, Socket client){
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (connect){
            try {
                Object readObject = in.readObject();
                if (readObject instanceof LinkedList) {
                    LinkedList<AbstractShape> shapes = (LinkedList<AbstractShape>) readObject;
                    whiteBoard.setShapes(shapes);
                    whiteBoard.paint(whiteBoard.getGraphics());
                }
                else if (readObject instanceof StringBuffer){
                    StringBuffer buffer = (StringBuffer) readObject;
                    System.out.println(buffer.toString());
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
    }*/
}
