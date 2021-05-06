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
                currUser.setAccess(true);

            }

            else if (manager != null && !isManager){
                //if manager is not null, it means the board has been created
                // incoming users are allowed to enter the white board
                manager.getOos().writeUnshared(username);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                String s = manager.getDis().readUTF();
                System.out.println(s);
                if (s.equals("permit")){
                    currUser.getDos().writeUTF("permitted");
                    currUser.setAccess(true);

                }
                else {
                    currUser.getDos().writeUTF("Sorry, you are not allowed to access");
                    currUser.getSocket().close();
                }
            }
            /**
             * 在此处给MANAGER发弹窗，如果允许了，再给currUser发第一条回复
             * 允许:这里直接往下走，拒绝:直接断开当前socket并回复弹窗
             */

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
                    }
                    storedShapes.add(shape);

                    for (User user : users) {
                        //make sure thread safety, write/readUnshared are used instead of write/readObject
                        user.getOos().writeUnshared(storedShapes);

                    }
                }


        } catch (IOException e) {
            /**
             * 如果退出用户是管理员的话，通知所有窗口可以关闭了，并关闭所有SOCKET
             */
            if (currUser == manager){
                for (User user : users){
                    try {
                        user.getSocket().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

            //when an IO exception occurred, it means the current client/socket has disconnected
            // so remove it from the list
            users.remove(currUser);
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

    private static void serveUserList(Socket client){
        DataInputStream in;
        DataOutputStream out = null;

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            String username = in.readUTF();
            User currUser = new User();
            currUser.setSocket(client);
            currUser.setId(count.getAndIncrement());
            currUser.setUsername(username);
            users.add(currUser);
            System.out.println(users);
        } catch (IOException e) {
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
                /**
                 * 怎么给MANAGER 的socket发送弹窗？
                 * 直接在此处判断MANAGER是否已经有了，如果有了，就在此处
                 */
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
