import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Sender implements Runnable{
    private String server;
    private int source;
    private List<Socket> connected;

    public Sender(int source, String server, List<Socket> connected) {
        this.source = source;
        this.server = server;
        this.connected = connected;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = new Socket(server, 3000);
                System.out.println("connect to " + server);
                connected.add(socket);
                socket.getOutputStream().write(source);
                socket.getOutputStream().flush();
                System.out.println("send id " + source);
                break;
            } catch (IOException ignored) {
            }
        }
    }
}
