import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 19:55
 **/
public class User implements Serializable {
    private int id;
    private String username;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private DataOutputStream dos;
    private static final long serialVersionUID = 1L;

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public User() {
    }

    public User(int id, String username, Socket socket, ObjectInputStream ois, ObjectOutputStream oos, DataInputStream dis, DataOutputStream dos) {
        this.id = id;
        this.username = username;
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.dis = dis;
        this.dos = dos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", socket=" + socket +
                '}';
    }
}
