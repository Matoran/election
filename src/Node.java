import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
    public static final int OK = 254;
    public static final int NOT_OK = 253;
    private ArrayList<Integer> neighbors = new ArrayList<>();
    private ArrayList<Integer> tried = new ArrayList<>();
    private List<Socket> connected = Collections.synchronizedList(new ArrayList<>());
    private int owner;
    private ServerSocket serverSocket;
    private boolean candidat;
    private int id;
    private Random rand = new Random(0);
    private int send = 0;
    private int receive = 0;
    int puissance = 1;

    public Node(int id, String filename, boolean candidat) throws InterruptedException {
        loadFile(filename);
        id -= 3000;
        this.id = id;
        this.candidat = candidat;
        try {
            serverSocket = new ServerSocket(id + 3000);
            init();
            while (true) {
                if (!todd()) {
                    //break;
                }
                Thread.sleep(1000);
                if (!teven()) {
                    //break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("GOTO SLEEP FOREVER " + id);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        System.out.println("t0");
        if (candidat) {
            owner = id;
            System.out.println(id);
            Socket socket;
            //tant qu'on n'arrive pas à se connecter
            int neighbour = rand.nextInt(tried.size());
            new Sender(id, tried.get(neighbour), connected).run();
            tried.remove(neighbour);
            send++;
            puissance *= 2;
        }
    }

    private boolean todd() throws IOException {
        System.out.println("t impaire");
        Socket best = null;
        int bestValue = -1;
        while (true) {
            try {
                serverSocket.setSoTimeout(1000);
                Socket socket = serverSocket.accept();
                int tempValue = socket.getInputStream().read();
                System.out.println("receive value " + tempValue);
                if (tempValue > bestValue) {
                    if (best != null) {
                        best.getOutputStream().write(NOT_OK);
                        best.getOutputStream().flush();
                        System.out.println("answer not ok");
                    }
                    best = socket;
                    bestValue = tempValue;
                } else {
                    socket.getOutputStream().write(NOT_OK);
                    socket.getOutputStream().flush();
                    System.out.println("answer not ok");
                }
                break;
            } catch (SocketTimeoutException ex) {
                break;
            }
        }
        if (best != null) {
            if (bestValue > id) {
                candidat = false;
                owner = bestValue;
                System.out.println("loose turn");
                best.getOutputStream().write(OK);
                best.getOutputStream().flush();
                System.out.println("answer ok");
                return false;
            } else {
                System.out.println("win turn");
                candidat = true;
                best.getOutputStream().write(NOT_OK);
                best.getOutputStream().flush();
                System.out.println("answer not ok");
            }
        }
        return true;
    }

    private boolean teven() throws IOException {
        System.out.println("t paire");
        for (int i = 0; i < connected.size(); i++) {
            Socket client = connected.get(i);
            int data = client.getInputStream().read();
            if (data == OK) {
                System.out.println("receive ok");
                receive++;
            } else {
                System.out.println("receive not ok");
            }
            client.close();
        }
        connected.clear();
        if (candidat) {
            if (send > receive) {
                candidat = false;
                System.out.println("loose turn because " + send + " < " + receive);
                send = 0;
                receive = 0;
                return false;
            } else {
                send = 0;
                receive = 0;
                System.out.println("win turn because " + send + " >= " + receive);
                System.out.println("puissance " + puissance);
                for (int i = 0; i < puissance && !tried.isEmpty(); i++) {
                    int neighbour = rand.nextInt(tried.size());
                    new Sender(id, tried.get(neighbour), connected).run();
                    tried.remove(neighbour);
                    send++;
                }
                puissance *= 2;
                if (tried.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void loadFile(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                int i = sc.nextInt();
                neighbors.add(i);
                tried.add(i);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
