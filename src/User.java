import java.net.Socket;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-02 19:55
 **/
public class User {
    private int id;
    private String username;
    private Socket client;

    public User() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Socket getClient() {
        return client;
    }
}
