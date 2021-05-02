import shapes.AbstractShape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
        User currUser = new User();
        try {
//            String ipFromInput = args[0];
//            String portFromInput = args[1];
//            String usernameFromInput = args[2];
            String ipFromInput = "localhost";
            String portFromInput = "9999";
            String usernameFromInput = "Manager";
            int port = Integer.parseInt(portFromInput);
            client = new Socket(ipFromInput,port);
            currUser.setUsername(usernameFromInput);

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
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        WhiteBoard whiteBoard = null;
        if (client != null){
            connect = true;
            try {
                out = new ObjectOutputStream(client.getOutputStream());

                whiteBoard = new WhiteBoard(out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //每次鼠标操作都执行一次方法
        }

        while (connect){
            try {
                in = new ObjectInputStream(client.getInputStream());
                Object readObject = in.readObject();
                ArrayList<AbstractShape> shapes = (ArrayList<AbstractShape>)readObject;
                whiteBoard.setShapes(shapes);
                whiteBoard.paint(whiteBoard.getGraphics());
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
