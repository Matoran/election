import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Node {
    public static final int OK = -2;
    public static final int NOT_OK = -3;
    private ArrayList<Integer> neighbors = new ArrayList<>();
    private ArrayList<Integer> tried = new ArrayList<>();
    private ArrayList<Socket> connected = new ArrayList<>();
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
            puissance *= 2;
            while (true) {
                todd();
                Thread.sleep(1000);
                if (!teven()) {
                    break;
                }
                puissance *= 2;
                send = 0;
                receive = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("GOTO SLEEP FOREVER");
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
            while (true) {
                try {
                    socket = new Socket(InetAddress.getLoopbackAddress(), tried.get(neighbour));
                    connected.add(socket);
                    tried.remove(neighbour);
                    socket.getOutputStream().write(id);
                    socket.getOutputStream().flush();
                    send++;
                    break;
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void todd() throws IOException {
        System.out.println("t impaire");
        int best = -1;
        int bestValue = -1;
        while (true) {
            try {
                //serverSocket.setSoTimeout(1000);
                connected.add(serverSocket.accept());
                int tempValue = connected.get(connected.size() - 1).getInputStream().read();
                System.out.println("tempValue" + tempValue);
                if (tempValue > bestValue) {

                    best = connected.size() - 1;
                    bestValue = tempValue;
                }else{
                    connected.get(connected.size() -1).getOutputStream().write(NOT_OK);
                    connected.get(connected.size() -1).getOutputStream().flush();
                }
                break;
            } catch (SocketTimeoutException ex) {
                break;
            }
        }
        if (best != -1) {
            if (bestValue > id) {
                System.out.println(best + "best");
                System.out.println(bestValue + " is better than " + id);
                candidat = false;
                owner = bestValue;
                connected.get(best).getOutputStream().write(OK);
            } else {
                candidat = true;
                connected.get(best).getOutputStream().write(NOT_OK);
            }
            connected.get(best).getOutputStream().flush();
        }
    }

    private boolean teven() throws IOException {
        System.out.println("t paire");
        for (int i = 0; i < connected.size(); i++) {
            Socket client = connected.get(i);
            int data = client.getInputStream().read();
            System.out.println("DATA"+ data);
            if (data == OK) {
                receive++;
                System.out.println("RECEIVE ++");
            }
        }
        if (candidat) {
            if (send > receive) {
                candidat = false;
            } else {
                System.out.println("YESSSSSSS");
                for (int i = 0; i < puissance && !tried.isEmpty(); i++) {
                    int neighbour = rand.nextInt(tried.size());
                    Socket socket = new Socket(InetAddress.getLoopbackAddress(), tried.get(neighbour));
                    tried.remove(neighbour);
                    socket.getOutputStream().write(id);
                    socket.getOutputStream().flush();
                    send++;
                }
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
