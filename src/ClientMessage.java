import java.io.Serializable;

/**
 * @program: COMP90015A2
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2021-05-07 21:07
 **/
public class ClientMessage implements Serializable {
    String prefix;
    String message;
    private static final long serialVersionUID = 1L;

    public ClientMessage(String prefix, String message) {
        this.prefix = prefix;
        this.message = message;
    }

    public ClientMessage(String prefix) {
        this.prefix = prefix;
    }

    public ClientMessage() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "prefix='" + prefix + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
