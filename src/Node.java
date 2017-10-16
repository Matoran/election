import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Node {
    private ArrayList<Integer> neighbors = new ArrayList<>();
    private HashMap<Integer, Boolean> tried = new HashMap<>();
    private int owner;

    public Node(int id, String filename, boolean candidat) {
        Random rand = new Random();
        int condition = rand.nextInt(10);
        System.out.println("Id=" + id + " condition=" + condition);
        loadFile(filename);
        int index = 0;
        try {
            ServerSocket serverSocket = new ServerSocket(id);
            if (candidat) {
                owner = id;
                System.out.println(id);
                Socket socket;
                //tant qu'on n'arrive pas à se connecter
                while (true) {
                    try {
                        int neighbour = rand.nextInt(neighbors.size());
                        socket = new Socket(InetAddress.getLoopbackAddress(), neighbors.get(neighbour));
                        socket.getOutputStream().write(condition);
                        socket.getOutputStream().flush();
                        break;
                    } catch (IOException ignored) {
                    }
                }
            }
            Socket best;
            int bestValue = -1;
            while (true) {
                try {
                    serverSocket.setSoTimeout(5000);
                    Socket client = serverSocket.accept();
                    BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
                    BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
                    int tempValue = bis.read();
                    if(tempValue > bestValue){
                        best = client;
                    }
                } catch (SocketTimeoutException ignored) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //impossible d'être ici
        while (true) {
            System.out.println("Mélissa");
        }

    }

    private void loadFile(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                int i = sc.nextInt();
                neighbors.add(i);
                tried.put(i, false);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
