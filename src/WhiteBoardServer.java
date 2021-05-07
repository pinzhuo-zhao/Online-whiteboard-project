import shapes.AbstractShape;

import javax.swing.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 16:16
 **/
public class WhiteBoardServer {
    private static volatile Integer port =9999;
    private static volatile LinkedList<AbstractShape> storedShapes = new LinkedList<>();
    private static volatile LinkedList<User> users = new LinkedList<>();
    private static volatile LinkedList<String> allowedUsers = new LinkedList<>();
    private static volatile LinkedList<String> rejectedUsers = new LinkedList<>();

    private static volatile User manager;
    private static volatile AtomicInteger count = new AtomicInteger(1);
    private static Boolean launched = false;



    public static void setPort(String port){
        WhiteBoardServer.port = Integer.parseInt(port);


    }

    private static void serveWhiteBoard(Socket client){
        ObjectInputStream ois;
        ObjectOutputStream oos = null;
        DataInputStream dis;
        DataOutputStream dos = null;
        User currUser = null;
        try {
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            String username = dis.readUTF();

            currUser = new User(count.getAndIncrement(),username,client,ois,oos,dis,dos);

            boolean isManager = dis.readBoolean();

            if (isManager && manager == null){
                manager = currUser;
                allowedUsers.add(currUser.getId()+"."+currUser.getUsername());

            }

            else if (manager != null && !isManager){
                //if manager is not null, it means the board has been created
                // incoming users are allowed to enter the white board
                ClientMessage request = new ClientMessage("request", currUser.getId() + "." + currUser.getUsername());
                manager.getOos().writeUnshared(request);
//                manager.getOos().writeUnshared(currUser.getId()+"."+currUser.getUsername());
            }
            /**
             * made a loop to check if the access request has been permitted by the manager
             * if permitted, proceed
             */
            while (!allowedUsers.contains(currUser.getId()+"."+currUser.getUsername())){
                if (rejectedUsers.contains(currUser.getId()+"."+currUser.getUsername())){
                    currUser.getDos().writeUTF("rejected");

                }
            }
            if (!(currUser == manager)) {
                //send a message to the client so the user knows that the manager proved the request
                currUser.getDos().writeUTF("permitted");
            }
            //initializing the previously drawn contents and current user list for the new user
            users.add(currUser);
            currUser.getOos().writeObject(storedShapes);
            StringBuffer buffer = new StringBuffer();
            for (User user : users) {
                buffer.append(user.getId() + "." + user.getUsername());
                buffer.append(",");
            }
            for (User user : users) {
                user.getOos().writeUnshared(buffer);
            }

            while (true) {
                Object readObject = currUser.getOis().readObject();
                AbstractShape shape = null;
                if (readObject instanceof AbstractShape) {
                    shape = (AbstractShape) readObject;
                    storedShapes.add(shape);
                    for (User user : users) {
                        //make sure thread safety, write/readUnshared are used instead of write/readObject
                        user.getOos().writeUnshared(storedShapes);
                    }
                }else if (readObject instanceof ClientMessage){
                    ClientMessage response = (ClientMessage) readObject;
                    if (response.getPrefix().equals("accept")){
                        allowedUsers.add(response.getMessage());
                    } else if (response.getPrefix().equals("reject")){
                        rejectedUsers.add(response.getMessage());
                    } else if (response.getPrefix().equals("kick")){
                        int kickUser = Integer.parseInt(response.getMessage());
                        for (User user :users){
                            if (user.getId() == kickUser){
                                user.getOos().writeUnshared(new ClientMessage("quit","Kicked"));
                                user.getSocket().close();
//                                users.remove(user);
                            }
                        }
                    }

                }




            }


        } catch (IOException e) {
            //if the manager quited, send notification to all other users and quit the program
            users.remove(currUser);
            if (currUser == manager){
                for (User user : users){
                    try {
                        user.getOos().writeUnshared(new ClientMessage("quit","Manager Disconnected"));
                        user.getSocket().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                System.exit(0);
            }

            //when an IO exception occurred, it means the current client/socket has disconnected
            // so remove it from the list
            StringBuffer buffer = new StringBuffer();
            for (User user : users){
                buffer.append(user.getId() + "."+ user.getUsername());
                buffer.append(",");
            }
            for (User user : users){
                try {
                    user.getOos().writeUnshared(buffer);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
//        ServerGUI gui = new ServerGUI();
        ServerSocket socket = null;
        while (!launched) {
            if (port != null) {
                try {
                    socket = new ServerSocket(port);
                    launched = true;
//                    gui.getResponse().setText("White Board Server launched at port: " + port);
                    //prompt the user if the input port is already in use
                } catch (BindException e) {
//                    gui.getResponse().setText("Port already in use, try again");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            while (true) {
                Socket client = socket.accept();

                Thread whiteBoardThread = new Thread(() -> serveWhiteBoard(client));
//                Thread userListThread = new Thread(() -> serveUserList(client));
                whiteBoardThread.start();
//                userListThread.start();
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