import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Sender implements Runnable{
    private int port;
    private int source;
    private List<Socket> connected;

    public Sender(int source, int port, List<Socket> connected) {
        this.source = source;
        this.port = port;
        this.connected = connected;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = new Socket(InetAddress.getLoopbackAddress(), port);
                System.out.println("connect to " + port);
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
