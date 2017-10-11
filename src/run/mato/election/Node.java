package run.mato.election;

import java.util.ArrayList;
import java.util.Random;

public class Node {
    private int condition;
    private int id;
    private boolean candidat;
    private ArrayList<Node> tried;
    private Node owner;

    public Node(int id, boolean candidat) {
        this.id = id;
        this.candidat = candidat;
        Random rand = new Random();
        condition = rand.nextInt(10);
        loadFile();
    }

    private void loadFile() {

    }
}
