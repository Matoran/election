package run.mato.election;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Node {
    private ArrayList<Integer> neighbors;
    private HashMap<Integer, Boolean> tried;
    private int owner;

    public Node(int id, String filename, boolean candidat) {
        Random rand = new Random();
        int condition = rand.nextInt(10);
        loadFile(filename);
        int index = 0;
        if(candidat){
            owner = id;
            try {
                ServerSocket serverSocket = new ServerSocket(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //neighbors.get(0);
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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
