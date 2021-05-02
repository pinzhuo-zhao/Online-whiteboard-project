import shapes.AbstractShape;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 16:16
 **/
public class WhiteBoardServer {
    private static volatile Integer port;
    private static volatile ArrayList<AbstractShape> storedShapes = new ArrayList<>();
    private static volatile ArrayList<Socket> clients = new ArrayList<>();
    private static volatile ArrayList<User> users = new ArrayList<>();
    private static Boolean launched = false;
    private static Boolean whiteboardCreated = false;


    public static void setPort(String port){
        WhiteBoardServer.port = Integer.parseInt(port);


    }

    private static void serveWhiteBoard(Socket client){
        ObjectInputStream in;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject(storedShapes);
            while (true){
                Object readObject = in.readObject();
                AbstractShape shape = null;
                if (readObject instanceof AbstractShape){
                    shape = (AbstractShape)readObject;
                }
                storedShapes.add(shape);

//                out.writeObject(storedShapes);
                for (Socket mySocket : clients){
                    out = new ObjectOutputStream(mySocket.getOutputStream());
                    out.writeObject(storedShapes);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerGUI gui = new ServerGUI();
        ServerSocket socket = null;
        while (!launched) {
            if (port != null) {
                try {
                    socket = new ServerSocket(port);
                    launched = true;
                    gui.getResponse().setText("White Board Server launched at port: " + port);
                    //prompt the user if the input port is already in use
                } catch (BindException e) {
                    gui.getResponse().setText("Port already in use, try again");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            while (true) {
                Socket client = socket.accept();
                clients.add(client);
                Thread whiteBoardThread = new Thread(() -> serveWhiteBoard(client));
                whiteBoardThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
